package com.karan.doubthub.presentation.viewmodel

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.karan.doubthub.domain.Repository

class LoginSignUpVM:ViewModel() {
    private val repoRef = Repository

    suspend fun signInUserWithGoogle(account: GoogleSignInAccount):Pair<Boolean,String>{
        return repoRef.signInUserWithGoogle(account)
    }
    suspend fun signUpUser( userName:String,email:String,password:String):Pair<Boolean,String>{
        return repoRef.signUpUser(userName, email, password)
    }
}