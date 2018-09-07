package com.edu.hust.henry.cashcal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.edu.hust.henry.cashcal.module.Info

/**
 * Project: CashCal
 * Created by DuongHV.
 * Copyright (c) 2018 - HUST.
 */

class MyCustomAdapter(var context: Context, var infoList: ArrayList<Info>) : BaseAdapter(){

    class ViewHolder(var row: View){
        var tvDow: TextView
        var tvCost: TextView

        init {
            tvDow = row.findViewById(R.id.txt_date_of_week) as TextView
            tvCost = row.findViewById(R.id.txt_cost_per_day) as TextView
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View?
        var viewHolder: ViewHolder

        if(convertView == null){
            var layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.info_line, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else{
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        var info: Info = getItem(position) as Info
        viewHolder.tvDow.text = info.dayofweek
        viewHolder.tvCost.text = info.cost.toString()

        return view as View
    }

    override fun getItem(position: Int): Any {
        return infoList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return infoList.size
    }

}