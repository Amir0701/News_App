package com.example.newsapp

import android.app.Application
import com.example.newsapp.data.service.di.repositoryModule
import com.example.newsapp.data.service.di.serviceModule
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(repositoryModule, serviceModule)
        }
    }
}