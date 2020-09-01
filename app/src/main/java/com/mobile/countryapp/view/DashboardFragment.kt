package com.mobile.countryapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import androidx.test.espresso.idling.CountingIdlingResource
import com.mobile.countryapp.R
import com.mobile.countryapp.base.BaseFragment
import com.mobile.countryapp.utils.Status
import com.mobile.countryapp.utils.visibleGone
import com.mobile.countryapp.view.adapter.ItemDetailAdapter
import com.mobile.countryapp.viewmodel.CountryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


@AndroidEntryPoint
class DashboardFragment : BaseFragment() {

    @Inject
    lateinit var mItemAdapter: ItemDetailAdapter
    @Inject
    lateinit var idlingResource: CountingIdlingResource

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    @Inject
    lateinit var mCountryViewModel: CountryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initializeViews()
        initializeListeners()
    }

    private fun initializeViews() {
        // Initialized view model setup
        // val countryModelFactory = CountryModelFactory(application, ApiHelper(apiInterface))
        // mCountryViewModel =
        //   ViewModelProviders.of(this, countryModelFactory).get(CountryViewModel::class.java)

        // Added item details adapter to recycler view
        // mItemAdapter = ItemDetailAdapter()
        rv_country_data.adapter = mItemAdapter

        // Added network connectivity change observer
//        mConnectionLiveData = ConnectionLiveData(this)

    }

    private fun initializeListeners() {
        // Added data change observer for country results
        mCountryViewModel.getCountryResultLiveData().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    tv_message.visibleGone(false)
                    if (!swipe_container.isRefreshing) {
                        pb_loading.visibleGone(true) //Showing progress bar when swipe refresh was not visible
                    }
                    rv_country_data.visibleGone(false)
                }
                Status.SUCCESS -> {
                    // Hiding loaders and updated adapter
                    pb_loading.visibleGone(false)
                    swipe_container.isRefreshing = false
                    tv_message.visibleGone(false)
                    rv_country_data.visibleGone(true)
                    mItemAdapter.setItemList(it.data?.rows)

                    setTitle(it.data?.title) // Setting the title here
                    try {
                        idlingResource.decrement()
                    } catch (ex: Throwable) {
                        ex.printStackTrace()
                    }
                }
                Status.ERROR -> {
                    // Hiding loaders and shown message on error
                    pb_loading.visibleGone(false)
                    swipe_container.isRefreshing = false
                    tv_message.visibleGone(true)
                    tv_message.text = it.message
                    try {
                        idlingResource.decrement()
                    } catch (ex: Throwable) {
                        ex.printStackTrace()
                    }
                }
            }
        })

        swipe_container.setOnRefreshListener {
            // Getting results on swipe to refresh
            mItemAdapter.clearItemList()
            mCountryViewModel.fetchCountryResults()
        }

    }

    private fun setTitle(title: String?) {
        activity?.title = title
    }
}

