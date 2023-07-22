package com.karan.doubthub.domain

import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.tasks.await

object FirebaseDataSource {

    private val firebaseAuth =FirebaseAuth.getInstance()




    suspend fun signInUserWithGoogle(account: GoogleSignInAccount): Pair<Boolean, String> {
        val credentials = GoogleAuthProvider.getCredential(account.idToken ,null)
        return try {
            firebaseAuth.signInWithCredential(credentials).await()
            val result = Pair(true,"Sign In successful...")
            result
        }catch (e:Exception){
            val result = Pair(false,e.message.toString())
            result
        }
    }



    suspend fun signUpUser(userName:String, email:String, password:String):Pair<Boolean,String>{
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email,password).await()
            updateUserName(userName)
            val result = Pair(true,"Sign In successful...")
            result
        }
        catch (e:java.lang.Exception){
            val result = Pair(false,e.message.toString())
            result
        }
    }

    private fun updateUserName(userName: String) {
        val profileUpdate = userProfileChangeRequest {
            displayName = userName
        }
        firebaseAuth.currentUser?.updateProfile(profileUpdate)

    }



}