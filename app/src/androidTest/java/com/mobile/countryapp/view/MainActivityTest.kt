package com.mobile.countryapp.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mobile.countryapp.R
import com.mobile.countryapp.utils.ConnectionLiveData
import com.mobile.countryapp.utils.MyIdlingResource
import com.mobile.countryapp.view.ExtraAssertions.isGone
import com.mobile.countryapp.view.ExtraAssertions.isVisible
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var homeRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun testSetUp() {
        ConnectionLiveData.mNetworkStatusDisabledForTesting =
            true // Disabled network status for testing checking

        IdlingRegistry.getInstance().register(MyIdlingResource.getIdlingResource())
    }

    @After
    fun testTearDown() {
        IdlingRegistry.getInstance().unregister(MyIdlingResource.getIdlingResource())
    }

    @Test
    fun testLoadingDataOnNetworkAvailability() {

        homeRule.scenario.onActivity { activity ->
            // Setting network state enabled
            activity.mCountryViewModel.setNetworkState(true)
        }

        onView(withId(R.id.rv_country_data)).check(isVisible())
        onView(withId(R.id.tv_message)).check(isGone())
        onView(withId(R.id.pb_loading)).check(isGone())
    }

    @Test
    fun testNetworkConnectivityNotAvailable() {
        ConnectionLiveData.mNetworkStatusDisabledForTesting = true

        homeRule.scenario.onActivity { activity ->
            // Setting network state disabled
            activity.mCountryViewModel.setNetworkState(false)
        }

        onView(withId(R.id.tv_message)).check(isVisible())
        onView(withId(R.id.pb_loading)).check(isGone())
        onView(withText("No network connectivity")).check(isVisible()) // Content check
    }

}