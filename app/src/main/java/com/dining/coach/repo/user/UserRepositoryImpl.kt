package com.dining.coach.repo.user

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(

) : UserRepository {
    fun test() {
        val mainJob = CoroutineScope(Dispatchers.Default).launch {
            val subJob: Job = launch(start = CoroutineStart.LAZY) {
                for (idx in 0 until 20) {
                    CoroutineScope(Dispatchers.IO).launch {

                    }
                }
            }.job

            launch {

            }
        }

        mainJob.cancel()
    }

}