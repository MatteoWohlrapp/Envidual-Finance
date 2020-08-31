package adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.envidual.finance.touchlab.databinding.CompanyNewsCardviewBinding
import com.example.envidual.finance.touchlab.databinding.FavouritesCardviewBinding
import domain.data.CompanyData
import domain.data.CompanyNews
import java.text.SimpleDateFormat
import java.util.*

class CompanyNewsViewHolder constructor(
    private val binding: CompanyNewsCardviewBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(companyNews: CompanyNews) {
        binding.companyNewsHeadline.text =  companyNews.headline
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val date = Date(companyNews.datetime!!*1000)
        binding.companyNewsDatetime.text = sdf.format(date)
        binding.companyNewsSource.text = companyNews.source
    }

}