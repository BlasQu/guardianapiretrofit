package com.example.breakingnewsappguardianapi.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Instance {
    private val retrofit by lazy{
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://content.guardianapis.com/")
            .build()
    }
    val api by lazy{
        retrofit.create(Api::class.java)
    }
}