package com.example.newsapp.data.repository

import com.example.newsapp.data.model.Result
import com.example.newsapp.data.service.ArticleService
import com.example.newsapp.domain.repository.ArticlesRepository

class ArticlesRepositoryImpl(private val articleService: ArticleService): ArticlesRepository {
    override suspend fun getArticles(q: String, page: Int): Result {
        return articleService.getArticles(q, page)
    }

}