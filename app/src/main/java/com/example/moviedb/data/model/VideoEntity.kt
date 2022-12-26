package com.example.moviedb.data.model

data class VideoEntity(
    val id: Long,
    val results: List<Result>
)


data class Result(
    val name: String,
    val key: String,
    val site: String,
    val size: Long,
    val type: String,
    val official: Boolean,
    val publishedAt: String,
    val id: String
)