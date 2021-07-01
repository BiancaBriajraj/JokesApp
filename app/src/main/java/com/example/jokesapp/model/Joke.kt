package com.example.jokesapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Joke(
    val id: Int,
    val type: String,
    val setup: String,
    val punchline: String):Parcelable


