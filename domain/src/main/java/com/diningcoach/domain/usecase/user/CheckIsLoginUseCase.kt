package com.diningcoach.domain.usecase.user

import com.diningcoach.domain.common.Resource
import com.diningcoach.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class CheckIsLoginUseCase (
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val isLogin = repository.checkIsLogin()
            emit(Resource.Success(isLogin))
        }catch (e: IOException) {
            emit(Resource.Error("Check your internet connection !!"))
        }
    }
}