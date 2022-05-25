package com.example.walk10.frag

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.car.ui.toolbar.MenuItem
import com.example.walk10.R
import com.example.walk10.act.EditAdsAct
import com.example.walk10.databinding.ListImageFragBinding
import com.example.walk10.dialoghelper.progressDialog
import com.example.walk10.utils.AdapterCallback
import com.example.walk10.utils.ImagePicker
import com.example.walk10.utils.ItemTTouchMoveCallback
import com.example.walk10.utils.imageManager
import com.example.walk10.utils.imageManager.imageResize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ImageListFrag(private val fragCloseInterface : FragmentCloseInterface,
                    private val newList : ArrayList<Uri>?)
                    : Fragment(), AdapterCallback {
    private val adapter = SelectImageRvAdapter(this)
    private val dragCallback = ItemTTouchMoveCallback(adapter)
    private val touchHelper = ItemTouchHelper(dragCallback)
    private var job: Job? = null
    private var addImageItem: MenuItem? = null
    private lateinit var binding: ListImageFragBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListImageFragBinding.inflate(layoutInflater)
        //adView = binding.adView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        binding.apply {
        touchHelper.attachToRecyclerView(rcViewSelectImage)
        rcViewSelectImage.layoutManager = LinearLayoutManager(activity)
        rcViewSelectImage.adapter = adapter
        }
    }

    override fun onItemDelete(){
        addImageItem?.isVisible = true
    }

    fun updateAdapterFromEdit(bitmapList: ArrayList<Bitmap>){
        adapter.updateAdapter(bitmapList, true)
    }

    override fun onDetach() {
        super.onDetach()
        fragCloseInterface.onFragClose(adapter.mainArray)
        job?.cancel()
    }

    fun resizeSelectedImages(newList: ArrayList<Uri>?, needClear: Boolean, activity : Activity){
        job = CoroutineScope(Dispatchers.Main).launch {
            val dialog = progressDialog.createProgressDialog(activity)
            val bitmapList = newList?.let { imageResize(it, activity) }
            dialog.dismiss()
            if (bitmapList != null) {
                adapter.updateAdapter(bitmapList, needClear)
            }
            if(adapter.mainArray.size > 2) addImageItem?.isVisible = false
        }
    }

    private fun setUpToolbar(){
        binding.apply{
        tb.inflateMenu(R.menu.menu_choose_image)
        val deleteItem = tb.menu.findItem(R.id.id_delete_image)
        val addImageItem = tb.menu.findItem(R.id.id_add_image)
            if(adapter.mainArray.size > 2) addImageItem?.isVisible = false

        tb.setNavigationOnClickListener{
            activity?.supportFragmentManager?.beginTransaction()?.remove(this@ImageListFrag)?.commit()
        }

        deleteItem.setOnMenuItemClickListener {
            adapter.updateAdapter(ArrayList(), true)
            //adImageItem?.isVisible = true
            true
        }
        addImageItem?.setOnMenuItemClickListener {
            val imageCount = ImagePicker.MAX_IMAGE_COUNT - adapter.mainArray.size
            ImagePicker.addImages(activity as EditAdsAct, imageCount)
            true
        }
    }
    }
    fun updateAdapter(newList : ArrayList<Uri>,  activity: Activity){
        resizeSelectedImages(newList, false, activity)
    }
    fun setSingleImage(uri : Uri, pos : Int){
        val pBar = binding.rcViewSelectImage[pos].findViewById<ProgressBar>(R.id.pBar)
        job = CoroutineScope(Dispatchers.Main).launch {
            pBar.visibility = View.VISIBLE
            val bitmapList = imageManager.imageResize(arrayListOf(uri), activity as Activity)
            pBar.visibility = View.GONE
            adapter.mainArray[pos] = bitmapList[0]
            adapter.notifyItemChanged(pos)
        }
    }

}
