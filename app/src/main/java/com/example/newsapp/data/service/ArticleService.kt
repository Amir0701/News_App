package com.example.newsapp.data.service

import com.example.newsapp.data.model.Result
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleService {
    @GET("/v2/everything")
    suspend fun getArticles(@Query("q") q: String, @Query("page") page: Int): Response<Result>
}