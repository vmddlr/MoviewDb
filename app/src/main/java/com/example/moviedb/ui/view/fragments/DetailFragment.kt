package com.example.moviedb.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.moviedb.R
import com.example.moviedb.data.model.TopRateDetailEntity
import com.example.moviedb.data.model.UpComingDetailEntity
import com.example.moviedb.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    private var upComingEntity: UpComingDetailEntity? = null
    private var topRateEntity: TopRateDetailEntity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arg: DetailFragmentArgs by navArgs()
        arg.entityUpComing?.let {
            upComingEntity = it
        }

        arg.entityToRate?.let {
            topRateEntity = it
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        this.binding = FragmentDetailBinding.inflate(inflater, container, false)
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.initFragment()
    }

    private fun initFragment() {
        val poster =
            if (topRateEntity != null) topRateEntity?.posterPath else upComingEntity?.posterPath
        val year =
            if (topRateEntity != null) topRateEntity?.releaseDate else upComingEntity?.releaseDate
        val title =
            if (topRateEntity != null) topRateEntity?.originalTitle else upComingEntity?.originalTitle
        val language =
            if (topRateEntity != null) topRateEntity?.originalLanguage else upComingEntity?.originalLanguage
        val voteAverage =
            if (topRateEntity != null) topRateEntity?.voteAverage else upComingEntity?.voteAverage
        val override =
            if (topRateEntity != null) topRateEntity?.overview else upComingEntity?.overview
        val id = if (topRateEntity != null) topRateEntity?.id else upComingEntity?.id

        Glide.with(this.requireContext())
            .load("https://image.tmdb.org/t/p/w500/${poster}")
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_movies)
            .centerCrop()
            .into(binding.ivPoster)

        binding.tvTitle.text = title
        binding.tvYear.text = year?.substring(0, 4)
        binding.tvLanguage.text = language
        binding.tvRank.text = voteAverage.toString()
        binding.tvOverview.text = override

        binding.btnPreview.setOnClickListener {
            val action = DetailFragmentDirections.toVideoFragment(id.toString())
            this.requireView().findNavController().navigate(action)
        }
    }
}