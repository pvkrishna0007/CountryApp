package com.mobile.countryapp.view

import android.os.Bundle
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()
    }

    private fun initialize() {
        // Initialized view model setup
        val api = RetrofitManager.getApiInterface()
        val countryModelFactory = CountryModelFactory(ApiHelper(api))
        val countryViewModel =
            ViewModelProviders.of(this, countryModelFactory).get(CountryViewModel::class.java)

        // Added item details adapter to recycler view
        val itemAdapter = ItemDetailAdapter()
        rv_country_data.adapter = itemAdapter

        // Added data change observer for country results
        countryViewModel.getCountryResultLiveData().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    pb_loading.visibleGone(true)
                }
                Status.SUCCESS -> {
                    pb_loading.visibleGone(false)
                    itemAdapter.setItemList(it.data?.rows)
                }
                Status.ERROR -> {
                    pb_loading.visibleGone(false)
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        // Added network connectivity change observer
        val connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this, Observer {
            countryViewModel.setNetworkState(it!!)
        })
    }
}
