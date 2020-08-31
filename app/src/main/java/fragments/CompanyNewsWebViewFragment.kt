package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.envidual.finance.touchlab.databinding.CompanyNewsWebViewBinding

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
        url = CompanyNewsWebViewFragmentArgs.fromBundle(requireArguments()).url
        binding.companyNewsWebView.webViewClient = WebViewClient()
        binding.companyNewsWebView.loadUrl(url)
        binding.companyNewsWebView.settings.javaScriptEnabled = true
    }
}