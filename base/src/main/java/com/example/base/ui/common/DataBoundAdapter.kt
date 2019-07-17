package com.example.base.ui.common

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class DataBoundAdapter<T> : RecyclerView.Adapter<DataBoundViewHolder>() {

    protected var items: MutableList<T> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder {
        return DataBoundViewHolder(inflateView(parent))
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder, position: Int) {
        bind(holder.itemView, items[position])
    }

    protected abstract fun inflateView(parent: ViewGroup): View

    protected abstract fun bind(rootView: View, item: T)

    override fun getItemCount(): Int {
        return items.size
    }
}
