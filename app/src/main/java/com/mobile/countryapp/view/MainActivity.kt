package com.mobile.countryapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mobile.countryapp.R
import com.mobile.countryapp.api.ApiHelper
import com.mobile.countryapp.api.RetrofitManager
import com.mobile.countryapp.utils.ConnectionLiveData
import com.mobile.countryapp.utils.Status
import com.mobile.countryapp.utils.visibleGone
import com.mobile.countryapp.viewmodel.CountryModelFactory
import com.mobile.countryapp.viewmodel.CountryViewModel
import com.poc.passenger.view.adapters.ItemDetailAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mItemAdapter: ItemDetailAdapter
    private lateinit var mConnectionLiveData: ConnectionLiveData
    private lateinit var mCountryViewModel: CountryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        initializeListeners()
    }

    private fun initializeViews() {
        // Initialized view model setup
        val api = RetrofitManager.getApiInterface()
        val countryModelFactory = CountryModelFactory(ApiHelper(api))
        mCountryViewModel =
            ViewModelProviders.of(this, countryModelFactory).get(CountryViewModel::class.java)

        // Added item details adapter to recycler view
        mItemAdapter = ItemDetailAdapter()
        rv_country_data.adapter = mItemAdapter

        // Added network connectivity change observer
        mConnectionLiveData = ConnectionLiveData(this)
    }

    private fun initializeListeners() {
        // Added data change observer for country results
        mCountryViewModel.getCountryResultLiveData().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    tv_message.visibleGone(false)
                    if (!swipe_container.isRefreshing) {
                        pb_loading.visibleGone(true) //Showing progress bar when swipe refresh was not visible
                    }
                }
                Status.SUCCESS -> {
                    // Hiding loaders and updated adapter
                    pb_loading.visibleGone(false)
                    swipe_container.isRefreshing = false
                    tv_message.visibleGone(false)
                    mItemAdapter.setItemList(it.data?.rows)

                    title = it.data?.title // Setting the title here
                }
                Status.ERROR -> {
                    // Hiding loaders and shown message on error
                    pb_loading.visibleGone(false)
                    swipe_container.isRefreshing = false
                    tv_message.visibleGone(true)
                    tv_message.text = it.message
                }
            }
        })

        swipe_container.setOnRefreshListener {
            // Getting results on swipe to refresh
            mItemAdapter.clearItemList()
            mCountryViewModel.fetchCountryResults()
        }

        mConnectionLiveData.observe(this, Observer {
            mCountryViewModel.setNetworkState(it!!)
        })
    }
}
