package com.example.walk10

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walk10.act.EditAdsAct
import com.example.walk10.adapters.AdsRcAdapter
import com.example.walk10.data.Ad
import com.example.walk10.databinding.ActivityMainBinding
import com.example.walk10.dialoghelper.DialogConst
import com.example.walk10.dialoghelper.DialogHelper
import com.example.walk10.utils.AdapterScary
import com.example.walk10.viewmodle.FirebaseViewModle
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AdapterScary.Listener {
    private lateinit var tvAccount: TextView
    private lateinit var rootElement:ActivityMainBinding
    private val dialogHelper = DialogHelper(this)
    val mAuth = Firebase.auth
    val adapter = AdsRcAdapter(this)
    private val firebaseViewModel : FirebaseViewModle by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityMainBinding.inflate(layoutInflater)
        val view = rootElement.root
        setContentView(view)
        init()
        firebaseViewModel.loadAllAds()
        initRecyclerView()
        initViewModel()
    }

    override fun onResume() {
        super.onResume()
        rootElement.mainContent.bNavView.selectedItemId = R.id.id_home
    }

    override fun onStart() {
        super.onStart()
        uiUpdate(mAuth.currentUser)
    }
    private fun initViewModel(){

        firebaseViewModel.liveAdsData.observe(this) {
            if (it != null) {
                rootElement.mainContent.rcView.adapter = AdapterScary(it, this)
                rootElement.mainContent.rcView.layoutManager = LinearLayoutManager(this)
                rootElement.mainContent.rcView.setHasFixedSize(true)
                adapter.updateAdapter(it)
                rootElement.mainContent.tvEmpty.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
            }
            bottomMenuOnClick()
        }
    }

    private fun init(){
        setSupportActionBar(rootElement.mainContent.toolbar)
        val toggle = ActionBarDrawerToggle(this, rootElement.drawerLayout, rootElement.mainContent.toolbar, R.string.open, R.string.close)
        rootElement.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        rootElement.navView.setNavigationItemSelectedListener (this)
        tvAccount = rootElement.navView.getHeaderView(0).findViewById(R.id.accountEmail)
    }

    private fun bottomMenuOnClick() = with(rootElement){
        mainContent.bNavView.setOnNavigationItemReselectedListener{ item ->
            when(item.itemId){
                R.id.id_new_ad -> {
                        val i = Intent(this@MainActivity, EditAdsAct::class.java)
                        startActivity(i)
                }
                R.id.id_my_ads -> {
                    firebaseViewModel.loadMyAds()
                    mainContent.toolbar.title = getString(R.string.my_ad)
                }
                R.id.id_favs -> {
                    firebaseViewModel.loadMyFavs()
                }
                R.id.id_home -> {
                    firebaseViewModel.loadAllAds()
                    mainContent.toolbar.title = getString(R.string.def)
                }
            }
        }
    }

    private fun initRecyclerView(){
        rootElement.apply {
            mainContent.rcView.layoutManager = LinearLayoutManager(this@MainActivity)
            mainContent.rcView.adapter = adapter
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.id_lost_ads ->{
                Toast.makeText(this," id_lost_ads",Toast.LENGTH_LONG).show()
            }
            R.id.id_found_ads ->{
                Toast.makeText(this," id_found_ads",Toast.LENGTH_LONG).show()
            }
            R.id.id_create_ads ->{

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

    companion object{
        const val EDIT_STATE = "edit_state"
        const val ADS_DATA = "ads_data"
    }

    override fun onDeleteItem(ad: Ad) {
        firebaseViewModel.deleteItem(ad)

    }

    override fun onAdViewed(ad: Ad) {
        firebaseViewModel.adViewed(ad)
    }

    override fun onFavClicked(ad: Ad) {
        firebaseViewModel.onFavClick(ad)
    }

}