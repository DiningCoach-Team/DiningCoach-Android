package com.diningcoach.data.di.module.repository

import com.diningcoach.data.repository.gallery.GalleryRepositoryImpl
import com.diningcoach.data.repository.user.UserRepositoryImpl
import com.diningcoach.domain.repository.GalleryRepository
import com.diningcoach.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindsUserRepository(implements: UserRepositoryImpl): UserRepository

    @Singleton
    @Binds
    fun bindsGalleryRepository(implements: GalleryRepositoryImpl): GalleryRepository
}