package com.example.jokesapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jokesapp.model.ApiService
import com.example.jokesapp.model.CustomCallback
import com.example.jokesapp.model.Joke
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JokeViewModel : ViewModel(){

private val BASE_URL ="https://official-joke-api.appspot.com/"

    private val _list = MutableLiveData<List<Joke>>()
    val list :LiveData<List<Joke>> get() = _list

    fun getInfo(category: String)
    {
        retrieveList(category)
    }

    private fun retrieveList(type: String)
    {

        val retrofit= Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        val apiService= retrofit.create(ApiService::class.java)
        val mCall: Call<List<Joke>> = apiService.getInfo(type.toLowerCase())
        mCall.enqueue(object : Callback<List<Joke>> {
            override fun onResponse(call: Call<List<Joke>>, response: Response<List<Joke>>) {
               val mmodel  = response.body()!!
                _list.value = mmodel
            }

            override fun onFailure(call: Call<List<Joke>>, t: Throwable) {
               //need to handle this error and the error of not choosing
            }
        })
    }
}