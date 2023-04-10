package com.example.letsrate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsrate.databinding.RecyclerRowBinding
import com.example.letsrate.model.RateModel

class RatingRecyclerAdapter(val rateList : ArrayList<RateModel>) : RecyclerView.Adapter <RatingRecyclerAdapter.RateHolder>() {

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


    }

    override fun getItemCount(): Int {
       return rateList.size
    }
}