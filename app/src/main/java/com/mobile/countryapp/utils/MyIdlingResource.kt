package com.mobile.countryapp.utils

import androidx.test.espresso.idling.CountingIdlingResource

object MyIdlingResource {

    private val idlingResource = CountingIdlingResource("coroutines")

    fun getIdlingResource() = idlingResource
}