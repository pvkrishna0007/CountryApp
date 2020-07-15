package com.mobile.countryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobile.countryapp.api.ApiHelper
import com.mobile.countryapp.model.CountryModel
import com.mobile.countryapp.utils.Resource
import retrofit2.Call
import retrofit2.Response

class CountryViewModel(val apiHelper: ApiHelper) : ViewModel() {

    private var isNetworkAvailable: Boolean? = null
    private var mResultLiveData = MutableLiveData<Resource<CountryModel>>()

    fun getCountryResultLiveData(): LiveData<Resource<CountryModel>> = mResultLiveData

    /**
     *  fetching the country results from Rest api
     */
    fun fetchCountryResults() {

        if (!isNetworkAvailable!!) {
            mResultLiveData.value = (Resource.error(null, "No network connectivity"))
            return
        }

        mResultLiveData.postValue(Resource.loading(null))

        apiHelper.getCountryData().enqueue(object : retrofit2.Callback<CountryModel> {

            override fun onResponse(
                call: Call<CountryModel>,
                response: Response<CountryModel>
            ) {
                mResultLiveData.value = (Resource.success(response.body()!!))
            }

            override fun onFailure(call: Call<CountryModel>, t: Throwable) {
                mResultLiveData.value = (Resource.error(null, t.message ?: "Error in response"))
            }
        })
    }

    /**
     *  setting the network state on connectivity changes
     */
    fun setNetworkState(isOnline: Boolean) {

        if (isNetworkAvailable == null) {
            isNetworkAvailable = isOnline
            fetchCountryResults()
        } else {
            isNetworkAvailable = isOnline
        }
    }
}

class CountryModelFactory(var apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CountryViewModel(apiHelper) as T
    }
}