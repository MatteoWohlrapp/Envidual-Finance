package viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.data.CompanyData
import domain.use_cases.GetCompanyByTickerUseCase
import domain.use_cases.GetCompanysForExploreUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.StringBuilder
import java.util.*
import kotlin.coroutines.suspendCoroutine

public class CompanyDataViewModel() : ViewModel(){

    var count = 0

    var companyDataList = MutableLiveData<List<CompanyData>>()
    val explorerUseCase = GetCompanysForExploreUseCase()

    private val listOfCompanies = LinkedList<CompanyData>()

    fun getCompanyData(ticker: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
            val data = explorerUseCase.invoke()
            companyDataList.postValue(data)
            }
        }
    }

//    @Synchronized
//    fun addCompanyData(data: CompanyData){
//        count = (count+1)%2
//        Log.d("ViewModel", "$count: added company: ${data.name}")
//        listOfCompanies.add(data)
//        companyDataList.postValue(listOfCompanies)
//        Log.d("ViewModel","companyDataList contains: ${printList()}")
//    }
//
//    fun printList() : String{
//        val builder = StringBuilder()
//        for(data in listOfCompanies)
//            builder.append(data.name + ", ")
//        return builder.toString()
//    }

}