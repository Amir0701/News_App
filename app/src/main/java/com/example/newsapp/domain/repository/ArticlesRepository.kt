package com.example.newsapp.domain.repository

import com.example.newsapp.data.db.model.ArticleEntity
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.Result
import retrofit2.Response

interface ArticlesRepository {
    suspend fun getArticles(q: String, page: Int = 1): Response<Result>

    suspend fun addArticleToHistory(article: ArticleEntity)

    suspend fun getArticlesFromHistory(): List<ArticleEntity>

    suspend fun getFavoriteArticles(): List<ArticleEntity>

    suspend fun isInFavorite(url: String): Boolean?
}