package com.diningcoach.data.di.module.repository

import com.diningcoach.data.repository.user.local.UserLocalDataSource
import com.diningcoach.data.repository.user.local.UserLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LocalDataSourceModule {

    @Singleton
    @Binds
    fun bindsUserLocalDataSource(implements: UserLocalDataSourceImpl): UserLocalDataSource
}