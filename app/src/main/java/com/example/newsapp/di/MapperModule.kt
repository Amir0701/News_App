package com.example.newsapp.di

import com.example.newsapp.data.mapper.ArticleMapper
import org.koin.dsl.module

val mapperModule = module {
    single {
        ArticleMapper()
    }
}