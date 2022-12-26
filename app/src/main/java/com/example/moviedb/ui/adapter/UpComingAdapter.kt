package com.example.moviedb.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.moviedb.R
import com.example.moviedb.data.model.UpComingDetailEntity
import com.example.moviedb.databinding.ItemCardLayoutBinding

class UpComingAdapter(
    private val listItem: ArrayList<UpComingDetailEntity>,
    private val setOnClickListener:(UpComingDetailEntity) -> Unit
): RecyclerView.Adapter<UpComingAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_layout, parent, false)

        return this.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = listItem[position]
        with(holder){
            Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500/${entity.posterPath}")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_movies)
                .centerCrop()
                .into(binding.ivPoster)

            binding.cardView.setOnClickListener { setOnClickListener(entity) }
        }

    }

    override fun getItemCount(): Int = listItem.size

    inner class  ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemCardLayoutBinding.bind(view)
    }
}