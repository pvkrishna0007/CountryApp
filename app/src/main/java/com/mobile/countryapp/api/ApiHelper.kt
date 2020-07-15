package com.mobile.countryapp.api

class ApiHelper(private val apiInterface: ApiInterface) {

    fun getCountryData() = apiInterface.getCountryData()

}