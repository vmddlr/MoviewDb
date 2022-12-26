package com.example.moviedb.data.usecase

import com.example.moviedb.data.model.TopRateEntity
import com.example.moviedb.data.model.VideoEntity
import com.example.moviedb.data.repository.ApiRepository
import javax.inject.Inject

class VideoUseCase @Inject constructor(private val apiRepository: ApiRepository) {

    suspend fun getServiceVideo(url: String) =
        apiRepository.apiRepository<VideoEntity>(url, language = "en-US")
}