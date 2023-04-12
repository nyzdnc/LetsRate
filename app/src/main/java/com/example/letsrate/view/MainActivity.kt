package com.example.letsrate.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.letsrate.R
import com.example.letsrate.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var navController : NavController
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this, R.id.fragmentContainerView)

        auth = Firebase.auth


        auth.addAuthStateListener {
            if (it.currentUser == null) {

                binding.bottomNavigationView.visibility = View.GONE

            } else {
                binding.bottomNavigationView.visibility = View.VISIBLE

            }

            binding.bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.bottomMenuHome -> navController.navigate(R.id.homeFragment)
                    R.id.bottomMenuAdd -> navController.navigate(R.id.addRateFragment)
                    R.id.bottomMenuProfile -> navController.navigate(R.id.profileFragment)
                }
                true
            }
        }

    }

}