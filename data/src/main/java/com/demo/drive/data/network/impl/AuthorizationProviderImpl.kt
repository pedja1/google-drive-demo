package com.demo.drive.data.network.impl

import android.content.Context
import com.demo.drive.core.network.AuthorizationProvider
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.Scopes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import com.demo.drive.core.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class AuthorizationProviderImpl @Inject constructor(@ApplicationContext private val context: Context) :
    AuthorizationProvider {
    private var tokenCache: String? = null

    override suspend fun getAuthorizationHeader(): Result<String> {
        //GoogleAuthUtil.getToken is blocking network function, execute it on IO thread
        return withContext(Dispatchers.IO) {
            val currentToken = tokenCache
            if (currentToken != null) {
                createHeaderValueResult(currentToken)
            } else {
                GoogleSignIn.getLastSignedInAccount(context)?.account?.let { account ->
                    try {
                        val token = GoogleAuthUtil.getToken(
                            context,
                            account,
                            "oauth2:${Scopes.DRIVE_FULL}"
                        )
                        tokenCache = token
                        createHeaderValueResult(token)
                    } catch (e: Exception) {
                        Result.Error(e)
                    }
                } ?: Result.Error(Exception("User not logged in"))
            }
        }
    }

    override fun invalidate() {
        tokenCache?.let {
            GoogleAuthUtil.clearToken(context, it)
        }
        tokenCache = null
    }

    private fun createHeaderValueResult(token: String) = Result.Success("Bearer $token")
}