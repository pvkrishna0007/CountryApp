package com.mobile.countryapp.api

import com.mobile.countryapp.model.CountryModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("s/2iodh4vg0eortkl/facts.json")
    suspend fun getCountryData(): Response<CountryModel>

}