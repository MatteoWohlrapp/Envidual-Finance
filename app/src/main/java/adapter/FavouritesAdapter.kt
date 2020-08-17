package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ListAdapter
import com.example.envidual.finance.touchlab.R
import com.example.envidual.finance.touchlab.databinding.FavouritesCardviewBinding
import domain.data.CompanyData
import fragments.FavouritesFragment
import fragments.FavouritesFragmentDirections

class FavouritesAdapter : ListAdapter<CompanyData, FavouritesViewHolder>(
    CompanyDiffCallback()
){

    private lateinit var binding : FavouritesCardviewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        binding = FavouritesCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return FavouritesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        with(holder){
            bind(getItem(position))
            itemView.setOnClickListener {
                val action = FavouritesFragmentDirections.actionFavouritesToCompanyDetailedFragment(
                    getItem(position).country!!,
                    getItem(position).currency!!,
                    getItem(position).finnhubIndustry!!,
                    getItem(position).ipo!!,
                    getItem(position).logo!!,
                    getItem(position).marketCapitalization.toString()!!,
                    getItem(position).name!!,
                    getItem(position).ticker!!
                    )
                val navController = Navigation.findNavController(itemView).navigate(action)
            }
        }
    }

}