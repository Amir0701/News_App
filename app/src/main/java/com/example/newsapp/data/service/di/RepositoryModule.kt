package com.example.newsapp.data.service.di

import com.example.newsapp.data.repository.ArticlesRepositoryImpl
import com.example.newsapp.domain.repository.ArticlesRepository
import org.koin.dsl.module


val repositoryModule = module {
    single<ArticlesRepository> {
        ArticlesRepositoryImpl(get())
    }
}