package fragments

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import coil.api.load
import coil.transform.CircleCropTransformation
import com.example.envidual.finance.touchlab.R
import com.example.envidual.finance.touchlab.databinding.CompanydataDetailedBinding

class CompanyDetailedFragment : Fragment() {

    private lateinit var binding: CompanydataDetailedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)

        binding = CompanydataDetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (arguments != null) {
            val args = CompanyDetailedFragmentArgs.fromBundle(arguments!!)

            binding.detailedIndustryData.text = args.industry
            binding.detailedCountryData.text = args.country
            binding.detailedIpoData.text = args.ipo
            binding.detailedMarketCapitalizationData.text =
                args.marketCapitalization + " " + args.currency
            binding.detailedName.text = args.name
            binding.detailedTicker.text = args.ticker
            binding.companyDetailedLogo.load(args.logo){
            }
        }

        binding.detailedCompanyBack.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_companyDetailedFragment_to_favourites)
        }
    }
}