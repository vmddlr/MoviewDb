package com.example.moviedb.core.provider

import com.example.moviedb.core.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProviderModule {

    @Singleton
    @Provides
    fun providesInterceptor() = Interceptor { chain ->
        val url = chain.request()
            .url
            .newBuilder()
            .addQueryParameter("api_key", "d5b85f76495001d2e67853435ecd46c8")
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(url)
            .build()

        chain.proceed(newRequest)
    }


    @Singleton
    @Provides
    fun providesHttpInterceptor() = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun providesClient(networkInterceptor: HttpLoggingInterceptor, interceptor: Interceptor) =
        OkHttpClient
            .Builder()
            .addNetworkInterceptor(networkInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()

    @Singleton
    @Provides
    fun providesRetrofit(client: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)
}