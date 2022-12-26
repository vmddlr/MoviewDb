package com.example.moviedb.data.repository

import com.example.moviedb.core.network.ApiService
import com.example.moviedb.data.model.GeneralResponse
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class ApiRepository @Inject constructor(val apiService: ApiService) {

    suspend inline fun <reified T> apiRepository(
        url: String,
        entityRequest: Any? = null,
        language: String
    ): GeneralResponse<T> {

        return try {
            val response = if (entityRequest == null) {
                apiService.getServiceCoroutine(url, language)
            } else {
                apiService.postServiceCoroutine(url, entityRequest)
            }

            convertEntity(response)
        } catch (t: Throwable) {
            GeneralResponse(
                messageError = t.message.toString()
            )
        }
    }

    inline fun <reified T> convertEntity(response: Response<Any>): GeneralResponse<T> {
        val jsonObject = if (response.body() != null) {
            JSONObject(Gson().toJson(response.body()))
        } else {
            JSONObject(response.errorBody()!!.charStream().readText())
        }

        return if (response.isSuccessful) {
            GeneralResponse(
                entity = Gson().fromJson(jsonObject.toString(), T::class.java)
            )
        } else {
            GeneralResponse(
                messageError = response.message()
            )
        }
    }
}