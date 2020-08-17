package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.envidual.finance.touchlab.databinding.SearchesCardviewBinding
import domain.data.CompanyData

class SearchesAdapter : ListAdapter<CompanyData, SearchesViewHolder>(
    CompanyDiffCallback()
){

    private lateinit var binding : SearchesCardviewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchesViewHolder {
        binding = SearchesCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SearchesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}