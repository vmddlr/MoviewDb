package com.example.moviedb.data.usecase

import com.example.moviedb.data.model.TopRateEntity
import com.example.moviedb.data.model.UpComingEntity
import com.example.moviedb.data.repository.ApiRepository
import javax.inject.Inject

class HomeUseCase @Inject constructor(private val apiRepository: ApiRepository) {

    suspend fun getServiceUpComing(url: String) =
        apiRepository.apiRepository<UpComingEntity>(url, language = "es")

    suspend fun getServiceTopRate(url: String) =
        apiRepository.apiRepository<TopRateEntity>(url, language = "es")
}