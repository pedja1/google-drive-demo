package com.demo.drive.data.di

import android.content.Context
import com.demo.drive.data.Constants
import com.demo.drive.data.network.ApiClient
import com.demo.drive.core.network.AuthorizationProvider
import com.demo.drive.data.network.DriveApi
import com.demo.drive.data.network.impl.ApiClientImpl
import com.demo.drive.data.network.impl.DriveApiImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    companion object {
        @Provides
        @Singleton
        internal fun provideApiClient(
            @ApplicationContext context: Context,
            authorizationProvider: AuthorizationProvider
        ): ApiClient {
            return ApiClientImpl(
                context,
                Constants.DRIVE_API_BASE_URL,
                authorizationProvider
            )
        }
    }

    @Binds
    @Singleton
    internal abstract fun bindDriveApi(driveApiImpl: DriveApiImpl): DriveApi
}