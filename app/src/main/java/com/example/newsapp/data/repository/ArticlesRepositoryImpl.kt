package com.example.newsapp.data.repository

import com.example.newsapp.data.db.ArticleDatabase
import com.example.newsapp.data.db.model.ArticleEntity
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.Result
import com.example.newsapp.data.service.ArticleService
import com.example.newsapp.domain.repository.ArticlesRepository
import retrofit2.Response

class ArticlesRepositoryImpl(private val articleService: ArticleService,
                             private val articleDatabase: ArticleDatabase): ArticlesRepository {
    override suspend fun getArticles(q: String, page: Int): Response<Result> {
        return articleService.getArticles(q, page)
    }

    override suspend fun addArticleToHistory(article: ArticleEntity) {
        articleDatabase.getDao().addToHistory(article)
    }

    override suspend fun getArticlesFromHistory(): List<ArticleEntity> {
        return articleDatabase.getDao().getArticlesFromHistory()
    }

    override suspend fun deleteArticle(article: ArticleEntity) {
        articleDatabase.getDao().deleteArticle(article)
    }
}