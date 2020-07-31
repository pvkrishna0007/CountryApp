package com.mobile.countryapp.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mobile.countryapp.R
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

    @Before
    fun testSetUp() {
        IdlingRegistry.getInstance().register(MyIdlingResource.getIdlingResource())
    }

    @After
    fun testTearDown() {
        IdlingRegistry.getInstance().unregister(MyIdlingResource.getIdlingResource())
    }

    /**
     *  Validating the screen UI when no network
     */
//    @Test
//    fun testALoadingDataOnConnectivityNotAvailable() {
//        ConnectionLiveData.mNetworkStatusDisabledForTesting = true
//
//        onView(withId(R.id.tv_message)).check(isVisible())
//        onView(withId(R.id.pb_loading)).check(isGone())
//        onView(withText(R.string.no_network_connectivity)).check(isVisible()) // Content check
//    }

    /**
     *  Validating the screen UI when network available. MUST REQUIRED INTERNET to pass testcase
     */
    @Test
    fun testLoadingDataOnNetworkAvailability() {
        onView(withId(R.id.rv_country_data)).check(isVisible())
        onView(withId(R.id.tv_message)).check(isGone())
        onView(withId(R.id.pb_loading)).check(isGone())
    }

}