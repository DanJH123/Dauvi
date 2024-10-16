package com.dapps.dauvi.core.util
import okhttp3.Interceptor
import okhttp3.Response

class AuthParamInterceptor(private val authToken: String) : Interceptor {

    companion object {
        const val AUTH_TOKEN_FIELD = "key"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val urlWithToken = originalRequest.url.newBuilder()
            .addQueryParameter(AUTH_TOKEN_FIELD, authToken)
            .build()

        // Create a new request with the modified URL
        val newRequest = originalRequest.newBuilder()
            .url(urlWithToken)
            .build()

        return chain.proceed(newRequest)
    }
}