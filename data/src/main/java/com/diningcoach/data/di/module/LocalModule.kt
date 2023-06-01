package com.diningcoach.data.di.module

import android.content.Context
import com.diningcoach.data.db.DCPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Provides
    @Singleton
    fun provideDCPreference(@ApplicationContext context: Context): DCPreference {
        return DCPreference(context)
    }
}