package com.mobile.countryapp.utils

import android.view.View
import android.widget.ImageView
import androidx.test.espresso.idling.CountingIdlingResource
import com.bumptech.glide.Glide
import com.mobile.countryapp.R

fun View.visibleGone(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun ImageView.loadImage(imageUrl: String?) {
    Glide.with(context)
        .load(imageUrl)
        .placeholder(R.drawable.ic_place_holder)
        .error(R.drawable.ic_place_holder)
        .into(this)
}

//fun AppCompatActivity.acquireLock(){
//    window.setFlags(
//        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//}
//
//fun AppCompatActivity.releaseLock(){
//    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//}


val mCountingIdlingResource = CountingIdlingResource("coroutines")