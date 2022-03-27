package com.example.walk10.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.walk10.R
import androidx.recyclerview.widget.RecyclerView.ViewHolder as ViewHolder1

class RcViewDialogSpinner : RecyclerView.Adapter<RcViewDialogSpinner.spVewHolder>() {

    private val mainList = ArrayList<String>()

    override fun onBindViewHolder(holder: spVewHolder, position: Int) {
        holder.setData(mainList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): spVewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sp_list_item, parent, false)
        return spVewHolder(view)

    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    class spVewHolder(itemView: View) : ViewHolder1(itemView) {
        val tvSpItem = itemView.findViewById<TextView>(R.id.tvspItem)
        fun setData(text : String){
            tvSpItem.text = text

        }
    }
    fun updateAdapter(list : ArrayList<String>){
        mainList.clear()
        mainList.addAll(list)
        notifyDataSetChanged()
    }
}