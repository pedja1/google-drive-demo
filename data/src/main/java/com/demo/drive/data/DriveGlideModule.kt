package com.demo.drive.data

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.*
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import com.bumptech.glide.module.AppGlideModule
import com.demo.drive.core.network.AuthorizationProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import com.demo.drive.core.model.Result


@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface GlideEntryPoint {
    fun getAuthorisationProvider(): AuthorizationProvider
}

@GlideModule
class DriveGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val authorizationProvider =
            EntryPointAccessors.fromApplication(context, GlideEntryPoint::class.java)
                .getAuthorisationProvider()
        registry.replace(String::class.java, InputStream::class.java, DriveAuthUrlLoader.Factory(authorizationProvider))
    }
}

internal class DriveAuthUrlLoader(
    modelLoader: ModelLoader<GlideUrl, InputStream>,
    private val authorizationProvider: AuthorizationProvider
) : BaseGlideUrlLoader<String>(modelLoader) {


    override fun handles(model: String): Boolean {
        return true
    }

    override fun getUrl(model: String?, width: Int, height: Int, options: Options?): String? {
        return model
    }

    override fun getHeaders(model: String?, width: Int, height: Int, options: Options?): Headers {
        return LazyHeaders.Builder().apply {
            //getHeaders is execute on background thread so we can use runBlocking here
            runBlocking {
                //try cast result tu Success because we cant handle error here (error is ignored)
                (authorizationProvider.getAuthorizationHeader() as? Result.Success)?.let {
                    addHeader("Authorization", it.data)
                }
            }
        }.build()
    }

    internal class Factory(private val authorizationProvider: AuthorizationProvider) :
        ModelLoaderFactory<String, InputStream> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, InputStream> {
            return DriveAuthUrlLoader(
                multiFactory.build(
                    GlideUrl::class.java,
                    InputStream::class.java
                ),
                authorizationProvider
            )
        }

        override fun teardown() {}
    }

}