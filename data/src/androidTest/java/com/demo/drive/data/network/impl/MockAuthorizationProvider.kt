package com.demo.drive.data.network.impl

import com.demo.drive.core.model.Result
import com.demo.drive.core.network.AuthorizationProvider

class MockAuthorizationProvider : AuthorizationProvider {
    override suspend fun getAuthorizationHeader(): Result<String> {
        return Result.Success("mock token")
    }

    override fun invalidate() {
        //does nothing
    }
}