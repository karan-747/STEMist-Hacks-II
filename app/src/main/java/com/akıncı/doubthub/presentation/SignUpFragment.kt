package com.karan.doubthub.presentation

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.akıncı.doubthub.R
import com.karan.doubthub.data.Keys
import com.akıncı.doubthub.databinding.FragmentSignUpBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.LoginStatusCallback
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var  binding : FragmentSignUpBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var  googleSignInClient :GoogleSignInClient
    private  val callbackManager = CallbackManager.Factory.create()



    private val googleLauncher =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
            if(result.resultCode == Activity.RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResult(task)
            }
    }






    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(requireContext(),"Logged in",Toast.LENGTH_SHORT).show()
                navigateToHome()
            }
            else{
                Toast.makeText(requireContext(),it.exception?.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up,container,false)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Keys.WEB_CLIENT_ID)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(),gso)
        firebaseAuth =FirebaseAuth.getInstance()






        binding.loginButton.setPermissions("email")
        binding.loginButton.registerCallback(callbackManager,object : FacebookCallback<LoginResult>{
            override fun onCancel() {
                Toast.makeText(requireContext(),"Login cancelled by user...",Toast.LENGTH_LONG)
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(requireContext(),error.message.toString(),Toast.LENGTH_LONG)
            }

            override fun onSuccess(result: LoginResult) {
                Toast.makeText(requireContext(),"Login successful...",Toast.LENGTH_LONG)


            }

        })
        binding.btnLogin.setOnClickListener(){
            navigateToHome()
            //binding.root.findNavController().navigate(R.id.action_signUpFragment_to_questionInputFragment)
        }

        binding.iVGoogle.setOnClickListener {
            signInWithGoogle()
        }
        binding.imageView5.setOnClickListener {

            binding.loginButton.callOnClick()
        }











        binding.loginButton.setOnClickListener {
            Toast.makeText(requireContext(),"Hello fb",Toast.LENGTH_SHORT).show()

        }




        return binding.root
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    //val user = firebaseAuth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    //updateUI(null)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK

    }


    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleLauncher.launch(signInIntent)
    }


    private  fun navigateToHome(){
        binding.cvSignUp.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.Default).launch {
            delay(3500)
            withContext(Dispatchers.Main){
                binding.cvSignUp.visibility = View.GONE
                binding.root.findNavController().navigate(R.id.action_signUpFragment_to_questionInputFragment)
            }
        }

    }


    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account:GoogleSignInAccount? = task.result
            account?.let {
                updateUI(it)
            }
        }
        else{
            Toast.makeText(requireContext(),task.exception?.message,Toast.LENGTH_SHORT).show()
        }
    }
}