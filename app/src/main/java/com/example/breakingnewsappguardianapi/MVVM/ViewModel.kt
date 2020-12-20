package com.example.breakingnewsappguardianapi.MVVM

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breakingnewsappguardianapi.data.BNData
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModel(private val repo: Repository) : ViewModel() {
    val data : MutableLiveData<BNData> = MutableLiveData()

    fun readData(pageSize: Int){
        viewModelScope.launch {
            val response = repo.readData(pageSize)
            response.enqueue(object : Callback<BNData>{
                override fun onResponse(
                    call: Call<BNData>,
                    response: Response<BNData>
                ) {
                    if (response.isSuccessful){
                        data.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<BNData>, t: Throwable) {
                    Log.d("ERRORTHROW", t.toString())
                }

            })
        }
    }

    fun readDataCategory(category : String, pageSize: Int){
        viewModelScope.launch {
            val response = repo.readDataCategory(category, pageSize)
            response.enqueue(object : Callback<BNData>{
                override fun onResponse(
                        call: Call<BNData>,
                        response: Response<BNData>
                ) {
                    if (response.isSuccessful){
                        data.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<BNData>, t: Throwable) {
                    Log.d("ERRORTHROW", t.toString())
                }

            })
        }
    }
}