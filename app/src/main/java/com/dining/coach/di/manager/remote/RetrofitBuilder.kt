package com.dining.coach.di.manager.remote

import com.dining.coach.data.remote.RemoteSource

class RetrofitBuilder {
    private var _source: RemoteSource? = null
    val source get() = _source

    // TODO : Call 객체가 클라이언트의 책임으로 인해 연결 실패 했을 경우 담는 Factory 만들기
    fun createFactory(): RetrofitBuilder? {
        return null
    }
}