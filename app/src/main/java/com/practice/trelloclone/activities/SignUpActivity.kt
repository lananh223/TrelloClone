package com.practice.trelloclone.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.practice.trelloclone.R
import com.practice.trelloclone.databinding.ActivitySignUpBinding
import com.practice.trelloclone.firebase.FirestoreClass

class SignUpActivity : BaseActivity() {

    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // This is used to hide the status bar and make the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setUpActionBar()

        binding.btnSignUp.setOnClickListener {
            registerUser()
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarSignUpActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_arrow_back_24)
        }
        binding.toolbarSignUpActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun registerUser() {
        val name: String = binding.etName.text.toString().trim { it <= ' ' }
        val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
        val password: String = binding.etPassword.text.toString().trim { it <= ' ' }

        if (validateForm(name, email, password)) {
            showProgressDialog()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Firebase register user
                        val firebaseUser: FirebaseUser = task.result.user!!
                        val registerEmail = firebaseUser.email!!
                        //create new user
                        val user = com.practice.trelloclone.module.User(
                            firebaseUser.uid,
                            name,
                            registerEmail
                        )
                        FirestoreClass().registerUser(this, user)

                    } else {
                        Toast.makeText(
                            this@SignUpActivity,
                            task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        }
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter the name")
                false
            }

            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter the email")
                false
            }

            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter the password")
                false
            }

            else -> true
        }
    }

    fun userRegisterSuccess() {
        Toast.makeText(
            this,
            "You have successfully registered.",
            Toast.LENGTH_SHORT
        ).show()

        hideProgressDialog()

        /**
         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
         * and send him to Intro Screen for Sign-In
         */

        FirebaseAuth.getInstance().signOut()
        // Finish the Sign-Up Screen
        finish()
    }
}