package com.example.walk10.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.walk10.R
import com.example.walk10.data.Ad

class AdapterScary (private val adList:List<Ad>): RecyclerView.Adapter<AdapterScary.MyKotlinVH>() {

        class MyKotlinVH(view: View) : RecyclerView.ViewHolder(view) {
            val mTextViewHeader: TextView = view.findViewById(R.id.tvTitle)
            val mTextViewFooter:TextView = view.findViewById(R.id.Date)
            val mTextViewDescription:TextView = view.findViewById(R.id.tvDescription)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyKotlinVH {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.ad_list_item,parent,false)
            return MyKotlinVH(view)
        }

        override fun onBindViewHolder(holder: MyKotlinVH, position: Int) {
            val currentItem = adList[position]
            holder.mTextViewHeader.text = currentItem.category
            holder.mTextViewFooter.text = currentItem.date
            holder.mTextViewDescription.text = currentItem.description
        }

        override fun getItemCount(): Int {
            return  adList.size
        }
    }