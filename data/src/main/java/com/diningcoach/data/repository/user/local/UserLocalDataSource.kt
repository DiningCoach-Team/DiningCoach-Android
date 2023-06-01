package com.diningcoach.data.repository.user.local

interface UserLocalDataSource {
    fun checkIsLogin(): Boolean
}