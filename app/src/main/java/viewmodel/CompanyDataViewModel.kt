package viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.data.CompanyData
import domain.use_cases.GetCompanyBySymbolUseCase
import kotlinx.coroutines.launch
import java.util.*

public class CompanyDataViewModel() : ViewModel(){

    var companyDataList = MutableLiveData<List<CompanyData>>()
    val companyBySymbolUseCase = GetCompanyBySymbolUseCase()
    private val listOfCompanies = LinkedList<CompanyData>()

    fun getCompanyData(symbol: String){
        viewModelScope.launch {
            val data = companyBySymbolUseCase.getCompany(symbol)
//            stockLiveData.postValue(data)
            listOfCompanies.add(data)
            companyDataList.postValue(listOfCompanies)
            Log.d("Finance", data.toString())
        }
    }

    fun insertStockData(symbol: String){
        viewModelScope.launch {
//            db.insertStockData(listOf(symbol))
        }
    }


    fun getStockData() {
        viewModelScope.launch {
//            db.selectAllItems().collect { value -> Log.d("Finance", value.toString()) }
        }
    }



}