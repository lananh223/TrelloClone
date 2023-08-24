package com.practice.trelloclone.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.practice.trelloclone.R
import com.practice.trelloclone.firebase.FirestoreClass
import com.practice.trelloclone.module.User

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mUserName: String

    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private var toolbarMainActivity: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        drawerLayout?.findViewById<DrawerLayout>(R.id.drawer_layout)
        navigationView?.findViewById<NavigationView>(R.id.nav_view)
        toolbarMainActivity?.findViewById<Toolbar>(R.id.toolbar_main_activity)

        val currentUserId = FirestoreClass().getCurrentUserId()
        if (currentUserId.isEmpty()) {
            startActivity(Intent(this, IntroActivity::class.java))
        } else {
            FirestoreClass().signInUser(this)
        }

        setUpActionBar()

        navigationView?.setNavigationItemSelectedListener(this)
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbarMainActivity)
        toolbarMainActivity?.setNavigationIcon(R.drawable.ic_action_navigation_menu)

        toolbarMainActivity?.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer() {
        if (drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout?.openDrawer(GravityCompat.START)
        }
    }

    fun updateNavigationUserDetails(user: User) {
        hideProgressDialog()

        mUserName = user.name

        val headerView = navigationView?.getHeaderView(0)
        val navUserImage = headerView?.findViewById<ImageView>(R.id.iv_user_image)

        if (navUserImage != null) {
            Glide
                .with(this)
                .load(user.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(navUserImage)
        }

        val navUserName = headerView?.findViewById<TextView>(R.id.tv_username)
        navUserName?.text = user.name

//        if(readBoardsList) {
//            showProgressDialog()
//            FirestoreClass.getBoardsList(this, )
//        }
    }

    override fun onBackPressed() {
        if (drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_my_profile -> {
                //Navigate to ProfileActivity
            }
            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                )
                startActivity(intent)
                finish()
            }
        }
        drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }
}