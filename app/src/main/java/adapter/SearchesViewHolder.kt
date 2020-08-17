package adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.envidual.finance.touchlab.databinding.SearchesCardviewBinding
import domain.data.CompanyData

class SearchesViewHolder constructor(
    private val binding: SearchesCardviewBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(companyData: CompanyData) {
        binding.companyName.text =  companyData.name
        binding.companyTicker.text = companyData.ticker
    }
}