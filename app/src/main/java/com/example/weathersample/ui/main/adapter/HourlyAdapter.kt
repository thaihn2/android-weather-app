package com.example.weathersample.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.base.entity.DataHourly
import com.example.base.ui.common.DataBoundAdapter
import com.example.weathersample.R
import com.example.weathersample.util.Utils
import kotlinx.android.synthetic.main.item_hourly.view.*

class HourlyAdapter : DataBoundAdapter<DataHourly>() {

    override fun inflateView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.item_hourly, parent, false)
    }

    override fun bind(rootView: View, item: DataHourly) {
        rootView.text_time.text = Utils.convertTimeToHour(item.time)

        Glide.with(rootView.context).load(
                Utils.getImage(Utils.changeIconToName(item.icon), rootView.context)).into(
                rootView.image_icon)

        rootView.text_temp.text = Utils.changeTempFToC(item.temperature).toString() + Utils.makeTemp()
    }

    fun updateAll(newItem: List<DataHourly>) {
        items.clear()
        items.addAll(newItem)
        notifyDataSetChanged()
    }
}
