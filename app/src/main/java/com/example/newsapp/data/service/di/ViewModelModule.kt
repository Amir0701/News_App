package com.example.newsapp.data.service.di

import com.example.newsapp.ui.viewmodel.NewsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        NewsFragmentViewModel(get())
    }
}