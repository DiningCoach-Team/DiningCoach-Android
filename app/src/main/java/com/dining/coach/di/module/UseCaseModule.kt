package com.dining.coach.di.module

import com.diningcoach.domain.repository.UserRepository
import com.diningcoach.domain.usecase.user.CheckIsLoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
 object UseCaseModule {

    @Singleton
    @Provides
    fun provideCheckIsLoginUseCase(repository: UserRepository): CheckIsLoginUseCase = CheckIsLoginUseCase(repository)
}