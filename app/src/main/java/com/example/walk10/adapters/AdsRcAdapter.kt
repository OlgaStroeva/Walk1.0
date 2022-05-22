package com.example.walk10.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.walk10.MainActivity
import com.example.walk10.act.EditAdsAct
import com.example.walk10.data.Ad
import com.example.walk10.databinding.AdListItemBinding
import com.google.firebase.auth.FirebaseAuth

class AdsRcAdapter(val act : MainActivity) : RecyclerView.Adapter<AdsRcAdapter.AdHolder> (){
    val adArray = ArrayList<Ad>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdHolder {
        val binding = AdListItemBinding.inflate(LayoutInflater.from(parent.context))
           return AdHolder(binding, act)
    }

    override fun onBindViewHolder(holder: AdHolder, position: Int) {
        holder.setData(adArray[position])
    }

    override fun getItemCount(): Int {
        return adArray.size
    }
    fun updateAdapter(newList: List<Ad>){
        val diffResult=DiffUtil.calculateDiff(DiffUtilHelper(adArray, newList))
        diffResult.dispatchUpdatesTo(this)
        adArray.clear()
        adArray.addAll(newList)

    }

    class AdHolder(val binding: AdListItemBinding, val act: MainActivity) : RecyclerView.ViewHolder(binding.root) {
        fun setData(ad: Ad) =with(binding) {
                //название объявления
                tvDescription.text=ad.category
                showEditPanel(isOwner(ad))
                ibEditAd.setOnClickListener()
                ibDeleteAd.setOnClickListener{}
                   act.onDeleteItem(ad)
        }
        private fun onClickEdit(ad: Ad): View.OnClickListener{
            return View.OnClickListener {
                val editIntent = Intent(act,EditAdsAct::class.java).apply {
                    putExtra(MainActivity.EDIT_STATE, true)
                    putExtra(MainActivity.ADS_DATA,ad)
                }
                act.startActivity(editIntent)
            }}

        private fun isOwner( ad: Ad) : Boolean{
            return (ad.uid == act.mAuth.uid)
        }

        private fun showEditPanel(isOwner:Boolean){
            if(isOwner){
                binding.editPanel.visibility = View.VISIBLE
            } else {
                binding.editPanel.visibility = View.GONE
            }
        }

    }
    interface DeleteItemListener{
        fun onDeleteItem(ad: Ad)

    }
}