package com.example.walk10.frag

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.walk10.R
import com.example.walk10.act.EditAdsAct
import com.example.walk10.databinding.SelectImageFragItemBinding
import com.example.walk10.utils.AdapterCallback
import com.example.walk10.utils.ImagePicker
import com.example.walk10.utils.ItemTTouchMoveCallback
import com.example.walk10.utils.imageManager

class SelectImageRvAdapter(val adapterCallback: AdapterCallback) : RecyclerView.Adapter<SelectImageRvAdapter.ImageHolder>(), ItemTTouchMoveCallback.ItemTouchAdapter{
    val mainArray = ArrayList<Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val viewBinding = SelectImageFragItemBinding.inflate(LayoutInflater.from(parent.context), parent, true)
        return ImageHolder(viewBinding, parent.context, this)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position])
    }

    override fun getItemCount(): Int {
       return mainArray.size
    }

    override fun onMove(startPos: Int, targetPos: Int) {
        val targetItem = mainArray[targetPos]
        mainArray[targetPos] = mainArray[startPos]
        mainArray[startPos] = targetItem
        notifyItemMoved(startPos, targetPos)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClear() {
        notifyDataSetChanged()
    }

    class ImageHolder(private val viewBinding : SelectImageFragItemBinding,
                      val context : Context, val adapter : SelectImageRvAdapter)
        : RecyclerView.ViewHolder(viewBinding.root){

        fun setData(bitmap: Bitmap){
            viewBinding.imEditImage.setOnClickListener {
                ImagePicker.getSingleImage(context as EditAdsAct)
                context.editImagePos = adapterPosition
            }

            viewBinding.imDelete.setOnClickListener {
                adapter.mainArray.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
                for(n in 0 until adapter.mainArray.size) adapter.notifyItemChanged(n)
                adapter.adapterCallback.onItemDelete()
            }

            viewBinding.tvTitle.text = context.resources.getStringArray(R.array.title_image_array)[adapterPosition]
            imageManager.chooseScaleType(viewBinding.imageView, bitmap)
            viewBinding.imageView.setImageBitmap(bitmap)
        }
    }
@SuppressLint("NotifyDataSetChanged")
fun updateAdapter(newList: ArrayList<Bitmap>, needClear : Boolean){
    if(needClear) mainArray.clear()
    mainArray.addAll(newList)
    notifyDataSetChanged()
}

}