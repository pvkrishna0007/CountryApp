package com.mobile.countryapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobile.countryapp.R
import com.mobile.countryapp.api.ApiHelper
import com.mobile.countryapp.model.CountryModel
import com.mobile.countryapp.utils.MyIdlingResource
import com.mobile.countryapp.utils.Resource
import kotlinx.coroutines.*

class CountryViewModel(private val application: Application, private val apiHelper: ApiHelper) :
    ViewModel() {

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

        // checking the network availability
        if (!isNetworkAvailable!!) {
            mResultLiveData.value =
                Resource.error(null, application.getString(R.string.no_network_connectivity))
            return
        }

        mResultLiveData.value = (Resource.loading(null))

        // loading the country data in the IO thread
        mCountryScope.launch(exceptionHandler) {
            val response = apiHelper.getCountryData()
            // update the data in the Main thread
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    mResultLiveData.value = Resource.success(response.body()!!)
                } else {
                    mResultLiveData.value =
                        Resource.error(null, application.getString(R.string.error_response))
                }
            }
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        mResultLiveData.value = Resource.error(
            null,
            exception.message ?: application.getString(R.string.error_response)
        )
    }

    /**
     *  setting the network state on connectivity changes
     *
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

class CountryModelFactory(private val application: Application, private val apiHelper: ApiHelper) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CountryViewModel(application, apiHelper) as T
    }
}