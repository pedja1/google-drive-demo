package com.demo.drive.data.network

import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

internal interface ApiClient {
    fun <S> createService(clazz: Class<S>): S
    fun <T> responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>
    ): Converter<ResponseBody, T>
}