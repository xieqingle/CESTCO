package com.cesecsh.ics.ui.activity.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cesecsh.baselib.widget.callBack.OnItemClickListener
import com.cesecsh.ics.R
import com.cesecsh.ics.data.domain.Ble

/**
 * 作者：RockQ on 2018/8/20
 * 邮箱：qingle6616@sina.com
 *
 * msg：首页门禁菜单列表 adapter
 */
class DoorAdapter(var context: Context) : RecyclerView.Adapter<DoorAdapter.ViewHolder>() {
    private var mDoors: List<Ble>? = null
    fun setDoors(doors: List<Ble>) {
        mDoors = doors
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_door_list, parent, false))
    }

    override fun getItemCount(): Int {
        return if (mDoors == null) 0 else mDoors!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            if (mOnItemClickListener != null) {
                mOnItemClickListener!!.onClick(holder.itemView, position)
            }
        }
        holder.mTextView.text = mDoors!![position].bleName
    }

    private var mOnItemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var mTextView: TextView = itemView!!.findViewById(R.id.door_name)
    }


}

