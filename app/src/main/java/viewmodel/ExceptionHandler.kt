package viewmodel

import androidx.lifecycle.MutableLiveData
import io.ktor.client.features.*
import remote.CompanyDataNotFoundException
import remote.CompanyNewsNotFoundException
import remote.NoInternetConnectionException
import remote.RequestLimitReachedException

fun handleException(e: Exception, errorMessage: MutableLiveData<String>){
    when (e) {
        is RequestLimitReachedException -> errorMessage.postValue(e.message)
        is CompanyDataNotFoundException -> errorMessage.postValue(e.message)
        is CompanyNewsNotFoundException -> errorMessage.postValue(e.message)
        is NoInternetConnectionException -> errorMessage.postValue(e.message)
        else -> throw e
    }
}