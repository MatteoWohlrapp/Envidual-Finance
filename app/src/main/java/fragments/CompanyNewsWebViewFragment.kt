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
import android.webkit.WebViewClient
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
import com.example.envidual.finance.touchlab.databinding.CompanyNewsWebViewBinding
import domain.data.CompanyNews
import domain.use_cases.GetCompanyNewsByTickerUseCase
import kotlinx.android.synthetic.main.company_data_detailed.*
import kotlinx.android.synthetic.main.company_news_web_view.*
import kotlinx.coroutines.withContext
import viewmodel.CompanyDetailedViewModel

class CompanyNewsWebViewFragment : Fragment() {

    private lateinit var binding: CompanyNewsWebViewBinding
    private var url = "https://google.com"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = CompanyNewsWebViewBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        url = CompanyNewsWebViewFragmentArgs.fromBundle(arguments!!).url
        binding.companyNewsWebView.webViewClient = WebViewClient()
        binding.companyNewsWebView.loadUrl(url)
    }
}