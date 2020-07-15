package com.poc.passenger.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.countryapp.R
import com.mobile.countryapp.model.Row
import com.mobile.countryapp.utils.loadImage
import kotlinx.android.synthetic.main.row_item_details.view.*

class ItemDetailAdapter : RecyclerView.Adapter<ItemDetailHolder>() {

    private var mItemList: List<Row>? = null

    /**
     * updated the item list and notified adapter
     */
    fun setItemList(itemList: List<Row>?) {
        this.mItemList = itemList
        notifyDataSetChanged()
    }

    fun clearItemList() {
        this.mItemList = null
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDetailHolder {
        return ItemDetailHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_item_details,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mItemList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ItemDetailHolder, position: Int) {
        // binding the view with data
        holder.bindView(mItemList!![position])
    }
}

class ItemDetailHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    /**
     * binding the item details to the views
     */
    fun bindView(itemDetails: Row) {
        //Showing title if not available showing default content
        itemView.tv_title.text = itemDetails.title ?: itemView.context.getString(R.string.no_title)

        //Showing description if not available showing default description
        itemView.tv_description.text =
            itemDetails.description ?: itemView.context.getString(R.string.no_description)

        // loading the image using glide library to avoid the UI blocking
        itemView.iv_logo.loadImage(itemDetails.imageUrl)
    }
}
