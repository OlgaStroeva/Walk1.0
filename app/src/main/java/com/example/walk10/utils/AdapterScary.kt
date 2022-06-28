package com.example.walk10.utils

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.walk10.MainActivity
import com.example.walk10.R
import com.example.walk10.act.EditAdsAct
import com.example.walk10.data.Ad
import com.squareup.picasso.Picasso

class AdapterScary(private var adList: List<Ad>, val act : MainActivity): RecyclerView.Adapter<AdapterScary.MyKotlinVH>() {

    interface Listener{
        fun onDeleteItem(ad: Ad)
        fun onAdViewed(ad: Ad)
        fun onFavClicked(ad : Ad)
    }

    class MyKotlinVH(view: View, val act : MainActivity) : RecyclerView.ViewHolder(view) {
            val mTextViewHeader: TextView = view.findViewById(R.id.tvTitle)
            val mTextViewFooter: TextView = view.findViewById(R.id.Date)
            val mTextViewDescription: TextView = view.findViewById(R.id.tvDescription)
        val FawCount : TextView = view.findViewById(R.id.tvFaw)
        val ViewCount : TextView = view.findViewById(R.id.tvViewsCounter)

            val editPanel: LinearLayout = view.findViewById(R.id.editPanel)
            val ibFaw : ImageButton = view.findViewById(R.id.ibFaw)
            val ibDeleteAd : ImageButton = view.findViewById(R.id.ibDeleteAd)
            val ibEditAd : ImageButton = view.findViewById(R.id.ibEditAd)
        val mainImage : ImageView = view.findViewById(R.id.mainImage)

            private fun onClickEdit(ad: Ad): View.OnClickListener {
                return View.OnClickListener {
                    val editIntent = Intent(act, EditAdsAct::class.java).apply {
                        putExtra(MainActivity.EDIT_STATE, true)
                        putExtra(MainActivity.ADS_DATA, ad)
                    }
                    act.startActivity(editIntent)
                }
            }

            fun setData(ad: Ad) {
                //название объявления
                Picasso.get().load(ad.mainImage).into(mainImage)
                if (ad.isFav) {
                    ibFaw.setImageResource(R.drawable.ic_faw_pressed)
                } else {
                    ibFaw.setImageResource(R.drawable.ic_faw_normal)
                }
                showEditPanel(isOwner(ad))
                itemView.setOnClickListener {
                    act.onAdViewed(ad)
                }
                ibEditAd.setOnClickListener(onClickEdit(ad))
                ibDeleteAd.setOnClickListener {
                    act.onDeleteItem(ad)
                }
                ibFaw.setOnClickListener {
                    act.onFavClicked(ad)
                    //ViewCount.text = ad.viewCounter
                }
            }

            private fun isOwner(ad: Ad): Boolean {
                return (ad.uid == act.mAuth.uid)
            }

            private fun showEditPanel(isOwner: Boolean) {
                if (isOwner) {
                    editPanel.visibility = View.VISIBLE
                } else {
                    editPanel.visibility = View.GONE
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyKotlinVH {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.ad_list_item,parent,false)
            return MyKotlinVH(view, act)
        }

        override fun onBindViewHolder(holder: MyKotlinVH, position: Int) {
            val ad = adList[position]
            holder.mTextViewHeader.text = ad.category
            holder.mTextViewFooter.text = ad.date
            holder.mTextViewDescription.text = ad.description
            holder.ViewCount.text = ad.viewCounter
            holder.FawCount.text = ad.favCounter
            holder.setData(ad)

        }

    fun updateAdapter(newList: List<Ad>){
        adList = newList
        notifyDataSetChanged()
        /*val mutable = adList.toMutableList()
        val diffResult= DiffUtil.calculateDiff(DiffUtilHelper(adList, newList))
        diffResult.dispatchUpdatesTo(this)
        mutable.clear()
        mutable.addAll(newList)*/
    }

        override fun getItemCount(): Int {
            return  adList.size
        }


    }