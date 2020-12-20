package com.example.breakingnewsappguardianapi.MVVM

import com.example.breakingnewsappguardianapi.data.BNData
import com.example.breakingnewsappguardianapi.data.CONSTS
import com.example.breakingnewsappguardianapi.retrofit.Instance
import retrofit2.Call
import retrofit2.Response
import java.util.*

class Repository {

    fun readData(pageSize: Int) : Call<BNData> = Instance.api.getNews(CONSTS.API_KEY,"thumbnail", pageSize)
    fun readDataCategory(category : String, pageSize: Int) : Call<BNData> = Instance.api.getNewsCategory(CONSTS.API_KEY,"thumbnail", category.toLowerCase(Locale.ROOT), pageSize)
}