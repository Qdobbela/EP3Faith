package com.example.ep3faith.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

private const val BASE_URL = "https://icanhazdadjoke.com/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface DadJokeApiService {
    @Headers("Accept: text/plain")
    @GET("/")
    fun getRandomJoke():
        Call<String>
}

object DadJokeApi {
    val retrofitService: DadJokeApiService by lazy {
        retrofit.create(DadJokeApiService::class.java)
    }
}
