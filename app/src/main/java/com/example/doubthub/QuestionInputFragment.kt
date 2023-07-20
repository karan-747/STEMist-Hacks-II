package com.example.doubthub

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.navigation.navOptions
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.example.doubthub.databinding.FragmentQuestionInputBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.lang.Exception


class QuestionInputFragment : Fragment(R.layout.fragment_question_input) {

    private lateinit var  binding: FragmentQuestionInputBinding

    private val textRecogniser = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private var textImage : InputImage? = null







    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // Use the returned uri.
            val uriContent = result.uriContent
            uriContent?.let {

                textImage  = InputImage.fromFilePath(requireContext(),it)
                recogniseTheText(textImage!!)
            }
        } else {
            // An error occurred.
            val exception = result.error
            Toast.makeText(requireContext(),exception?.message,Toast.LENGTH_SHORT).show()

        }
    }

    private fun startCrop() {


        cropImage.launch(
           CropImageContractOptions(null,
               CropImageOptions(true,
                   imageSourceIncludeCamera = true,
                   cropShape = CropImageView.CropShape.RECTANGLE, showCropLabel = true,
                   allowFlipping = true,
                   activityTitle = "Crop Question",
                   showProgressBar = true,
                   noOutputImage = false,
               )
           )
        )
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_question_input,container,false)
        val subjects = resources.getStringArray(R.array.Subjects)
        val classes = resources.getStringArray(R.array.Classes)

        binding.spnClass.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.spnSubject.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        binding.imgBtnCamera.setOnClickListener(){
            startCrop()
        }
        return binding.root
    }




    private fun recogniseTheText(image: InputImage) {
        val result = textRecogniser.process(image).addOnSuccessListener {
            binding.edtQuestion.setText(it.text)
        }.addOnFailureListener {
            Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
        }
    }


}