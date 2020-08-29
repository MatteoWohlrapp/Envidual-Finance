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
import com.example.envidual.finance.touchlab.databinding.CompanyNewsDetailedBinding
import domain.data.CompanyNews
import domain.use_cases.GetCompanyNewsByTickerUseCase
import kotlinx.android.synthetic.main.company_data_detailed.*
import kotlinx.coroutines.withContext
import viewmodel.CompanyDetailedViewModel

class CompanyNewsDetailedFragment : Fragment() {

    private lateinit var binding: CompanyNewsDetailedBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CompanyNewsDetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (arguments != null) {
            val args = CompanyNewsDetailedFragmentArgs.fromBundle(arguments!!)

            binding.companyNewsHeadline.text = args.headline
            binding.companyNewsSummary.text = args.summary
            binding.companyNewsDatetime.text = args.datetime
            binding.companyNewsSource.text = args.source
            binding.companyNewsUrl.text = args.url
            binding.companyNewsImage.load(args.image){
                size(1200)
            }
        }
        binding.companyNewsBack.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.popBackStack()
        }
    }
}