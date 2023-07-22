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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

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

import com.karan.doubthub.R
import com.karan.doubthub.databinding.FragmentSignUpBinding
import com.karan.doubthub.presentation.viewmodel.LoginSignUpVM


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var  binding : FragmentSignUpBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var  googleSignInClient : GoogleSignInClient
    private lateinit var mViewModel: LoginSignUpVM



    private val googleLauncher =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResult(task)
        }
    }
    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        binding.progressBar.visibility = View.GONE
        CoroutineScope(Dispatchers.IO).launch {
            val result = mViewModel.signInUserWithGoogle(account)
            withContext(Dispatchers.Main){
                if(result.first){
                    Toast.makeText(requireContext(),result.second,Toast.LENGTH_SHORT).show()
                    navigateToHome()
                }
                else{
                    Toast.makeText(requireContext(),result.second,Toast.LENGTH_SHORT).show()
                }
            }
        }
//        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
//            if(it.isSuccessful){
//                Toast.makeText(requireContext(),"Logged in",Toast.LENGTH_SHORT).show()
//                navigateToHome()
//            }
//            else{
//                Toast.makeText(requireContext(),it.exception?.toString(),Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up,container,false)

        mViewModel = ViewModelProvider(this@SignUpFragment)[LoginSignUpVM::class.java]

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

        binding.btnLogin.setOnClickListener(){
            if(checkInputFields()){
               signUpNewUser()
            }
            else{
                Toast.makeText(requireContext(),"Please enter input fields...",Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun signUpNewUser() {
        val userName= binding.etUsername.text.toString()
        val email= binding.etEmail.text.toString()
        val password= binding.etPassword.text.toString()
        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            val result = mViewModel.signUpUser(userName,email,password)
            withContext(Dispatchers.Main){
                if(result.first){
                    binding.progressBar.visibility = View.GONE
                    navigateToHome()
                }
                else{
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(),result.second,Toast.LENGTH_SHORT).show()
                }
            }

        }


    }


    private  fun navigateToHome(){
        binding.cvSignUp.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.Default).launch {
            delay(3500)
            withContext(Dispatchers.Main){
                binding.cvSignUp.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
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

    private fun checkInputFields():Boolean{
        val userName= binding.etUsername.text.toString()
        val email= binding.etEmail.text.toString()
        val password= binding.etPassword.text.toString()
        if(userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
            return true
        }
        return false
    }

}