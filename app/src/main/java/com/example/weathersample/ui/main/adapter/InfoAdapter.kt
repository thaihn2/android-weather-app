package com.example.weathersample.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.base.entity.Info
import com.example.base.ui.common.DataBoundAdapter
import com.example.weathersample.R
import kotlinx.android.synthetic.main.item_info.view.*

class InfoAdapter : DataBoundAdapter<Info>() {

    override fun inflateView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.item_info, parent, false)
    }

    override fun bind(rootView: View, item: Info) {
        rootView.text_title.text = item.title
        rootView.text_content.text = item.content
    }

    fun updateAll(newList: List<Info>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}
