package com.example.newsapp.data.mapper

import com.example.newsapp.data.db.model.ArticleEntity
import com.example.newsapp.data.model.Article

class ArticleMapper {
    fun toArticleEntity(article: Article): ArticleEntity{
        return ArticleEntity(
            article.url,
            article.author,
            article.title,
            article.description,
            article.urlToImage,
            article.publishedAt,
            article.content,
            article.source
        )
    }
}