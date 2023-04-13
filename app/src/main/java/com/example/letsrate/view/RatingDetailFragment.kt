package com.example.letsrate.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.letsrate.databinding.FragmentRatingDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RatingDetailFragment : Fragment() {

    private lateinit var binding : FragmentRatingDetailBinding
    private lateinit var firebase : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRatingDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebase = Firebase.firestore

        val bundle = this.arguments
        if(bundle != null){
            val productName = bundle.getString("productName").toString()
            val sellerName = bundle.getString("sellerName").toString()
            val commentTitle = bundle.getString("commentTitle").toString()
            val comment = bundle.getString("comment").toString()
            val rate = bundle.getString("rate").toString()
            val downloadUrl = bundle.getString("downloadUrl").toString()

            binding.sellerNameInput.text = sellerName
            binding.productNameInput.text = productName
            binding.commentTitleInput.text = commentTitle
            binding.commentInput.text = comment
            binding.myEventsPoint.text = rate

            Glide.with(binding.root).load(downloadUrl).into(binding.imageView2)

            binding.buttonDelete.setOnClickListener {



            }



        }


    }

}