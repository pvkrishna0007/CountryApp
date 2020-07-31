package com.mobile.countryapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mobile.countryapp.api.ApiHelper
import com.mobile.countryapp.api.ApiInterface
import com.mobile.countryapp.model.CountryModel
import com.mobile.countryapp.utils.ConnectionLiveData
import com.mobile.countryapp.utils.Status
import com.mobile.countryapp.viewmodel.CountryViewModel
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
@Config(manifest = Config.NONE)
class CountryViewModelTest {

    // Run tasks synchronously
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    // Mock variables
    @Mock
    private lateinit var apiInterface: ApiInterface

    // private variables
    private val testDispatcher = TestCoroutineDispatcher()

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
            dispatcher = testDispatcher,
            apiHelper = ApiHelper(apiInterface),
            connectionLiveData = ConnectionLiveData(applicationMock).apply { value = false }
        )

        Assert.assertEquals(
            applicationMock.getString(R.string.no_network_connectivity),
            viewModel.getCountryResultLiveData().value?.message
        )
    }

    @Test
    fun testSuccessResponseOnInternetAvailable() = testDispatcher.runBlockingTest {

        val response = Response.success(CountryModel())
        `when`(apiInterface.getCountryData()).thenReturn(response)

        val helper = ApiHelper(apiInterface)
        val applicationMock = Mockito.mock(Application::class.java)

        val viewModel = CountryViewModel(
            application = applicationMock as Application,
            dispatcher = testDispatcher,
            apiHelper = helper,
            connectionLiveData = ConnectionLiveData(applicationMock).apply { value = true }
        )

        Assert.assertEquals(Status.SUCCESS, viewModel.getCountryResultLiveData().value?.status)
    }

    @Test
    fun testFailureResponseOnInternetAvailable() = testDispatcher.runBlockingTest {

        val response = Response.error<CountryModel>(
            500,
            ResponseBody.create(MediaType.parse("application/json"), "")
        )

        `when`(apiInterface.getCountryData()).thenReturn(response)

        val helper = ApiHelper(apiInterface)
        val applicationMock = Mockito.mock(Application::class.java)
        `when`<Any>(applicationMock.getString(R.string.error_response))
            .thenReturn("Error Response")

        val viewModel = CountryViewModel(
            application = applicationMock as Application,
            dispatcher = testDispatcher,
            apiHelper = helper,
            connectionLiveData = ConnectionLiveData(applicationMock).apply { value = true }
        )

        Assert.assertEquals(Status.ERROR, viewModel.getCountryResultLiveData().value?.status)
    }

}