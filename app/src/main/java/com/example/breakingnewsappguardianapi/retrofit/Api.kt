package com.example.breakingnewsappguardianapi.retrofit

import com.example.breakingnewsappguardianapi.data.BNData
import com.example.breakingnewsappguardianapi.data.CONSTS
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("search")
    fun getNews(@Query("api-key") api_key: String, @Query("show-fields") thmb: String = "thumbnail", @Query("page-size") pages: Int = 10) : Call<BNData>

    @GET("search")
    fun getNewsCategory(@Query("api-key") api_key: String, @Query("show-fields") thmb: String = "thumbnail", @Query("section") section: String, @Query("page-size") pages: Int = 10) : Call<BNData>
}