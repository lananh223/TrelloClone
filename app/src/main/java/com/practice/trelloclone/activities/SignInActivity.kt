package com.practice.trelloclone.activities

import android.os.Bundle
import com.practice.trelloclone.databinding.ActivitySignInBinding

class SignInActivity : BaseActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}