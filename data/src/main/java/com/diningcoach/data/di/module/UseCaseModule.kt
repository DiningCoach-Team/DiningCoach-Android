package com.diningcoach.data.di.module

import com.diningcoach.domain.repository.GalleryRepository
import com.diningcoach.domain.repository.UserRepository
import com.diningcoach.domain.usecase.gallery.GalleryImageFetchUseCase
import com.diningcoach.domain.usecase.user.CheckIsLoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideCheckIsLoginUseCase(repository: UserRepository): CheckIsLoginUseCase =
        CheckIsLoginUseCase(repository)

    @Provides
    @Singleton
    fun provideGalleryImageFetchUseCase(repository: GalleryRepository): GalleryImageFetchUseCase =
        GalleryImageFetchUseCase(repository)

}