package viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.example.envidual.finance.touchlab.db.EnvidualFinanceDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import remote.FinanceRemote
import remote.RemoteFinanceInterface
import sql.DatabaseHelper
import org.koin.core.module.Module
import org.koin.dsl.module

public class StockDataViewModel(application: Application) : AndroidViewModel(application){

//    val stockLiveData = MutableLiveData<IntradayData>()
    private var remoteFinance : RemoteFinanceInterface = FinanceRemote()
    private var db : DatabaseHelper  = DatabaseHelper(AndroidSqliteDriver(EnvidualFinanceDatabase.Schema, application.baseContext, "EnvidualFinanceDatabase"), Dispatchers.IO)

    fun getCompanyData(symbol: String){
        viewModelScope.launch {
            val data = remoteFinance.getCompanyData(symbol)
//            stockLiveData.postValue(data)
            Log.d("Finance", data.toString())
        }
    }

    fun insertStockData(symbol: String){
        viewModelScope.launch {
            db.insertStockData(listOf(symbol))
        }
    }


    fun getStockData() {
        viewModelScope.launch {
            db.selectAllItems().collect { value -> Log.d("Finance", value.toString()) }
        }
    }



}