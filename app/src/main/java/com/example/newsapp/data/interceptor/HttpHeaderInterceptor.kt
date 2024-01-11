package com.example.newsapp.data.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class HttpHeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request =
            chain
            .request()
            .newBuilder()
            .addHeader("X-Api-Key", "ca9984bf10144e429d80f30de1c8ab7e")
            .build()

        return chain.proceed(request)
    }
}