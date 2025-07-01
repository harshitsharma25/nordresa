package com.nordresa.travel.ui.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.nordresa.travel.databinding.FragmentSignUpBinding
import com.nordresa.travel.ui.base.BaseFragment

class SignUpFragment :  BaseFragment<FragmentSignUpBinding>() {
    var isPasswordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnSignUp.setOnClickListener {
            registerUser()
        }

        binding.tvSignInLink.setOnClickListener {
            gotoSignIn()
        }

        binding.etPasswordSignup.setOnTouchListener { v, event ->
            val DRAWABLE_END = 2 // Right drawable index

            if (event.action == MotionEvent.ACTION_UP) {
                val editText = binding.etPasswordSignup
                if (event.rawX >= (editText.right - editText.compoundDrawables[DRAWABLE_END].bounds.width())) {
                    // Toggle password visibility
                    isPasswordVisible = !isPasswordVisible

                    if (isPasswordVisible) {
                        editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_hide, 0)
                    } else {
                        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_show, 0)
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

    private fun registerUser() {
        val name: String = binding.etName.text.toString().trim { it <= ' ' }
        val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
        val password: String = binding.etPasswordSignup.text.toString().trim { it <= ' ' }
        val progressBar: ProgressBar = binding.pbSignUp

        if (validateForm(name, email, password)) {
            showProgressBar(progressBar)
            binding.btnSignUp.isEnabled = false

            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    binding.btnSignUp.isEnabled = true

                    if (task.isSuccessful && isAdded) {
                        Toast.makeText(requireContext(), "Account created successfully!", Toast.LENGTH_SHORT).show()
                        hideProgressBar(progressBar)
                        // Optional: Add short delay if UI looks jumpy
                        Handler(Looper.getMainLooper()).postDelayed({
                            ProceedToHomeFragment()
                        }, 500) // 300ms delay
                    } else if (isAdded) {
                        Toast.makeText(requireContext(), "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    hideProgressBar(progressBar)
                    binding.btnSignUp.isEnabled = true
                }
        }
    }

    private fun ProceedToHomeFragment(){
        findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
    }

    private fun gotoSignIn(){
        findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
    }

    private fun validateForm(name:String, email: String, password: String) : Boolean {
        return when{
            name.isEmpty() -> {
                showErrorSnackBar("Please Enter Name")
                false
            }
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
}