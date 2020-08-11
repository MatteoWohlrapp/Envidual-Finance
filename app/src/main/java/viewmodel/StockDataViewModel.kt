package viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.data.IntradayDataContainer
import kotlinx.coroutines.launch
import remote.FinanceRemote
import remote.RemoteFinanceInterface

public class StockDataViewModel : ViewModel(){

//    val stockLiveData = MutableLiveData<IntradayData>()
    private var remoteFinance : RemoteFinanceInterface = FinanceRemote()

    fun getIntraDay(){
        viewModelScope.launch {
            val data = remoteFinance.getIntradayData()
//            stockLiveData.postValue(data)
            Log.d("Finance", data.toString())
        }
    }



}