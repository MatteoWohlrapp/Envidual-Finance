package fragments

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import coil.api.load
import com.example.envidual.finance.touchlab.R
import com.example.envidual.finance.touchlab.databinding.CompanyNewsDetailedBinding

class CompanyNewsDetailedFragment : Fragment() {

    private lateinit var binding: CompanyNewsDetailedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        super.onCreate(savedInstanceState)
    }

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
            val args = CompanyNewsDetailedFragmentArgs.fromBundle(requireArguments())

            binding.companyNewsHeadline.text = args.headline
            binding.companyNewsSummary.text = args.summary
            binding.companyNewsDatetime.text = args.datetime
            binding.companyNewsSource.text = args.source
            binding.companyNewsUrl.text = args.url
            binding.companyNewsImage.load(args.image){
//                size(1200)
            }
        }

        binding.companyNewsUrl.setOnClickListener {
            val url  = binding.companyNewsUrl.text
            val builder = CustomTabsIntent.Builder()
            val colorInt: Int = Color.parseColor("#145373")
            builder.setToolbarColor(colorInt)
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(this.requireContext(), Uri.parse(url.toString()))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().popBackStack()
        return super.onOptionsItemSelected(item)
    }
}