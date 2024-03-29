package com.example.jokesapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jokesapp.model.ApiService
import com.example.jokesapp.model.Joke
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JokeViewModel : ViewModel() {

    private val BASEURL = "https://official-joke-api.appspot.com/"
    private val _list = MutableLiveData<List<Joke>>()
    val list: LiveData<List<Joke>> get() = _list
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Boolean>()

    fun getInfo(category: String) {
        retrieveList(category)
    }

    private fun retrieveList(type: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASEURL)
            .build()
        loading.value = true
        val apiService = retrofit.create(ApiService::class.java)
        val mCall: Call<List<Joke>> = apiService.getInfo(isRandom(type))
        mCall.enqueue(object : Callback<List<Joke>> {
            override fun onResponse(call: Call<List<Joke>>, response: Response<List<Joke>>) {
                val fromApiResult = response.body()!!
                _list.value = fromApiResult
                loading.value = false
            }
            override fun onFailure(call: Call<List<Joke>>, t: Throwable) {
                loading.value = true
                error.value = true
            }
        })
    }

    private fun isRandom(type: String): String {
        val listOfOptions = listOf("programming", "general")
        return if (type.lowercase() == "random") {
            listOfOptions.random()
        }else {
            type.lowercase()
        }
    }
}
