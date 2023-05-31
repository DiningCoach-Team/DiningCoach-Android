package com.dining.coach.repo.user

import com.dining.coach.util.debug.DEBUG
import com.dining.coach.util.debug.name
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(

) : UserRepository {
    private var chkTime = false
    private var chkIsLogin = false

    override fun getIsLogin(): Boolean {
        val startTime = System.currentTimeMillis()

        CoroutineScope(Dispatchers.Default).launch {
            CoroutineScope(Dispatchers.Default).launch {

            }

            while(true) {
                val pastTime = startTime - System.currentTimeMillis()

                DEBUG(this@UserRepositoryImpl.name, "pastTime : $pastTime")

                if (pastTime >= 1000) break
            }

            chkTime = true
            DEBUG(this@UserRepositoryImpl.name, "finish func")
        }

        return false
    }
}