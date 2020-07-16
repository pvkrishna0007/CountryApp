package com.mobile.countryapp.utils

import androidx.test.espresso.idling.CountingIdlingResource

class MyIdlingResource {
    companion object {
        private val idlingResource = CountingIdlingResource("coroutines")

        fun increment() {
            idlingResource.increment()
        }

        fun decrement() {
            idlingResource.decrement()
        }

        fun getIdlingResource() = idlingResource
    }
}