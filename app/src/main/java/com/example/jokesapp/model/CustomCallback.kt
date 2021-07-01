package com.example.jokesapp.model

interface CustomCallback {
    fun onSuccess(value: Joke)
    fun onFailure(message: String)
}