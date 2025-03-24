package com.jdw.diet_memo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ListViewAdapter(
    val list : MutableList<DateModel>
) : BaseAdapter(){
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any? {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        var conv = convertView
        if (convertView == null) {
            conv = LayoutInflater.from(parent?.context).inflate(R.layout.listview_item, parent, false)
        }

        val text = conv.findViewById<TextView>(R.id.listText)
        val date = conv.findViewById<TextView>(R.id.listDate)

        text.text = list[position].memo
        date.text = list[position].date

        return conv!!
    }
}