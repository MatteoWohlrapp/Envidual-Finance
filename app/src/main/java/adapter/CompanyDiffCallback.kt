package adapter

import androidx.recyclerview.widget.DiffUtil
import domain.data.CompanyData

class CompanyDiffCallback : DiffUtil.ItemCallback<CompanyData>(){
    override fun areItemsTheSame(oldItem: CompanyData, newItem: CompanyData): Boolean {
        return  oldItem.ticker == newItem.ticker
    }

    override fun areContentsTheSame(oldItem: CompanyData, newItem: CompanyData): Boolean {
        return  oldItem == newItem
    }
}