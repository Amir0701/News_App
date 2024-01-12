package com.example.newsapp.di

import android.content.Context.MODE_PRIVATE
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharedPreferenceModule = module {
    single {
        androidContext().getSharedPreferences("Settings", MODE_PRIVATE)
    }
}