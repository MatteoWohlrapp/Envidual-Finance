package fragments

import adapter.ItemSpacingDecoration
import adapter.SearchesAdapter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.envidual.finance.touchlab.R
import domain.data.CompanyData
import kotlinx.android.synthetic.main.favourites_fragment.*
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.coroutines.delay
import viewmodel.SearchesViewModel


class SearchesFragment : Fragment() {

    lateinit var searchesViewModel: SearchesViewModel
    lateinit var searchesAdapter: SearchesAdapter
    private val onCheckboxClick = MutableLiveData<CompanyData>()

    //    needed for swipe gestures on Recycler view
    lateinit var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback
    private var swipeBackground = ColorDrawable(Color.parseColor("#FF0000"))
    private lateinit var deleteIcon: Drawable

    //    variable to help scroll upwards only when new entry is made
    private var needToScrollToTop = false

    override fun onCreate(savedInstanceState: Bundle?) {
        searchesViewModel = ViewModelProviders.of(this).get(SearchesViewModel::class.java)
        super.onCreate(savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Search"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        setupRecyclerViewSwipeGestures()
        searchesViewModel.getCompanyDataForSearches()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.search_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchesAdapter = SearchesAdapter(onCheckboxClick)

        searches_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ItemSpacingDecoration())
            adapter = searchesAdapter
        }

        setupObservers()

        setHasOptionsMenu(true)

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(searches_recycler_view)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as SearchView


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                needToScrollToTop = true
                if (query != null) {
                    searchesViewModel.getCompanyDataForSearchesWithTicker(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    fun setupObservers() {
        onCheckboxClick.observe(viewLifecycleOwner,
            Observer {
                needToScrollToTop = false
                if (it.isFavourite!!) {
                    searchesViewModel.addCompanyToFavourites(it)
                } else {
                    searchesViewModel.deleteCompanyFromFavourites(it)
                }
            }
        )
        searchesViewModel.searches.observe(viewLifecycleOwner, Observer {
            if (needToScrollToTop)
                searchesAdapter.submitList(it) { searches_recycler_view.scrollToPosition(0) }
            else
                searchesAdapter.submitList(it)
        })

        searchesViewModel.searchesProgressBar.observe(viewLifecycleOwner, Observer {
            if (it)
                searches_progress_bar.visibility = View.VISIBLE
            else
                searches_progress_bar.visibility = View.GONE
        })

        searchesViewModel.companyNotFound.observe(viewLifecycleOwner, Observer {
            if (it)
                Toast.makeText(
                    this.context,
                    "Sorry wrong ticker, we could not find your company",
                    Toast.LENGTH_SHORT
                ).show()
        })
    }

    private fun setupRecyclerViewSwipeGestures() {
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
                needToScrollToTop = false
                val position = viewHolder.adapterPosition
                val companyData = searchesAdapter.currentList[position]
                searchesViewModel.deleteCompanyFromSearches(companyData)
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
                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2
                if (dX < 0) {
                    swipeBackground.bounds.left = itemView.right + dX.toInt()
                    swipeBackground.bounds.top = itemView.top
                    swipeBackground.bounds.right = itemView.right
                    swipeBackground.bounds.bottom = itemView.bottom
                    deleteIcon.bounds.left =
                        itemView.right - iconMargin - deleteIcon.intrinsicHeight
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