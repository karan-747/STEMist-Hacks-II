package com.karan.doubthub.domain

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

object Repository: RepositoryInterface {

    private val model = FirebaseDataSource

    override suspend fun signInUserWithGoogle(account: GoogleSignInAccount): Pair<Boolean, String>{
        return model.signInUserWithGoogle(account)
    }

    override suspend fun signUpUser(
        userName: String,
        email: String,
        password: String
    ): Pair<Boolean, String> {
        return model.signUpUser(userName, email, password)
    }
}