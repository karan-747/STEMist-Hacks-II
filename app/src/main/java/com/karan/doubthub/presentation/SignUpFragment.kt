package com.karan.doubthub.presentation

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.doubthub.R
import com.example.doubthub.databinding.FragmentSignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.karan.doubthub.data.Keys

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var  binding : FragmentSignUpBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var  googleSignInClient : GoogleSignInClient



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

        binding.btnLogin.setOnClickListener(){
            navigateToHome()

        }
        binding.imageView3.setOnClickListener {
            signInWithGoogle()
        }

        return binding.root
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

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleLauncher.launch(signInIntent)
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