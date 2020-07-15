package com.mobile.countryapp.api

import com.mobile.countryapp.model.CountryModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("s/2iodh4vg0eortkl/facts.json")
    fun getCountryData(): Call<CountryModel>

}