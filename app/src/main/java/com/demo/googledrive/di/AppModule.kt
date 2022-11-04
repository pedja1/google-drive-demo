package com.demo.googledrive.di

import android.content.Context
import com.demo.googledrive.utils.PathManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface FolderNavigationStackModule {
    companion object {
        @Provides
        @ViewModelScoped
        fun providerFolderNavigationStack(@ApplicationContext context: Context): PathManager {
            return PathManager(PathManager.getListRootFolder(context))
        }
    }
}