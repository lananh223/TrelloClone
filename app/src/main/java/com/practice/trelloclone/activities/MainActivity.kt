package com.practice.trelloclone.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.practice.trelloclone.R
import com.practice.trelloclone.databinding.AppBarMainBinding
import com.practice.trelloclone.firebase.FirestoreClass

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: AppBarMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = AppBarMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        drawerLayout.findViewById<DrawerLayout>(R.id.drawer_layout)
        navigationView.findViewById<NavigationView>(R.id.nav_view)

        val currentUserId = FirestoreClass().getCurrentUserId()
        if (currentUserId.isEmpty()) {
            startActivity(Intent(this, IntroActivity::class.java))
        }

        setUpActionBar()

        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarMainActivity)
        binding.toolbarMainActivity.setNavigationIcon(R.drawable.ic_action_navigation_menu)

        binding.toolbarMainActivity.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
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
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}