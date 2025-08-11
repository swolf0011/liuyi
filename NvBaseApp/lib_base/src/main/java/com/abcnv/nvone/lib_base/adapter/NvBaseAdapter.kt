package com.abcnv.nvone.lib_base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class NvBaseAdapter<T, E : RecyclerView.ViewHolder>(
    var list: MutableList<T>,
    val layout: Int,
    val itemCallback: DiffUtil.ItemCallback<T>,
    val callbackFactory: (binding: View) -> E,
    val callbackBind: (T, E) -> Unit
) :
    ListAdapter<T, E>(itemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): E {
        val li = LayoutInflater.from(parent.context)
        return callbackFactory(li.inflate(layout, parent, false))
    }

    override fun onBindViewHolder(holder: E, position: Int) {
        val entity = getItem(position)
        callbackBind(entity, holder)
    }

    override fun getItem(position: Int): T {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class EntityViewHolder(val binding: View) : RecyclerView.ViewHolder(binding) {
}

class EntityDiffCallback : DiffUtil.ItemCallback<NvAdapterEntity>() {

    override fun areItemsTheSame(oldItem: NvAdapterEntity, newItem: NvAdapterEntity): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: NvAdapterEntity, newItem: NvAdapterEntity): Boolean {
        return oldItem.equals(newItem)
    }
}
