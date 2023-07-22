package com.karan.doubthub.domain

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface RepositoryInterface {

    suspend fun signInUserWithGoogle(account: GoogleSignInAccount): Pair<Boolean, String>
    suspend fun signUpUser( userName:String,email:String,password:String):Pair<Boolean,String>
}