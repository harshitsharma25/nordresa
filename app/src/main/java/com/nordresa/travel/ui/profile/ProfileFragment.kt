package com.nordresa.travel.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nordresa.travel.R
import com.nordresa.travel.databinding.FragmentProfileBinding
import com.nordresa.travel.models.User


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        binding.profileOptions.logoutRipple.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_profileFragment_to_SigninFragment)
        }

        SetUserInfo()

//        binding.profileView.tvUserName.setText()
//        binding.profileView.tvUserEmail.setText()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun SetUserInfo(){
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val userDocRef = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)

            userDocRef.get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)
                    binding.profileView.tvUserName.text = user?.name
                    binding.profileView.tvUserEmail.text = user?.email
                    Log.d("Firestore", "Name: ${user?.name}")
                }

                .addOnFailureListener { exception ->
                    Log.e("Firestore", "get failed with ", exception)
                }
        }

    }
}