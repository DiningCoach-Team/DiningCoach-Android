package com.dining.coach.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dining.coach.R
import com.dining.coach.base.BaseActivity
import com.dining.coach.base.BaseViewModel
import com.dining.coach.databinding.ActivitySplashBinding
import com.dining.coach.ui.login.LoginActivity
import com.dining.coach.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity: BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private lateinit var googleSignInClient: GoogleSignInClient
    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser
    private val viewModel: SplashViewModel by viewModels()

    override fun createActivity(): BaseViewModel {
        initKakaoSignInSetting()
        initGoogleSignInSetting()

        return viewModel
    }

    override fun onStart() {
        super.onStart()

//        autoSignInWithKakao()  // 카카오 자동 로그인
//        autoSignInWithGoogle() // 구글 자동 로그인
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (hasFocus) initViewModelCallback()
    }

    private fun initViewModelCallback() {
        viewModel.getOnMoveAction {
            lifecycleScope.launch {
                if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                    viewModel.isLogin.collectLatest { status ->
                        if (status) {
                            gotoActivityWithClear<MainActivity>()
                        } else {
                            gotoActivityWithClear<LoginActivity>()
                        }
                    }
                }
            }
        }
    }

    private fun initKakaoSignInSetting() {
        val keyHash = Utility.getKeyHash(this)
        Log.e("Key", "keyHash: $keyHash")
        /** Kakao SDK init */
        KakaoSdk.init(this, this.getString(R.string.kakao_app_key))
    }

    private fun initGoogleSignInSetting() {
        // 앱에 필요한 사용자 데이터를 요청하도록 로그인 옵션을 설정.
        // 유저 아이디와 기본 프로필 정보 요청
        val signIn = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, signIn)
    }


    private fun autoSignInWithKakao() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { tokenInfo, error ->
            if (error != null) {
                Log.e("signInKakao - 1", "카카오 계정으로 로그인 실패", error)
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() ->
                        Toast.makeText(this, getString(R.string.access_denied), Toast.LENGTH_SHORT).show()
                    error.toString() == AuthErrorCause.InvalidClient.toString() ->
                        Toast.makeText(this, getString(R.string.invalid_client), Toast.LENGTH_SHORT).show()
                    error.toString() == AuthErrorCause.InvalidGrant.toString() ->
                        Toast.makeText(this, getString(R.string.invalid_grant), Toast.LENGTH_SHORT).show()
                    error.toString() == AuthErrorCause.InvalidRequest.toString() ->
                        Toast.makeText(this, getString(R.string.invalid_request), Toast.LENGTH_SHORT).show()
                    error.toString() == AuthErrorCause.InvalidScope.toString() ->
                        Toast.makeText(this, getString(R.string.invalid_scope), Toast.LENGTH_SHORT).show()
                    error.toString() == AuthErrorCause.Misconfigured.toString() ->
                        Toast.makeText(this, getString(R.string.mis_configured), Toast.LENGTH_SHORT).show()
                    error.toString() == AuthErrorCause.ServerError.toString() ->
                        Toast.makeText(this, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                    error.toString() == AuthErrorCause.Unauthorized.toString() ->
                        Toast.makeText(this, getString(R.string.unauthorized), Toast.LENGTH_SHORT).show()
                    else ->
                        Toast.makeText(this, getString(R.string.etc_error), Toast.LENGTH_SHORT).show()
                }
            }
            else if (tokenInfo != null) {
                onSignInSuccess()
                Log.i("autoSignInWithKakao", "카카오 계정으로 자동 로그인 성공 ${tokenInfo.accessToken}")
            }
        }

        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            // 에러가 존재할 경우(로그인 정보 없음) -> 로그인 시도
            if (error != null) {
                Log.e("autoSignInWithKakao", "autoSignInKakao 사용자 정보 요청 실패", error)
                UserApiClient.instance.run {
                    if(isKakaoTalkLoginAvailable(this@SplashActivity))
                        loginWithKakaoTalk(this@SplashActivity, callback = callback)
                    else
                        loginWithKakaoAccount(this@SplashActivity, callback = callback)
                }
            }
            else if (user != null) { // 유저 정보가 존재할 경우 ->  kakaoId로 개인 API 서버에서 유저 정보 획득
                Log.i("autoSignInWithKakao", "autoSignInKakao 사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                onSignInSuccess()
            }
        }
    }

    private fun autoSignInWithGoogle() {
        user?.getIdToken(true)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken = task.result.token
                    Log.d("autoSignInWithGoogle", "idToken: $idToken")
                    onSignInSuccess()
                } else {
                    task.exception?.printStackTrace()
                }
            }
    }

    private fun onSignInSuccess() {
        val intent = MainActivity.getIntent(this)
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
    }
}