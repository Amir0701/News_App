package com.example.newsapp.di

import com.example.newsapp.ui.viewmodel.HistoryFragmentViewModel
import com.example.newsapp.ui.viewmodel.NewsDetailFragmentViewModel
import com.example.newsapp.ui.viewmodel.NewsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        NewsFragmentViewModel(get(), get())
    }

    viewModel {
        NewsDetailFragmentViewModel(get(), get())
    }

    viewModelOf(::HistoryFragmentViewModel)
}