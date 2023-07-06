package com.example.nailexpress.di

import android.app.Application
import com.example.nailexpress.datasource.AppEvent2
import com.example.nailexpress.datasource.local.SharePrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providesSharePre(application: Application): SharePrefs =
        SharePrefs.getInstance(application)

    @Singleton
    @Provides
    fun provideAppEvent(): AppEvent2 = AppEvent2()
}