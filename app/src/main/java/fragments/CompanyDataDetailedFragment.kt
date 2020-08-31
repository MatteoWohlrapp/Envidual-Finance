package fragments

import adapter.CompanyNewsAdapter
import adapter.ItemSpacingDecoration
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import coil.transform.CircleCropTransformation
import com.example.envidual.finance.touchlab.R
import com.example.envidual.finance.touchlab.databinding.CompanyDataDetailedBinding
import domain.data.CompanyNews
import domain.use_cases.GetCompanyNewsByTickerUseCase
import kotlinx.android.synthetic.main.company_data_detailed.*
import kotlinx.coroutines.withContext
import viewmodel.CompanyDetailedViewModel

class CompanyDataDetailedFragment : Fragment() {

    private lateinit var binding: CompanyDataDetailedBinding
    lateinit var companyDetailedViewModel: CompanyDetailedViewModel
    lateinit var companyNewsAdapter: CompanyNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        companyDetailedViewModel = ViewModelProviders.of(this).get(CompanyDetailedViewModel::class.java)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CompanyDataDetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (arguments != null) {
            val args = CompanyDataDetailedFragmentArgs.fromBundle(requireArguments())

            binding.detailedIndustryData.text = args.industry
            binding.detailedCountryData.text = args.country
            binding.detailedIpoData.text = args.ipo
            binding.detailedMarketCapitalizationData.text =
                args.marketCapitalization + " " + args.currency
            binding.detailedName.text = args.name
            binding.detailedShareOutstandingData.text = args.shareOutstanding + " " + args.currency
            binding.detailedTicker.text = args.ticker
            binding.companyDetailedLogo.load(args.logo){
                size(200)
            }
            companyDetailedViewModel.getCompanyNewsByTicker(args.ticker)
        }

        companyNewsAdapter = CompanyNewsAdapter()

        companyDetailedViewModel.companyNews.observe(viewLifecycleOwner, Observer {
            companyNewsAdapter.submitList(it)
        })

        companyDetailedViewModel.companyNewsProgressBar.observe(viewLifecycleOwner, Observer {
            if(it)
                company_news_progress_bar.visibility = View.VISIBLE
            else
                company_news_progress_bar.visibility = View.GONE
        })

        companyDetailedViewModel.companyNewsNotFound.observe(viewLifecycleOwner, Observer {
            if(it)
                companyNewsAdapter.submitList(listOf(CompanyNews(
                    headline = "No news found",
                    datetime = 0,
                    source = "..."
                )))
        })

        company_news_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ItemSpacingDecoration())
            adapter = companyNewsAdapter
        }

        binding.detailedCompanyBack.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.popBackStack()
        }
    }
}