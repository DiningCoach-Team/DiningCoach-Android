package com.diningcoach.domain.repository

interface UserRepository {
    fun checkIsLogin(): Boolean
}