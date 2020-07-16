package com.mobile.countryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobile.countryapp.api.ApiHelper
import com.mobile.countryapp.model.CountryModel
import com.mobile.countryapp.utils.MyIdlingResource
import com.mobile.countryapp.utils.Resource
import kotlinx.coroutines.*

class CountryViewModel(private val apiHelper: ApiHelper) : ViewModel() {

    private var isNetworkAvailable: Boolean? = null
    private val mJob = Job()
    private val mCountryScope = CoroutineScope(Dispatchers.IO + mJob)
    private var mResultLiveData = MutableLiveData<Resource<CountryModel>>()

    fun getCountryResultLiveData(): LiveData<Resource<CountryModel>> = mResultLiveData

    /**
     *  getting the country results from Rest api
     */
    fun fetchCountryResults() {
        MyIdlingResource.getIdlingResource().increment()

        if (!isNetworkAvailable!!) {
            mResultLiveData.value = Resource.error(null, "No network connectivity")
            return
        }
        mResultLiveData.value = (Resource.loading(null))

        mCountryScope.launch {
            val response = apiHelper.getCountryData()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    mResultLiveData.value = Resource.success(response.body()!!)
                } else {
                    mResultLiveData.value = Resource.error(null, "Error in response")
                }
            }
        }
    }

    /**
     *  setting the network state on connectivity changes
     */
    fun setNetworkState(isOnline: Boolean) {

        if (isNetworkAvailable == null) {
            isNetworkAvailable = isOnline
            fetchCountryResults() // Getting the results when network state available
        } else {
            isNetworkAvailable = isOnline
        }
    }

    override fun onCleared() {
        super.onCleared()
        mJob.cancel()
    }
}

class CountryModelFactory(var apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CountryViewModel(apiHelper) as T
    }
}