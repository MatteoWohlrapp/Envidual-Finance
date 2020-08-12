package adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.envidual.finance.touchlab.databinding.DataCardviewBinding
import domain.data.CompanyData

class CompanyViewHolder constructor(
    private val binding: DataCardviewBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(companyData: CompanyData) {
        binding.companyName.text =  companyData.name
        binding.companyTicker.text = companyData.ticker
    }
}