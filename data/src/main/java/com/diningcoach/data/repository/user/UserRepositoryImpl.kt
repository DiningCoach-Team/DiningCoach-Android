package com.diningcoach.data.repository.user

import com.diningcoach.data.repository.user.local.UserLocalDataSource
import com.diningcoach.data.repository.user.remote.UserRemoteDataSource
import com.diningcoach.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
): UserRepository {
    override fun checkIsLogin(): Boolean {
        return false
    }
}