package com.demo.drive.data.network.impl

import com.demo.drive.core.network.AuthorizationProvider
import okhttp3.*
import javax.inject.Inject
import com.demo.drive.core.model.Result
import kotlinx.coroutines.runBlocking

internal class AuthInterceptor @Inject constructor(private val authorizationProvider: AuthorizationProvider) : Interceptor, Authenticator {
    override fun intercept(chain: Interceptor.Chain): Response {
        //intercept is called from background thread, runBlocking is ok here
        val authHeader = (runBlocking { authorizationProvider.getAuthorizationHeader() } as? Result.Success)?.data
        val builder = chain.request().newBuilder()
        if(authHeader != null) {
            builder.addHeader("Authorization", authHeader)
        }
        return chain.proceed(builder.build())
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        authorizationProvider.invalidate()
        //authenticate is called from background thread, runBlocking is ok here
        val authHeader = (runBlocking { authorizationProvider.getAuthorizationHeader() } as? Result.Success)?.data
        if(authHeader != null) {
            return response.request.newBuilder().addHeader("Authorization", authHeader).build()
        }
        return null
    }
}