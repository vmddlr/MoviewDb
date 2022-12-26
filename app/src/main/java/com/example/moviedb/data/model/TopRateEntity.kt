package com.example.moviedb.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class TopRateEntity(
    @SerializedName("page") var page: Int? = null,
    @SerializedName("results") var listTopRate: ArrayList<TopRateDetailEntity> = arrayListOf(),
    @SerializedName("total_results") var totalResults: Int? = null,
    @SerializedName("total_pages") var totalPages: Int? = null
) : Serializable

@Parcelize
data class TopRateDetailEntity(
    @SerializedName("poster_path") var posterPath: String? = null,
    @SerializedName("adult") var adult: Boolean? = null,
    @SerializedName("overview") var overview: String? = null,
    @SerializedName("release_date") var releaseDate: String? = null,
    @SerializedName("genre_ids") var genreIds: ArrayList<Int> = arrayListOf(),
    @SerializedName("id") var id: Int? = null,
    @SerializedName("original_title") var originalTitle: String? = null,
    @SerializedName("original_language") var originalLanguage: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("backdrop_path") var backdropPath: String? = null,
    @SerializedName("popularity") var popularity: Double? = null,
    @SerializedName("vote_count") var voteCount: Int? = null,
    @SerializedName("video") var video: Boolean? = null,
    @SerializedName("vote_average") var voteAverage: Double? = null
): Parcelable