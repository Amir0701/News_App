package com.example.newsapp.data.model

data class Result(
    val status: String,
    val totalResults: Long,
    val articles: List<Article>
)