package com.example.breakingnewsappguardianapi.recyclerview

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.breakingnewsappguardianapi.MainActivity
import com.example.breakingnewsappguardianapi.R
import com.example.breakingnewsappguardianapi.data.BNData
import com.example.breakingnewsappguardianapi.data.Response
import com.example.breakingnewsappguardianapi.data.Result
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rv_item.view.*

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    var data : List<Result> = emptyList()

    class diffCallback(val oldList: List<Result>, val newList: List<Result>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].apiUrl == newList[newItemPosition].apiUrl
        }
    }

    fun submitData(newList: List<Result>){
        val oldList = this.data
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback(oldList, newList))
        this.data = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(data[adapterPosition].webUrl)))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.rvtitle.text = data[position].webTitle
        Glide.with(holder.itemView.context).load(data[position].fields.thumbnail).transform(RoundedCorners(30)).into(holder.itemView.rvimage)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}