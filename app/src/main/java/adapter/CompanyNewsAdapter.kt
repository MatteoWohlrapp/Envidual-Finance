package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ListAdapter
import com.example.envidual.finance.touchlab.databinding.CompanyNewsCardviewBinding
import domain.data.CompanyData
import domain.data.CompanyNews
import fragments.CompanyDataDetailedFragmentDirections
import fragments.CompanyNewsDetailedFragmentDirections
import java.text.SimpleDateFormat
import java.util.*

class CompanyNewsAdapter : ListAdapter<CompanyNews, CompanyNewsViewHolder>(CompanyNewsDiffCallback()) {

    private lateinit var binding : CompanyNewsCardviewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyNewsViewHolder {
        binding = CompanyNewsCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CompanyNewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CompanyNewsViewHolder, position: Int) {
        with(holder){
            bind(getItem(adapterPosition))
            itemView.setOnClickListener {
                val action = CompanyDataDetailedFragmentDirections.actionCompanyDetailedFragmentToCompanyNewsDetailedFragment(
                    getItem(adapterPosition).image!!,
                    getItem(adapterPosition).headline!!,
                    getItem(adapterPosition).summary!!,
                    SimpleDateFormat("dd/MM/yyyy").format(Date(getItem(adapterPosition).datetime!!*1000)),
                    getItem(adapterPosition).source!!,
                    getItem(adapterPosition).url!!
                )
                val navController = Navigation.findNavController(itemView).navigate(action)
            }
        }
    }

}

