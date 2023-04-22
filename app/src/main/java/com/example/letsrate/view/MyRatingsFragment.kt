package com.example.letsrate.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letsrate.adapter.MyRatingRecyclerAdapter
import com.example.letsrate.databinding.FragmentMyRatingsBinding
import com.example.letsrate.model.RateModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyRatingsFragment : Fragment() {
    private lateinit var binding : FragmentMyRatingsBinding
    private lateinit var myRatingsArrayList : ArrayList<RateModel>
    private lateinit var firebase : FirebaseFirestore
    private lateinit var myRatingRecyclerAdapter : MyRatingRecyclerAdapter
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyRatingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebase = Firebase.firestore
        auth = Firebase.auth
        myRatingsArrayList = ArrayList<RateModel>()

        binding.myRatingsRecyclerView.layoutManager = LinearLayoutManager(this.context)
        myRatingRecyclerAdapter = MyRatingRecyclerAdapter(myRatingsArrayList)
        binding.myRatingsRecyclerView.adapter = myRatingRecyclerAdapter

        getData()

    }

    private fun getData(){

        firebase.collection("Ratings").orderBy("createdDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val comment = document.get("comment") as String
                    val commentTitle = document.get("commentTitle") as String
                    val downloadUrl = document.get("downloadUrl") as String
                    val sellerName = document.get("sellerName") as String
                    val productName = document.get("productName") as String
                    val userEmail = document.get("userEmail") as String
                    val rate = document.get("rate") as String
                    val rateId = document.id
                    val createdDate = document.get("createdDate") as Timestamp

                    val rating = RateModel(
                        commentTitle,
                        productName,
                        sellerName,
                        comment,
                        rate,
                        downloadUrl,
                        userEmail,
                        rateId,
                        createdDate
                    )
                        if(userEmail == auth.currentUser!!.email!!){
                            myRatingsArrayList.add(rating)

                        }

                }
                myRatingRecyclerAdapter.notifyDataSetChanged()

            }.addOnFailureListener {
                println(it.localizedMessage)
            }
    }
}