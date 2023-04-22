package com.example.letsrate.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letsrate.adapter.RatingRecyclerAdapter
import com.example.letsrate.databinding.FragmentHomeBinding
import com.example.letsrate.model.RateModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var ratingArrayList : ArrayList<RateModel>
    private lateinit var firebase : FirebaseFirestore
    private lateinit var ratingRecyclerAdapter : RatingRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebase = Firebase.firestore

        ratingArrayList = ArrayList()

        getData()

        binding.homePageRecyclerView.layoutManager = LinearLayoutManager(this.context)
        ratingRecyclerAdapter = RatingRecyclerAdapter(ratingArrayList)
        binding.homePageRecyclerView.adapter = ratingRecyclerAdapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

    }

    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<RateModel>()
            for (i in ratingArrayList) {
                if (i.productName.lowercase(Locale.ROOT).contains(query) || i.sellerName.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
            } else {
                ratingRecyclerAdapter.setFilteredList(filteredList)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData() {
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

                        ratingArrayList.add(rating)
                    }
                ratingRecyclerAdapter.notifyDataSetChanged()

            }.addOnFailureListener {
                println(it.localizedMessage)
            }

    }

}