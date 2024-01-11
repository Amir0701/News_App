package com.example.newsapp.domain.repository

import com.example.newsapp.data.model.Result

interface ArticlesRepository {
    suspend fun getArticles(q: String, page: Int = 1): Result
}