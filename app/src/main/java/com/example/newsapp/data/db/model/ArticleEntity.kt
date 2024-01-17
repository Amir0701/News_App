package com.example.newsapp.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapp.data.model.Source

@Entity(tableName = "Article")
data class ArticleEntity(
    @PrimaryKey
    val url: String,
    val author: String? = null,
    val title: String,
    val description: String,
    val urlToImage: String? = null,
    val publishedAt: String,
    val content: String,
    val source: Source,
    val isInHistory: Boolean = false,
    val isInFavorite: Boolean = false
)