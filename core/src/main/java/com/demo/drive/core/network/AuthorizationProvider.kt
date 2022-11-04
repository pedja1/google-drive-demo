package com.demo.drive.core.network

import com.demo.drive.core.model.Result

interface AuthorizationProvider {
    /**
     * Should return value for Authorization header
     */
    suspend fun getAuthorizationHeader(): Result<String>

    /**
     * Invalidate token
     */
    fun invalidate()
}