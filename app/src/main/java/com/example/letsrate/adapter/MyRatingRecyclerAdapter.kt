package com.example.letsrate.adapter

import android.app.AlertDialog
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsrate.databinding.MyratingsrecyclerRowBinding
import com.example.letsrate.model.RateModel
import com.example.letsrate.view.MyRatingsFragmentDirections
import com.google.firebase.firestore.FirebaseFirestore

class MyRatingRecyclerAdapter(var myRateList : ArrayList<RateModel>) : RecyclerView.Adapter <MyRatingRecyclerAdapter.RateHolder>() {

    class RateHolder(val binding : MyratingsrecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateHolder {

        val binding = MyratingsrecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return RateHolder(binding )

    }

    override fun onBindViewHolder(holder: RateHolder, position: Int) {

        holder.binding.recyclerRowCommentTitle.text = myRateList.get(position).commentTitle
        holder.binding.recyclerRowProductName.text = myRateList.get(position).productName
        holder.binding.recyclerRowSellerName.text = myRateList.get(position).sellerName
        holder.binding.recyclerRowCommentDescription.text = myRateList.get(position).comment
        holder.binding.recyclerRowRatePoint.text = myRateList.get(position).rate
        Glide.with(holder.itemView.context).load(myRateList.get(position).downloadUrl).into(holder.binding.recyclerRowImageBox)

        holder.binding.dltBtn.setOnClickListener {

            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("Delete")
            builder.setMessage("Are you sure ?")

            builder.setPositiveButton("Yes") { dialog, which ->
                Toast.makeText(it.context,
                    "Deleted", Toast.LENGTH_SHORT).show()

                val db = FirebaseFirestore.getInstance()
                val myCollection = db.collection("Ratings")
                myRateList.get(position).rateId?.let { it1 ->
                    myCollection.document(it1)
                        .delete()
                        .addOnSuccessListener {

                        }
                        .addOnFailureListener { exception ->

                        }
                }

                val action = MyRatingsFragmentDirections.myRatingsFragmentReload()
                Navigation.findNavController(it).navigate(action)

            }

            builder.setNegativeButton("No") { dialog, which ->
                Toast.makeText(it.context,
                    "Canceled", Toast.LENGTH_SHORT).show()
            }

            builder.show()

        }

        holder.itemView.setOnClickListener {

            val action = MyRatingsFragmentDirections.actionMyRatingsFragmentToRatingDetailFragment()
            val bundle = Bundle()
            bundle.putString("commentTitle", myRateList.get(position).commentTitle)
            bundle.putString("productName", myRateList.get(position).productName)
            bundle.putString("sellerName", myRateList.get(position).sellerName)
            bundle.putString("comment", myRateList.get(position).comment)
            bundle.putString("rate", myRateList.get(position).rate)
            bundle.putString("downloadUrl", myRateList.get(position).downloadUrl)
            bundle.putString("rateId", myRateList.get(position).rateId)

            action.arguments.putAll(bundle)
            Navigation.findNavController(it).navigate(action)
        }

        holder.binding.editBtn.setOnClickListener {

            val action = MyRatingsFragmentDirections.actionMyRatingsFragmentToUpdateRatingFragment()
            val bundle = Bundle()
            bundle.putString("commentTitle", myRateList.get(position).commentTitle)
            bundle.putString("productName", myRateList.get(position).productName)
            bundle.putString("sellerName", myRateList.get(position).sellerName)
            bundle.putString("comment", myRateList.get(position).comment)
            bundle.putString("rate", myRateList.get(position).rate)
            bundle.putString("downloadUrl", myRateList.get(position).downloadUrl)
            bundle.putString("rateId", myRateList.get(position).rateId)
            action.arguments.putAll(bundle)
            Navigation.findNavController(it).navigate(action)

        }

    }

    override fun getItemCount(): Int {
       return myRateList.size
    }

    fun setFilteredList(mList: ArrayList<RateModel>){
        this.myRateList = mList
        notifyDataSetChanged()
    }

    }