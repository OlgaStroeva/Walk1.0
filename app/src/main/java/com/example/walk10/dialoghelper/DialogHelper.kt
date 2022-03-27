package com.example.walk10.dialoghelper

import android.app.AlertDialog
import com.example.walk10.MainActivity
import com.example.walk10.R
import com.example.walk10.accounthelper.AccountHelper
import com.example.walk10.databinding.SignDialogBinding

class DialogHelper(act:MainActivity) {
    private val act = act
    private val accHelper = AccountHelper(act)

    fun createSignDialog(index:Int){
        val builder = AlertDialog.Builder(act)
        val rootDialogElement = SignDialogBinding.inflate(act.layoutInflater)
        val view = rootDialogElement.root
        builder.setView(view)
        if (index == DialogConst.SIGN_REG_STATE){
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.register)
            rootDialogElement.btSignUpln.text = act.resources.getString(R.string.register_action)
        }
        else{
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.entrance)
            rootDialogElement.btSignUpln.text = act.resources.getString(R.string.entrance_action)
        }
        val dialog = builder.create()
        rootDialogElement.btSignUpln.setOnClickListener{
            dialog.dismiss()
            if (index == DialogConst.SIGN_REG_STATE){
                accHelper.signUpWithEmail(rootDialogElement.edSignEmail.text.toString(),
                    rootDialogElement.edSignPassword.text.toString())
            }
            else{
                accHelper.signInWithEmail(rootDialogElement.edSignEmail.text.toString(),
                    rootDialogElement.edSignPassword.text.toString())
            }
        }
        dialog.show()

    }
}