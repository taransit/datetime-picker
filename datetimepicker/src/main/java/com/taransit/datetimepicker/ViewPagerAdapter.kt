package com.taransit.datetimepicker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter(context: Context) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {

    val TABS = context.resources.getStringArray(R.array.tabs)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        return LayoutInflater
            .from(parent.context).inflate(R.layout.item_view_pager, parent, false)
            .let(::PagerViewHolder)
    }

    override fun getItemCount(): Int = TABS.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {}

    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}