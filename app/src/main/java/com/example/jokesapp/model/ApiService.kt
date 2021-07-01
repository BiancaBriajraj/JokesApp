package com.example.jokesapp.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    //https://official-joke-api.appspot.com/jokes/programming/random

    @GET("jokes/{type}/random")
    fun getInfo(@Path("type")type:String): Call<List<Joke>>

}