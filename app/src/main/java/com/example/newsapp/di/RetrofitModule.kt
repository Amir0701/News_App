package com.example.newsapp.di

import com.example.newsapp.BuildConfig
import com.example.newsapp.data.interceptor.HttpHeaderInterceptor
import com.example.newsapp.presentation.common.InternetConnection
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {
    factory<GsonConverterFactory> {
        GsonConverterFactory.create(get())
    }

    factory<Gson> {
        Gson()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(get(GsonConverterFactory::class))
            .client(get(OkHttpClient::class))
            .build()
    }

    factory<HttpLoggingInterceptor> {
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    factory<HttpHeaderInterceptor> {
        HttpHeaderInterceptor()
    }

    single<OkHttpClient>{
        OkHttpClient
            .Builder()
            .addInterceptor(get<HttpLoggingInterceptor>(HttpLoggingInterceptor::class))
            .addInterceptor(get<HttpHeaderInterceptor>(HttpHeaderInterceptor::class))
            .build()
    }

    factory {
        InternetConnection(get())
    }
}