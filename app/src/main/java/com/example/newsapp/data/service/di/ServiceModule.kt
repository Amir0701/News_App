package com.example.newsapp.data.service.di

import com.example.newsapp.data.service.ArticleService
import com.google.gson.Gson
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val serviceModule = module {
    includes(retrofitModule)

    single<ArticleService>() {
        provideArticleService(get())
    }
}

fun provideArticleService(retrofit: Retrofit): ArticleService{
    return retrofit.create(ArticleService::class.java)
}