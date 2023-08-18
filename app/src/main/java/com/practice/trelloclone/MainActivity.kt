package com.practice.trelloclone

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.practice.trelloclone.activities.IntroActivity
import com.practice.trelloclone.firebase.FirestoreClass

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        val currentUserId = FirestoreClass().getCurrentUserId()
        if (currentUserId.isEmpty()) {
            startActivity(Intent(this, IntroActivity::class.java))
        }
    }
}