package com.cesecsh.ics.ui.activity.entrance.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cesecsh.baselib.utils.ScreenFitUtils
import com.cesecsh.baselib.widget.callBack.OnItemClickListener
import com.cesecsh.ics.R
import com.cesecsh.ics.data.domain.Ble
import com.cesecsh.ics.data.domain.EntranceControl

/**
 * 作者：RockQ on 2018/7/17
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
class EntranceControlSelectedAdapter(val context: Context) : RecyclerView.Adapter<EntranceControlSelectedAdapter.ViewHolder>() {
    private var mEntranceControls: ArrayList<Ble>? = null
    fun setEntranceControls(entranceControls: ArrayList<Ble>) {
        this.mEntranceControls = entranceControls
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mEntranceControls != null && mEntranceControls!!.size > 0) {
            val entranceControl = mEntranceControls!!.get(position)
            holder.mTvName.text = entranceControl.bleName
            holder.mImgSelected.isSelected = entranceControl.isSelected
            holder.itemView.setOnClickListener { if (mItemClickListener != null) mItemClickListener!!.onClick(holder.itemView, position) }
        }
    }

    private var mItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_entrance_control_selected, parent, false)
        ScreenFitUtils.auto(view)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (mEntranceControls == null) 0 else mEntranceControls!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mImgSelected: ImageView
        val mTvName: TextView

        init {
            mImgSelected = itemView.findViewById(R.id.mImgSelected)
            mTvName = itemView.findViewById(R.id.mTvName)

        }
    }
}