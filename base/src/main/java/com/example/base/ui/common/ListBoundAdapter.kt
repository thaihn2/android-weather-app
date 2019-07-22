package com.example.base.ui.common

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import java.util.concurrent.Executors

abstract class ListBoundAdapter<T>(diffCallback: DiffUtil.ItemCallback<T>) : ListAdapter<T, DataBoundViewHolder>(
        AsyncDifferConfig.Builder<T>(diffCallback)
                .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
                .build()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder {
        return DataBoundViewHolder(inflateView(parent, viewType))
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder, position: Int) {
        bind(holder.itemView, getItem(position))
    }

    protected abstract fun inflateView(parent: ViewGroup, viewType: Int? = 0): View

    protected abstract fun bind(rootView: View, item: T)
}
