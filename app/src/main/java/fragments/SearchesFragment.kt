package fragments

import adapter.ItemSpacingDecoration
import adapter.SearchesAdapter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.envidual.finance.touchlab.R
import domain.data.CompanyData
import kotlinx.android.synthetic.main.search_fragment.*
import viewmodel.SearchesViewModel


class SearchesFragment : Fragment(){

    lateinit var searchesViewModel : SearchesViewModel
    lateinit var searchesAdapter: SearchesAdapter
    private val onCheckboxClick = MutableLiveData<CheckBoxCompany>()
    lateinit var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback
    private var swipeBackground = ColorDrawable(Color.parseColor("#FF0000"))
    private lateinit var deleteIcon : Drawable

    override fun onCreate(savedInstanceState: Bundle?) {

        searchesViewModel = ViewModelProviders.of(this).get(SearchesViewModel::class.java)

        super.onCreate(savedInstanceState)

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

        searchesAdapter= SearchesAdapter(onCheckboxClick)
        onCheckboxClick.observe( viewLifecycleOwner,
            Observer {
                if(it.boolean){
                    searchesViewModel.addCompanyToFavourites(it.companyData)
                } else{
                    searchesViewModel.removeCompanyFromFavourites(it.companyData)
                }
            }
        )
        searchesViewModel.searches.observe(viewLifecycleOwner, Observer {
            if(it.first().name == null)
                Toast.makeText(this.context, "Sorry, company not found", Toast.LENGTH_SHORT).show()
            else
                searchesAdapter.submitList(it)
        })

        searchesViewModel.searchesProgressBar.observe(viewLifecycleOwner, Observer{
            if(it)
                searches_progress_bar.visibility = View.VISIBLE
            else
                searches_progress_bar.visibility = View.GONE
        })

        searches_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ItemSpacingDecoration())
            adapter = searchesAdapter
        }

        Log.d("Fragment", "Called on view created in search")
        setHasOptionsMenu(true)

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null){
                    searchesViewModel.getCompanyDataForSearchesWithTicker(query!!)
                }
                Log.d("Searches", "onQueryTextSubmit: $query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean{
                if(newText == ""){
                    searchesViewModel.getCompanyDataForSearches()
                    Log.d("Searches", "onQueryTextChange: $newText")

                }

                return true
            }
        })

        searchView.setOnCloseListener {
            Log.d("Searches", "onCloseListener")

            searchesViewModel.getCompanyDataForSearches()

            true
        }

        searchView
    }

}

data class CheckBoxCompany(var boolean: Boolean, var companyData: CompanyData)