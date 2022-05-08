package com.example.walk10.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.walk10.data.Ad
import com.example.walk10.databinding.AdListItemBinding

class AdsRcAdapter : RecyclerView.Adapter<AdsRcAdapter.AdHolder> (){
    val adArray = ArrayList<Ad>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdHolder {
        val binding = AdListItemBinding.inflate(LayoutInflater.from(parent.context))
           return AdHolder(binding)
    }

    override fun onBindViewHolder(holder: AdHolder, position: Int) {
        holder.setData(adArray[position])
    }

    override fun getItemCount(): Int {
        return adArray.size
    }
    fun updateAdapter(newList: List<Ad>){
        adArray.clear()
        adArray.addAll(newList)
        notifyDataSetChanged()
    }

    class AdHolder(val binding: AdListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(ad: Ad){
            binding.apply {
                //название объявления
                tvDescription.text=ad.description
            }
        }
    }
}