package com.example.letsrate.view

import android.os.Bundle
import android.provider.Telephony.Mms.Rate
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letsrate.R
import com.example.letsrate.adapter.RatingRecyclerAdapter
import com.example.letsrate.databinding.FragmentHomeBinding
import com.example.letsrate.model.RateModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var ratingArrayList : ArrayList<RateModel>
    private lateinit var firebase : FirebaseFirestore
    private lateinit var ratingRecyclerAdapter : RatingRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebase = Firebase.firestore

        ratingArrayList = ArrayList<RateModel>()

        binding.homePageRecyclerView.layoutManager = LinearLayoutManager(this.context)
        ratingRecyclerAdapter = RatingRecyclerAdapter(ratingArrayList)
        binding.homePageRecyclerView.adapter = ratingRecyclerAdapter


        getDate()
    }

    private fun getDate(){

        firebase.collection("Ratings").orderBy("createdDate", Query.Direction.DESCENDING).addSnapshotListener { value, error ->

            if(error != null){
                Toast.makeText(this.context,error.localizedMessage,Toast.LENGTH_LONG).show()
            } else {
                if(!value!!.isEmpty){

                    val documents = value.documents

                    ratingArrayList.clear()

                    for (document in documents){

                        val comment = document.get("comment") as String
                        val commentTitle = document.get("commentTitle") as String
                        val downloadUrl = document.get("downloadUrl") as String
                        val sellerName = document.get("sellerName") as String
                        val productName = document.get("productName") as String
                        val userEmail = document.get("userEmail") as String
                        val rate = document.get("rate") as String

                        val rating = RateModel(
                            commentTitle,
                            productName,
                            sellerName,
                            comment,
                            rate,
                            downloadUrl,
                            userEmail)

                        ratingArrayList.add(rating)


                    }

                    ratingRecyclerAdapter.notifyDataSetChanged()
                }
            }
        }
    }


}