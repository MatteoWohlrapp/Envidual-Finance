package adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ListAdapter
import com.example.envidual.finance.touchlab.databinding.FavouritesCardviewBinding
import domain.data.CompanyData
import fragments.FavouritesFragmentDirections

class FavouritesAdapter : ListAdapter<CompanyData, FavouritesViewHolder>(
    CompanyDataDiffCallback()
){

    private lateinit var binding : FavouritesCardviewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        binding = FavouritesCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return FavouritesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        Log.d("Position", "Binding at position ${holder.adapterPosition}")
        Log.d("Position", "With company: ${getItem(holder.adapterPosition).name}")
        with(holder){
            bind(getItem(adapterPosition))
            itemView.setOnClickListener {
                val action = FavouritesFragmentDirections.actionFavouritesToCompanyDetailedFragment(
                    getItem(adapterPosition).country!!,
                    getItem(adapterPosition).currency!!,
                    getItem(adapterPosition).finnhubIndustry!!,
                    getItem(adapterPosition).ipo!!,
                    getItem(adapterPosition).logo!!,
                    getItem(adapterPosition).marketCapitalization.toString()!!,
                    getItem(adapterPosition).name!!,
                    getItem(adapterPosition).shareOutstanding.toString()!!,
                    getItem(adapterPosition).ticker!!
                    )
                val navController = Navigation.findNavController(itemView).navigate(action)
            }
        }
    }
}

