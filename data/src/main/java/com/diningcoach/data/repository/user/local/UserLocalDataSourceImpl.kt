package com.diningcoach.data.repository.user.local

import com.diningcoach.data.db.DCPreference
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    private val preferences: DCPreference
): UserLocalDataSource {
    override fun checkIsLogin(): Boolean {
        preferences.getString("PREF_ID", "")
        return
    }
}