package adapter

import androidx.recyclerview.widget.DiffUtil
import domain.data.CompanyData
import domain.data.CompanyNews

class CompanyNewsDiffCallback : DiffUtil.ItemCallback<CompanyNews>(){
    override fun areItemsTheSame(oldItem: CompanyNews, newItem: CompanyNews): Boolean {
        return  oldItem.ticker == newItem.ticker
    }

    override fun areContentsTheSame(oldItem: CompanyNews, newItem: CompanyNews): Boolean {
        return  oldItem == newItem
    }
}
