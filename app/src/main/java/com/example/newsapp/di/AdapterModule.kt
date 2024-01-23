package com.example.newsapp.di

import com.example.newsapp.presentation.view.NewsAdapter
import org.koin.dsl.module

val adapterModule= module {
    factory {
        NewsAdapter()
    }
}
