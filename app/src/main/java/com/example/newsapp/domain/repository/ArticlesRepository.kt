package com.example.newsapp.domain.repository

import com.example.newsapp.data.model.Result
import retrofit2.Response

interface ArticlesRepository {
    suspend fun getArticles(q: String, page: Int = 1): Response<Result>
}