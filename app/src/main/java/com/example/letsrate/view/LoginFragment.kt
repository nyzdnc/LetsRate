package com.example.letsrate.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.letsrate.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var firebase : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        firebase = Firebase.firestore

        if(auth.currentUser != null){
            val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }

        binding.buttonSignUp.setOnClickListener {
            createUser()

        }

        binding.buttonLogin.setOnClickListener {
            login()
        }

    }

    private fun createUser(){
        val email = binding.inputEmail.text.toString()
        val password = binding.inputPassword.text.toString()

        if( email.equals("") || password.equals("") ){

            Toast.makeText(this.context,"Please fill the e-mail and password !",Toast.LENGTH_LONG).show()

        } else {

            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                val userMap = hashMapOf<String, String>()

                userMap.put("userEmail", email)
                userMap.put("userPassword", password)

                firebase.collection("Users").add(userMap)

                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                Navigation.findNavController(binding.root).navigate(action)

            }.addOnFailureListener {
                Toast.makeText(this.context,it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun login(){
        val email = binding.inputEmail.text.toString()
        val password = binding.inputPassword.text.toString()

        if( email.equals("") || password.equals("") ){

            Toast.makeText(this.context,"Please fill the e-mail and password !",Toast.LENGTH_LONG).show()

        } else {

            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {

                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                Navigation.findNavController(binding.root).navigate(action)

            }.addOnFailureListener {
                Toast.makeText(this.context,it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}