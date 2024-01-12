package com.example.newsapp

import android.app.Application
import com.example.newsapp.data.service.di.repositoryModule
import com.example.newsapp.data.service.di.retrofitModule
import com.example.newsapp.data.service.di.serviceModule
import com.example.newsapp.data.service.di.viewModelModule
import org.koin.core.context.startKoin
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(repositoryModule, serviceModule, viewModelModule, retrofitModule))
        }

    }
}