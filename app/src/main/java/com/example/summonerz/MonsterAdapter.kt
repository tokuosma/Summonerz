package com.example.summonerz

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.list_view_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class MonsterAdapter(context: Context, private val list: List<Monster>) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val row = inflater.inflate(R.layout.list_view_item, parent, false)
        row.monsterName.text = list[position].name

        val sdf = SimpleDateFormat("HH:mm dd.MM.yyyy")
        sdf.timeZone = TimeZone.getDefault()
        val time = list[position].time_of_scan
        val readableTime = sdf.format(time)
        row.monsterTrigger.text = readableTime

        return row
    }

    override fun getItem(position: Int): Any {
        return list[position]

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()

    }

    override fun getCount(): Int {
        return list.size

    }

}