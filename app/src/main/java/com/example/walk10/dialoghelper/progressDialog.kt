package com.example.walk10.dialoghelper

import android.app.Activity
import android.app.AlertDialog
import com.example.walk10.databinding.ProgressDialogLayoutBinding

object progressDialog {
    fun createProgressDialog(act: Activity) : AlertDialog{
        val builder = AlertDialog.Builder(act)
        val rootDialogElement = ProgressDialogLayoutBinding.inflate(act.layoutInflater)
        val view = rootDialogElement.root
        val dialog = builder.create()
        builder.setView(view)
        dialog.setCancelable(false)
        dialog.show()
        return dialog
    }
}