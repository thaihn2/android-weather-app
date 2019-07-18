package com.example.weathersample.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.base.entity.DataDaily
import com.example.base.ui.common.DataBoundAdapter
import com.example.weathersample.R
import com.example.weathersample.util.Utils
import kotlinx.android.synthetic.main.item_weekly.view.*

class WeeklyAdapter : DataBoundAdapter<DataDaily>() {
    override fun inflateView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.item_weekly, parent, false)
    }

    override fun bind(rootView: View, item: DataDaily) {
        rootView.text_day.text = Utils.convertTimeToDay(item.time)

        Glide.with(rootView.context).load(
                Utils.getImage(Utils.changeIconToName(item.icon), rootView.context)).into(
                rootView.image_icon)

        rootView.text_temp_max.text = Utils.changeTempFToC(item.temperatureHigh).toString() + Utils.makeTemp()
        rootView.text_temp_min.text = Utils.changeTempFToC(item.temperatureLow).toString() + Utils.makeTemp()
    }

    fun updateAll(newList: List<DataDaily>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}
