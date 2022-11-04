package com.demo.drive.data.di

import com.demo.drive.core.repository.DriveRepository
import com.demo.drive.data.repository.DriveRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DriveRepositoryModule {

    @Binds
    @Singleton
    internal abstract fun bindDriveRepository(driveRepositoryImpl: DriveRepositoryImpl): DriveRepository
}