package com.mobile.countryapp.api

import javax.inject.Inject

class ApiHelper @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun getCountryData() = apiInterface.getCountryData()

}