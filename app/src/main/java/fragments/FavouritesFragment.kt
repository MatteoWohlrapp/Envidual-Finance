package fragments

import adapter.FavouritesAdapter
import adapter.ItemSpacingDecoration
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.envidual.finance.touchlab.R
import kotlinx.android.synthetic.main.favourites_fragment.*
import viewmodel.FavouritesViewModel

class FavouritesFragment : Fragment(){

    lateinit var favouritesViewModel : FavouritesViewModel
    lateinit var favouritesAdapter: FavouritesAdapter
    lateinit var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback
    private var swipeBackground = ColorDrawable(Color.parseColor("#FF0000"))
    private lateinit var deleteIcon : Drawable

    override fun onCreate(savedInstanceState: Bundle?) {

        favouritesViewModel = ViewModelProviders.of(this).get(FavouritesViewModel::class.java)

        super.onCreate(savedInstanceState)

        favouritesViewModel.getCompanyDataForFavourites()
        favouritesViewModel.updateCompanies()

        (activity as AppCompatActivity).supportActionBar?.title = "Favourites"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        setupRecyclerViewSwipeGestures()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favourites_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        favouritesAdapter= FavouritesAdapter()

        favourites_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ItemSpacingDecoration())
            adapter = favouritesAdapter
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(favourites_recycler_view)
    }

    private fun setupObservers(){
        favouritesViewModel.errorMessage.observe(viewLifecycleOwner, {
            Toast.makeText(
                this.context,
                it,
                Toast.LENGTH_SHORT
            ).show()
        })
        favouritesViewModel.favourites.observe(viewLifecycleOwner,
            { favouritesAdapter.submitList(it) })

        favouritesViewModel.favouritesProgressBar.observe(viewLifecycleOwner, {
            if (it)
                favourites_progress_bar.visibility = View.VISIBLE
            else
                favourites_progress_bar.visibility = View.GONE
        })
    }

    private fun setupRecyclerViewSwipeGestures(){
        deleteIcon = ContextCompat.getDrawable(this.requireContext(), R.drawable.delete_icon)!!

//        callback for the swipe gesture
        itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val companyData = favouritesAdapter.currentList[position]
                Log.d("Swipe", "Swiped position: $position")
                Log.d("Swipe", "Company name: ${companyData.name}")
                favouritesViewModel.deleteCompanyFromFavourites(companyData)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight)/2
                if(dX < 0){
                    swipeBackground.bounds.left = itemView.right + dX.toInt()
                    swipeBackground.bounds.top = itemView.top
                    swipeBackground.bounds.right = itemView.right
                    swipeBackground.bounds.bottom = itemView.bottom
                    deleteIcon.bounds.left = itemView.right - iconMargin - deleteIcon.intrinsicHeight
                    deleteIcon.bounds.top = itemView.top + iconMargin
                    deleteIcon.bounds.right = itemView.right - iconMargin
                    deleteIcon.bounds.bottom = itemView.bottom - iconMargin

                }

                swipeBackground.draw(c)
                deleteIcon.draw(c)

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

    }
}