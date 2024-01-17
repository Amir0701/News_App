package com.example.newsapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.data.db.converter.SourceTypeConverter
import com.example.newsapp.data.db.dao.ArticleEntityDao
import com.example.newsapp.data.db.model.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1)
@TypeConverters(SourceTypeConverter::class)
abstract class ArticleDatabase: RoomDatabase(){
    abstract fun getDao(): ArticleEntityDao
}