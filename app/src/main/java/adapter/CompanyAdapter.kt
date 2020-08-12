package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.envidual.finance.touchlab.databinding.DataCardviewBinding
import domain.data.CompanyData

class CompanyAdapter : ListAdapter<CompanyData, CompanyViewHolder>(
    CompanyDiffCallback()
){

    private lateinit var binding : DataCardviewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        binding = DataCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CompanyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}