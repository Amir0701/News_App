package com.example.newsapp.di

import androidx.room.Room
import com.example.newsapp.data.db.ArticleDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localDataBase = module {
    single {
        Room.databaseBuilder(androidContext(), ArticleDatabase::class.java, "ArticleDB").build()
    }
}