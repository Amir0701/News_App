package com.example.newsapp.presentation.common

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null){

    class Loading<T>(): Resource<T>()
    class Error<T>(message: String, data: T?): Resource<T>(message = message, data = data)
    class NoInternetConnection<T>(): Resource<T>()
    class Success<T>(data: T): Resource<T>(data)
}
