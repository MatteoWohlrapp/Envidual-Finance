package fragments

import adapter.CompanyNewsAdapter
import adapter.ItemSpacingDecoration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.example.envidual.finance.touchlab.R
import com.example.envidual.finance.touchlab.databinding.CompanyDataDetailedBinding
import domain.data.CompanyNews
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.company_data_detailed.*
import viewmodel.CompanyDetailedViewModel

class CompanyDataDetailedFragment : Fragment() {

        private lateinit var binding: CompanyDataDetailedBinding
    lateinit var companyDetailedViewModel: CompanyDetailedViewModel
    lateinit var companyNewsAdapter: CompanyNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        companyDetailedViewModel =
            ViewModelProviders.of(this).get(CompanyDetailedViewModel::class.java)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        (activity as AppCompatActivity).supportActionBar?.title = "Favourites"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        findNavController().popBackStack()
        return super.onOptionsItemSelected(item)
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
            (activity as AppCompatActivity).supportActionBar?.title = args.name
            companyDetailedViewModel.getCompanyNewsByTicker(args.ticker)
        }

        companyNewsAdapter = CompanyNewsAdapter()
        company_news_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ItemSpacingDecoration())
            adapter = companyNewsAdapter
        }

        setupObservers()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            (activity as AppCompatActivity).supportActionBar?.title = "Favourites"
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
            findNavController().popBackStack()
        }
    }

    private fun setupObservers(){
        companyDetailedViewModel.companyNews.observe(viewLifecycleOwner, {
            companyNewsAdapter.submitList(it)
        })

        companyDetailedViewModel.companyNewsProgressBar.observe(viewLifecycleOwner, {
            if (it)
                company_news_progress_bar.visibility = View.VISIBLE
            else
                company_news_progress_bar.visibility = View.GONE
        })

        companyDetailedViewModel.companyNewsNotFound.observe(viewLifecycleOwner, {
            if (it)
                companyNewsAdapter.submitList(
                    listOf(
                        CompanyNews(
                            headline = "No news found",
                            datetime = 0,
                            source = "..."
                        )
                    )
                )
        })

        companyDetailedViewModel.toManyRequests.observe(viewLifecycleOwner, {
            if(it)
                Toast.makeText(context, "Too many network requests, try again later!", Toast.LENGTH_SHORT).show()
        })
    }
}