package com.nordresa.travel.ui.auth

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.nordresa.travel.R
import androidx.navigation.fragment.findNavController
import com.nordresa.travel.databinding.FragmentSigninBinding
import com.nordresa.travel.ui.base.BaseFragment

class SignInFragment : BaseFragment<FragmentSigninBinding>() {
    var isPasswordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnSignIn.setOnClickListener {
            signInUser()
        }
        binding.tvSignUpLink.setOnClickListener {
            gotoSignUp()
        }


        binding.etPasswordSignin.setOnTouchListener { v, event ->
            val DRAWABLE_END = 2 // Right drawable index

            if (event.action == MotionEvent.ACTION_UP) {
                val editText = binding.etPasswordSignin
                if (event.rawX >= (editText.right - editText.compoundDrawables[DRAWABLE_END].bounds.width())) {
                    // Toggle password visibility
                    isPasswordVisible = !isPasswordVisible

                    if (isPasswordVisible) {
                        editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_show, 0)
                    } else {
                        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_hide, 0)
                    }

                    // Move cursor to end
                    editText.setSelection(editText.text.length)
                    return@setOnTouchListener true
                }
            }
            false
        }


        super.onViewCreated(view, savedInstanceState)
    }

    private fun signInUser(){
        val email : String = binding.etEmailSignin.text.toString().trim{ it <= ' '}
        val password : String = binding.etPasswordSignin.text.toString().trim{ it <= ' '}
        val progressBar : ProgressBar = binding.pbSeries

        if(validForm(email,password)){
            showProgressBar(progressBar)

            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    hideProgressBar(progressBar)
                    if(task.isSuccessful){
                        findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                    } else{
                        Toast.makeText(
                            requireContext(),
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,).show()
                    }
                }.addOnFailureListener { exception ->
                    hideProgressBar(progressBar)
                    Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun validForm(email : String, password : String) : Boolean {
        return when{
            email.isEmpty() -> {
                showErrorSnackBar("Please Enter Email")
                false
            }
            password.isEmpty() -> {
                showErrorSnackBar("Please Enter Password")
                false
            }
            else -> {
                true
            }
        }
    }

    private fun gotoSignUp(){
        findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
    }
}