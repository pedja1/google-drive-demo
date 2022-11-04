package com.demo.drive.data.di

import com.demo.drive.core.network.AuthorizationProvider
import com.demo.drive.data.network.impl.AuthorizationProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthorizationProviderModule {
    @Binds
    @Singleton
    internal abstract fun bindAuthorizationProvider(authorizationProviderImpl: AuthorizationProviderImpl): AuthorizationProvider
}