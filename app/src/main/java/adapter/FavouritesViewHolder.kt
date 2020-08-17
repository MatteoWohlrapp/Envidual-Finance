package adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.envidual.finance.touchlab.databinding.FavouritesCardviewBinding
import domain.data.CompanyData

class FavouritesViewHolder constructor(
    private val binding: FavouritesCardviewBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(companyData: CompanyData) {
        binding.companyName.text =  companyData.name
        binding.companyTicker.text = companyData.ticker
        binding.companyCapitalization.text = companyData.marketCapitalization.toString() + " $"
    }
}