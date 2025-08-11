package com.abcnv.nvone.lib_base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property:
 *
 * @Author liuyi
 */
class NvAdapterOne<T : NvAdapterEntity>(
    var mDatalist: MutableList<T>,
    var mLayoutId: Int,
    val itemClickListener: (viewHolder: NvViewHolder, itemData: T) -> Unit
) : RecyclerView.Adapter<NvViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(mLayoutId, parent, false)
        return NvViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return mDatalist[position].layoutType
    }

    override fun getItemCount(): Int {
        return mDatalist.size
    }

    override fun onBindViewHolder(viewHolder: NvViewHolder, position: Int) {
        itemClickListener.invoke(viewHolder, mDatalist[position])
    }

    /**
    val dataList = mutableListOf<NvAdapterEntity>()
    val layoutId:Int = 0
    val adapter = NvAdapterOne<NvAdapterEntity>(dataList, layoutId) { viewHolder, itemData ->
        val tv_name = viewHolder.getView<TextView>(R.id.tv_name)
        tv_name.text = "No.${itemData.index}"
    }
     */
}
