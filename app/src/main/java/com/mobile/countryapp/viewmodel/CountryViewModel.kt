package com.mobile.countryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobile.countryapp.api.ApiHelper
import com.mobile.countryapp.model.CountryModel
import com.mobile.countryapp.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Response

class CountryViewModel(val apiHelper: ApiHelper) : ViewModel() {

    private var isNetworkAvailable: Boolean = false
    private val countryScope = CoroutineScope(Dispatchers.IO)
    private var resultLiveData = MutableLiveData<Resource<CountryModel>>()

    fun getCountryResultLiveData(): LiveData<Resource<CountryModel>> = resultLiveData

    init {
        // Requested for getting the country results
        fetchCountryResults()
    }

    /**
     *  fetching the country results from Rest api
     */
    fun fetchCountryResults() {
        if (!isNetworkAvailable) {
            resultLiveData.value = (Resource.error(null, "No network connectivity"))
            return
        }

        resultLiveData.postValue(Resource.loading(null))

        apiHelper.getCountryData().enqueue(object : retrofit2.Callback<CountryModel> {

            override fun onResponse(
                call: Call<CountryModel>,
                response: Response<CountryModel>
            ) {
                resultLiveData.value = (Resource.success(response.body()!!))
            }

            override fun onFailure(call: Call<CountryModel>, t: Throwable) {
                resultLiveData.value = (Resource.error(null, t.message ?: "Error in response"))
            }
        })
    }

    /**
     *  setting the network state on connectivity changes
     */
    fun setNetworkState(it: Boolean) {
        isNetworkAvailable = it
    }
}

class CountryModelFactory(var apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CountryViewModel(apiHelper) as T
    }
}