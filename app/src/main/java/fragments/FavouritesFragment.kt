package fragments

import adapter.FavouritesAdapter
import adapter.ItemSpacingDecoration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.envidual.finance.touchlab.R
import kotlinx.android.synthetic.main.favourites_fragment.*
import viewmodel.FavouritesViewModel

class FavouritesFragment : Fragment(){

    lateinit var favouritesViewModel : FavouritesViewModel

    lateinit var favouritesAdapter: FavouritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        favouritesViewModel = ViewModelProviders.of(this).get(FavouritesViewModel::class.java)

        super.onCreate(savedInstanceState)

        favouritesViewModel.getCompanyDataForFavourites()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Fragment", "Called on create view in favourites")

        return inflater.inflate(R.layout.favourites_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favouritesAdapter= FavouritesAdapter()
        favouritesViewModel.favourites.observe(viewLifecycleOwner, Observer { favouritesAdapter.submitList(it)})

        favourites_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ItemSpacingDecoration())
            adapter = favouritesAdapter
        }

        Log.d("Fragment", "Called on view created in favourites")

    }
}