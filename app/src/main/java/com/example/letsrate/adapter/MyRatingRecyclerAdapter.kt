package com.example.letsrate.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsrate.R
import com.example.letsrate.databinding.RecyclerRowBinding
import com.example.letsrate.model.RateModel
import com.example.letsrate.view.HomeFragmentDirections
import com.example.letsrate.view.MyRatingsFragmentDirections

class MyRatingRecyclerAdapter(var myRateList : ArrayList<RateModel>) : RecyclerView.Adapter <MyRatingRecyclerAdapter.RateHolder>() {
    
    class RateHolder(val binding : RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateHolder {

        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return RateHolder(binding)

    }

    override fun onBindViewHolder(holder: RateHolder, position: Int) {


        holder.binding.recyclerRowCommentTitle.text = myRateList.get(position).commentTitle
        holder.binding.recyclerRowProductName.text = myRateList.get(position).productName
        holder.binding.recyclerRowSellerName.text = myRateList.get(position).sellerName
        holder.binding.recyclerRowCommentDescription.text = myRateList.get(position).comment
        holder.binding.recyclerRowRatePoint.text = myRateList.get(position).rate
        Glide.with(holder.itemView.context).load(myRateList.get(position).downloadUrl).into(holder.binding.recyclerRowImageBox)


        holder.itemView.setOnClickListener {


            val action = MyRatingsFragmentDirections.actionMyRatingsFragmentToRatingDetailFragment()
            val bundle = Bundle()
            bundle.putString("commentTitle", myRateList.get(position).commentTitle)
            bundle.putString("productName", myRateList.get(position).productName)
            bundle.putString("sellerName", myRateList.get(position).sellerName)
            bundle.putString("comment", myRateList.get(position).comment)
            bundle.putString("rate", myRateList.get(position).rate)
            bundle.putString("downloadUrl", myRateList.get(position).downloadUrl)
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