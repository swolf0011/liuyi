package com.abcnv.nvone.lib_base.adapter

import android.util.SparseArray
import android.view.View
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
class NvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mViews = SparseArray<View>()

    /**
     * 根据id获取控件
     * @param viewId 控件id
     * @return
     */
    fun <T : View> getView(viewId: Int): T {
        var view: View = mViews.get(viewId)
        if (null == view) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T
    }


}