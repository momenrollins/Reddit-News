package com.momen.redditnews.network

import com.momen.redditnews.model.NewsRoot
import retrofit2.http.GET

interface ApiInterface {

    @GET("kotlin/.json")
    suspend fun getNews(): NewsRoot
}