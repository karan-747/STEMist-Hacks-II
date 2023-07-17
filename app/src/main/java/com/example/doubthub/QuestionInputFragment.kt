package com.example.doubthub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.doubthub.databinding.FragmentQuestionInputBinding


class QuestionInputFragment : Fragment(R.layout.fragment_question_input) {

    private lateinit var  binding: FragmentQuestionInputBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_question_input,container,false)

        binding.imgBtnCamera.setOnClickListener(){

        }

        return binding.root
    }
}