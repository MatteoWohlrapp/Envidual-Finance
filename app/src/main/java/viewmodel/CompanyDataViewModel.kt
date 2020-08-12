package viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.data.CompanyData
import domain.use_cases.GetCompanyByTickerUseCase
import kotlinx.coroutines.launch
import java.util.*

public class CompanyDataViewModel() : ViewModel(){

    var companyDataList = MutableLiveData<List<CompanyData>>()
    val companyByTickerUseCase = GetCompanyByTickerUseCase()
    private val listOfCompanies = LinkedList<CompanyData>()

    fun getCompanyData(ticker: String){
        viewModelScope.launch {
            val data = companyByTickerUseCase.getCompany(ticker)
            Log.d("Finance", data.toString())
            listOfCompanies.add(data)
            companyDataList.postValue(listOfCompanies)
        }
    }
}