package com.practice.trelloclone.firebase

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.practice.trelloclone.activities.MainActivity
import com.practice.trelloclone.activities.SignInActivity
import com.practice.trelloclone.activities.SignUpActivity
import com.practice.trelloclone.module.User
import com.practice.trelloclone.utils.Constants

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    //add database for user to get extra data: photo, phone number
    fun registerUser(activity: SignUpActivity, userInfo: User) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisterSuccess()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(
                    "Register User",
                    "Error writing document"
                )
            }
    }

    fun signInUser(activity: Activity) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggingInUser = document.toObject(User::class.java)

                if (loggingInUser != null) {
                    when (activity) {
                        is SignInActivity -> activity.signInSuccess(loggingInUser)
                        is MainActivity -> activity.updateNavigationUserDetails(loggingInUser)
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is SignInActivity -> activity.hideProgressDialog()
                    is MainActivity -> activity.hideProgressDialog()
                }
                Log.e(
                    "Sign in User",
                    "Error writing document"
                )
            }
    }

    fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""

        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }
}