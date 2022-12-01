package com.momen.redditnews.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.momen.redditnews.MainApplication
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    private const val BASE_URL = "https://www.reddit.com/r/"
    private var onlineInterceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        val maxAge = 60 // read from cache for 60 seconds even if there is internet connection
        response.newBuilder()
            .header("Cache-Control", "public, max-age=$maxAge")
            .removeHeader("Pragma")
            .build()
    }
    private var offlineInterceptor = Interceptor { chain ->
        var request: Request = chain.request()
        if (!isNetworkAvailable()) {
            val maxStale = 60 * 60 * 24 * 30 // Offline cache available for 30 days
            request = request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .removeHeader("Pragma")
                .build()
        }
        chain.proceed(request)
    }
    var cacheSize = 10 * 1024 * 1024L // 10 MB

    private var cache: Cache = Cache(MainApplication.appContext.cacheDir, cacheSize)

    private var okHttpClient: OkHttpClient =
        OkHttpClient.Builder() // .addInterceptor(provideHttpLoggingInterceptor()) // For HTTP request & Response data logging
            .addInterceptor(offlineInterceptor)
            .addNetworkInterceptor(onlineInterceptor)
            .cache(cache)
            .build()
    private val retrofit: Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .client(
                okHttpClient
            )
            .build()
    val apiInterface: ApiInterface = retrofit.create(ApiInterface::class.java)


    private fun isNetworkAvailable() =
        (MainApplication.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            getNetworkCapabilities(activeNetwork)?.run {
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } ?: false
        }
}