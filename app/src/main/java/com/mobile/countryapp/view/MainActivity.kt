package com.mobile.countryapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobile.countryapp.R
import com.mobile.countryapp.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(DashboardFragment())
    }

    private fun replaceFragment(fragment: BaseFragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment, null)
            .commitNowAllowingStateLoss()
    }

}
