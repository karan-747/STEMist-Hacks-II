package com.example.doubthub.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.doubthub.R
import com.example.doubthub.databinding.FragmentLoginBinding
import com.example.doubthub.databinding.FragmentSignUpBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var  binding : FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up,container,false)

        binding.btnLogin.setOnClickListener(){
            navigateToHome()
            //binding.root.findNavController().navigate(R.id.action_signUpFragment_to_questionInputFragment)
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
}