package com.mobile.countryapp.api

class ApiHelper(private val apiInterface: ApiInterface) {

    suspend fun getCountryData() = apiInterface.getCountryData()

}