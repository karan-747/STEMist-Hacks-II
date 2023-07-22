package com.karan.doubthub.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.akıncı.doubthub.R
import com.akıncı.doubthub.databinding.FragmentLoginBinding


class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var  binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)

        binding.btnLogin.setOnClickListener(){
            binding.root.findNavController().navigate(R.id.action_loginFragment_to_questionInputFragment)
        }
        binding.tvSignUp.setOnClickListener(){
            binding.root.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        return binding.root
    }

}