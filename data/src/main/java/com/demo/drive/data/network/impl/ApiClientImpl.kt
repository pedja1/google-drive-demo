package com.demo.drive.data.network.impl

import android.content.Context
import com.demo.drive.core.network.AuthorizationProvider
import com.demo.drivedemo.api.BuildConfig
import com.demo.drive.data.network.ApiClient
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

internal class ApiClientImpl(
    context: Context,
    baseUrl: String,
    private val authorizationProvider: AuthorizationProvider
) : ApiClient {
    private val retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(createHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    private fun createHttpClient(context: Context): OkHttpClient {
        val cacheSize = 10 * 1024 * 1024L // 10 MB
        val cache = Cache(context.cacheDir, cacheSize)
        val builder = OkHttpClient.Builder()
        builder.cache(cache)
        builder.addInterceptor(AuthInterceptor(authorizationProvider))
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(interceptor)
            builder.addNetworkInterceptor(interceptor)
        }
        return builder.build()
    }

    override fun <S> createService(clazz: Class<S>): S {
        return retrofit.create(clazz)
    }

    override fun <T> responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>
    ): Converter<ResponseBody, T> {
        return retrofit.responseBodyConverter(type, annotations)
    }
}