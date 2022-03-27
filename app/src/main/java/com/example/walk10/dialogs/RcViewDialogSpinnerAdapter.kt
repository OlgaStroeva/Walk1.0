package com.example.walk10.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.walk10.R
import com.example.walk10.act.EditAdsAct
import androidx.recyclerview.widget.RecyclerView.ViewHolder as ViewHolder1

class RcViewDialogSpinnerAdapter(var context: Context, var dialog: AlertDialog) : RecyclerView.Adapter<RcViewDialogSpinnerAdapter.spVewHolder>() {

    private val mainList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): spVewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sp_list_item, parent, false)
        return spVewHolder(view, context, dialog)

    }
    override fun onBindViewHolder(holder: spVewHolder, position: Int) {
        holder.setData(mainList[position])
    }



    override fun getItemCount(): Int {
        return mainList.size
    }

    class spVewHolder(itemView: View, var context: Context,var dialog: AlertDialog) : ViewHolder1(itemView), View.OnClickListener {
        private var itemText = ""
        val tvSpItem = itemView.findViewById<TextView>(R.id.tvspItem)
        fun setData(text : String){
            val tvSpItem = itemView.findViewById<TextView>(R.id.tvspItem)
            tvSpItem.text = text
            itemText = text
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            (context as EditAdsAct).rootElement.tvCity.text = itemText
            dialog.dismiss()
        }
    }
    fun updateAdapter(list : ArrayList<String>){
        mainList.clear()
        mainList.addAll(list)
        notifyDataSetChanged()
    }
}