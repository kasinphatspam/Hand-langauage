package com.gura.team.audisign.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gura.team.audisign.R
import com.gura.team.audisign.model.Category

class LabelAdapter(private val mList: ArrayList<String>, private val context: Context): BaseAdapter() {

    private lateinit var layoutInflater: LayoutInflater

    override fun getCount(): Int {
        return mList.size
    }

    override fun getItem(position: Int): Any {
        return mList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val holder: ViewHolder

        val item = mList[position]

        if(view == null) {
            holder = ViewHolder()
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.label_item, null)
            holder.titleTextView = view!!.findViewById(R.id.itemNameTextView)
            holder.descriptionTextView = view.findViewById(R.id.descriptionTextView)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        holder.titleTextView!!.text = item
        holder.descriptionTextView!!.text = ""

        return view
    }

    class ViewHolder {

        var titleTextView: TextView? = null
        var descriptionTextView: TextView? = null

    }
}