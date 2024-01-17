package com.example.newsapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.newsapp.data.db.model.ArticleEntity
import com.example.newsapp.data.model.Article

@Dao
interface ArticleEntityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToHistory(article: ArticleEntity)
}