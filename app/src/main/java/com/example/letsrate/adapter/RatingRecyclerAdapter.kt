package com.example.letsrate.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsrate.databinding.RecyclerRowBinding
import com.example.letsrate.model.RateModel
import com.example.letsrate.view.HomeFragmentDirections

class RatingRecyclerAdapter(var rateList : ArrayList<RateModel>) : RecyclerView.Adapter <RatingRecyclerAdapter.RateHolder>() {

    class RateHolder(val binding : RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateHolder {

        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return RateHolder(binding)

    }

    override fun onBindViewHolder(holder: RateHolder, position: Int) {


        holder.binding.recyclerRowCommentTitle.text = rateList.get(position).commentTitle
        holder.binding.recyclerRowProductName.text = rateList.get(position).productName
        holder.binding.recyclerRowSellerName.text = rateList.get(position).sellerName
        holder.binding.recyclerRowCommentDescription.text = rateList.get(position).comment
        holder.binding.recyclerRowRatePoint.text = rateList.get(position).rate
        Glide.with(holder.itemView.context).load(rateList.get(position).downloadUrl).into(holder.binding.recyclerRowImageBox)

        holder.itemView.setOnClickListener {


            val action = HomeFragmentDirections.actionHomeFragmentToRatingDetailFragment()
            val bundle = Bundle()
            bundle.putString("commentTitle", rateList.get(position).commentTitle)
            bundle.putString("productName", rateList.get(position).productName)
            bundle.putString("sellerName", rateList.get(position).sellerName)
            bundle.putString("comment", rateList.get(position).comment)
            bundle.putString("rate", rateList.get(position).rate)
            bundle.putString("downloadUrl", rateList.get(position).downloadUrl)
            action.arguments.putAll(bundle)
            Navigation.findNavController(it).navigate(action)
        }



    }

    override fun getItemCount(): Int {
       return rateList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(mList: ArrayList<RateModel>){
        this.rateList = mList
        notifyDataSetChanged()
    }

    }