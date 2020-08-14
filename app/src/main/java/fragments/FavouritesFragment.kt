package fragments

import adapter.CompanyAdapter
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
import kotlinx.android.synthetic.main.explore_fragment.*
import viewmodel.FavouritesViewModel

class FavouritesFragment : Fragment(){

    lateinit var favouritesViewModel : FavouritesViewModel

    lateinit var companyAdapter: CompanyAdapter

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
        return inflater.inflate(R.layout.explore_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        companyAdapter= CompanyAdapter()
        favouritesViewModel.favourites.observe(viewLifecycleOwner, Observer { companyAdapter.submitList(it)
            Log.d("Explore", it.toString())
        })

        explore_company_data.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ItemSpacingDecoration())
            adapter = companyAdapter
        }
    }
}