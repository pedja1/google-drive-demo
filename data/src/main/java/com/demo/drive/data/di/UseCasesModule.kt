package com.demo.drive.data.di

import com.demo.drive.core.usecase.drive.DownloadFileUseCase
import com.demo.drive.core.usecase.drive.ListFilesUseCase
import com.demo.drive.core.usecase.drive.LogoutUseCase
import com.demo.drive.data.usecase.drive.DownloadFileUseCaseImpl
import com.demo.drive.data.usecase.drive.ListFilesUseCaseImpl
import com.demo.drive.data.usecase.drive.LogoutUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCasesModule {

    @Binds
    @Singleton
    internal abstract fun bindListFilesUseCase(listFilesUseCaseImpl: ListFilesUseCaseImpl): ListFilesUseCase

    @Binds
    @Singleton
    internal abstract fun bindLogoutUseCase(logoutUseCaseImpl: LogoutUseCaseImpl): LogoutUseCase

    @Binds
    @Singleton
    internal abstract fun bindDownloadFileUseCase(downloadFileUseCaseImpl: DownloadFileUseCaseImpl): DownloadFileUseCase

}