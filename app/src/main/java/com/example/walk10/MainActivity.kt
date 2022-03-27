package com.example.walk10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.walk10.databinding.ActivityMainBinding
import com.example.walk10.dialoghelper.DialogConst
import com.example.walk10.dialoghelper.DialogHelper
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var tvAccount: TextView
    private lateinit var rootElement:ActivityMainBinding
    private val dialogHelper = DialogHelper(this)
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityMainBinding.inflate(layoutInflater)
        val view = rootElement.root
        setContentView(view)
        init()
    }

    override fun onStart() {
        super.onStart()
        uiUpdate(mAuth.currentUser)
    }

    private fun init(){

        var toggle = ActionBarDrawerToggle(this, rootElement.drawerLayout, rootElement.mainContent.toolbar, R.string.open, R.string.close)
        rootElement.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        rootElement.navView.setNavigationItemSelectedListener (this)
        tvAccount = rootElement.navView.getHeaderView(0).findViewById(R.id.accountEmail)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.id_lost_ads ->{
                Toast.makeText(this,"Presed id_lost_ads",Toast.LENGTH_LONG).show()
            }
            R.id.id_found_ads ->{
                Toast.makeText(this,"Presed id_found_ads",Toast.LENGTH_LONG).show()
            }
            R.id.id_create_ads ->{
                Toast.makeText(this,"Presed id_create_ads",Toast.LENGTH_LONG).show()
            }
            R.id.id_account_ads ->{
                Toast.makeText(this,"id_account_ads",Toast.LENGTH_LONG).show()
            }
            R.id.id_register_ads ->{
                dialogHelper.createSignDialog(DialogConst.SIGN_REG_STATE)
            }
            R.id.id_entrance_ads ->{
                dialogHelper.createSignDialog(DialogConst.SIGN_IN_STATE)
            }
            R.id.id_exit_ads ->{
                uiUpdate(null)
                mAuth.signOut()
            }

        }
        rootElement.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun uiUpdate(user: FirebaseUser?){
        tvAccount.text = if(user == null){
            resources.getString((R.string.not_reg))
        } else {
            user.email
        }
    }

}