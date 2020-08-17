package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.envidual.finance.touchlab.databinding.FavouritesCardviewBinding
import domain.data.CompanyData

class FavouritesAdapter : ListAdapter<CompanyData, FavouritesViewHolder>(
    CompanyDiffCallback()
){

    private lateinit var binding : FavouritesCardviewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        binding = FavouritesCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return FavouritesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}