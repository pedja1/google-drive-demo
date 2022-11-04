package com.demo.drive.data.usecase.drive

import android.content.Context
import com.demo.drive.core.model.Result
import com.demo.drive.core.usecase.drive.LogoutUseCase
import com.demo.drive.core.network.AuthorizationProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class LogoutUseCaseImpl @Inject constructor(
    private val authorizationProvider: AuthorizationProvider,
    @ApplicationContext context: Context
) : LogoutUseCase {

    private val googleSignInClient by lazy {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .build()
        GoogleSignIn.getClient(context, googleSignInOptions)
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            authorizationProvider.invalidate()
            googleSignInClient.signOut().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}