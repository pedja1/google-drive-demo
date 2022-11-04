package com.demo.drive.core.usecase.drive

import com.demo.drive.core.model.Result

interface LogoutUseCase {
    suspend fun logout(): Result<Unit>
}