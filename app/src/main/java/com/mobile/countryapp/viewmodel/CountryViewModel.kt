package com.mobile.countryapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.test.espresso.idling.CountingIdlingResource
import com.mobile.countryapp.R
import com.mobile.countryapp.api.ApiHelper
import com.mobile.countryapp.model.CountryModel
import com.mobile.countryapp.utils.ConnectionLiveData
import com.mobile.countryapp.utils.Resource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.*
import javax.inject.Inject

@ActivityRetainedScoped
class CountryViewModel @Inject constructor(
    private val application: Application,
    dispatcher: CoroutineDispatcher,
    private val idlingResource: CountingIdlingResource? = null,
    private val apiHelper: ApiHelper,
    private val connectionLiveData: ConnectionLiveData
) :
    ViewModel() {

    private val mJob = Job()
    private val mCountryScope = CoroutineScope(dispatcher + mJob)
    private var mResultLiveData = MutableLiveData<Resource<CountryModel>>()

    fun getCountryResultLiveData(): LiveData<Resource<CountryModel>> = mResultLiveData

    init {
        fetchCountryResults()
    }

    /**
     *  getting the country results from Rest api
     */
    fun fetchCountryResults() {
        idlingResource?.increment()

        // checking the network availability
        if (connectionLiveData.value != true) {
            mResultLiveData.value =
                Resource.error(null, application.getString(R.string.no_network_connectivity))
            return
        }

        mResultLiveData.value = (Resource.loading(null))

        // loading the country data in the IO thread
        mCountryScope.launch {
            val response = apiHelper.getCountryData()
            // update the data in the Main thread
            if (response.isSuccessful) {
                mResultLiveData.postValue(Resource.success(response.body()!!))
            } else {
                mResultLiveData.postValue(
                    Resource.error(
                        null,
                        application.getString(R.string.error_response)
                    )
                )
            }
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        mResultLiveData.value = Resource.error(
            null,
            exception.message ?: application.getString(R.string.error_response)
        )
    }


    override fun onCleared() {
        super.onCleared()
        mJob.cancel()
    }
}
