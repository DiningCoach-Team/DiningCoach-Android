package com.dining.coach.di.module

import com.dining.coach.repo.user.UserRepository
import com.dining.coach.repo.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {
    @Singleton
    @Binds
    fun bindsUserRepository(repositoryImpl: UserRepositoryImpl): UserRepository
}