package com.mobile.countryapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mobile.countryapp.api.ApiHelper
import com.mobile.countryapp.api.ApiInterface
import com.mobile.countryapp.model.CountryModel
import com.mobile.countryapp.utils.Resource
import com.mobile.countryapp.viewmodel.CountryViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CountryViewModelTest {

    // Run tasks synchronously
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    // Mock variables
    @Mock
    private lateinit var apiInterface: ApiInterface
    @Mock
    private lateinit var observerResourceMock: Observer<Resource<CountryModel>>

    @Before
    fun initializeResources() {
        MockitoAnnotations.initMocks(this)
    }

    @After
    fun releaseResources() {
    }

    @Test
    fun testNoInternetConnectivityContentCheck() {
        val applicationMock = Mockito.mock(Application::class.java)
        `when`<Any>(applicationMock.getString(R.string.no_network_connectivity))
            .thenReturn("No network")

        val viewModel = CountryViewModel(
            application = applicationMock as Application,
            apiHelper = ApiHelper(apiInterface)
        )
        viewModel.getCountryResultLiveData().observeForever(observerResourceMock)
        viewModel.setNetworkState(false)

        verify(observerResourceMock).onChanged(
            Resource.error(
                null,
                applicationMock.getString(R.string.no_network_connectivity)
            )
        )
    }

    @Test
    fun testLoadingTestOnInternetAvailable() {

        val helper = ApiHelper(apiInterface)
        val applicationMock = Mockito.mock(Application::class.java)
        val viewModel = CountryViewModel(
            application = applicationMock as Application,
            apiHelper = helper
        )

        viewModel.getCountryResultLiveData().observeForever(observerResourceMock)
        viewModel.setNetworkState(true)

        verify(observerResourceMock).onChanged(Resource.loading(null))
    }

}