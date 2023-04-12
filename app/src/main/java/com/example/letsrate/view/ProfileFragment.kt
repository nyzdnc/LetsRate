package com.example.letsrate.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letsrate.R
import com.example.letsrate.adapter.RatingRecyclerAdapter
import com.example.letsrate.databinding.FragmentProfileBinding
import com.example.letsrate.model.RateModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var navController : NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        navController = Navigation.findNavController(binding.root)

        binding.profileUserEmail.text = auth.currentUser!!.email

        binding.profileLogOut.setOnClickListener {

            navController.navigate(R.id.loginFragment)
            auth.signOut()

        }

        binding.userProfileMyEventsButton.setOnClickListener {

            navController.navigate(R.id.myRatingsFragment)
        }






    }

}