package com.example.newsapp.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.data.db.model.ArticleEntity
import com.example.newsapp.data.model.Article

@Dao
interface ArticleEntityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToHistory(article: ArticleEntity)
    @Query("SELECT * FROM Article WHERE isInHistory = 1")
    suspend fun getArticlesFromHistory(): List<ArticleEntity>

    @Delete
    suspend fun deleteArticle(article: ArticleEntity)
}