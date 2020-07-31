package com.mobile.countryapp.utils

import android.view.View
import android.widget.ImageView
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