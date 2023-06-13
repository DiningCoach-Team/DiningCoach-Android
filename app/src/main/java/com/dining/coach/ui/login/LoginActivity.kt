package com.dining.coach.ui.login

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.dining.coach.R
import com.dining.coach.base.BaseActivity
import com.dining.coach.base.BaseViewModel
import com.dining.coach.databinding.ActivityLoginBinding
import com.dining.coach.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private lateinit var googleSignInClient: GoogleSignInClient
    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser

    private val viewModel: LoginViewModel by viewModels()

    override fun createActivity(): BaseViewModel {
        initKakaoSignInSetting()
        initGoogleSignInSetting()
        setOnClickListeners()

        bind.vm = viewModel
        return viewModel
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

    private fun setOnClickListeners() {
        bind.btnKakao.setOnClickListener {
            signInWithKakao()
        }

        bind.btnGoogle.setOnClickListener {
            signInWithGoogle()
        }
    }

    override fun onResume() {
        super.onResume()
//        autoSignInWithKakao()
//        autoSignInWithGoogle()
    }


    /*
    * 카카오 소셜 로그인
    */

    private fun signInWithKakao() {
        // 카카오 계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오 계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { tokenInfo, error ->
            if (error != null) {
                Log.e("signInWithKakao - 1", "카카오 계정으로 로그인 실패", error)
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
                Log.i("signInWithKakao - 2", "카카오 계정으로 로그인 성공 ${tokenInfo.accessToken}")
            }
        }

        // 로그인을 통해 발급 받은 토큰이 있는지 확인
//        checkAuthenticatedToken(callback)

        // 해당 기기에 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오 계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { tokenInfo, error ->
                if (error != null) {
                    Log.e("signInWithKakao - 3", "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오 계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오 계정이 없는 경우, 카카오 계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (tokenInfo != null) {
                    onSignInSuccess()
                    Log.i("signInWithKakao - 4", "카카오톡으로 로그인 성공 : ${tokenInfo.accessToken}")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun checkAuthenticatedToken(callback: (OAuthToken?, Throwable?) -> Unit) {
        if (AuthApiClient.instance.hasToken()) { // hasToken()의 결과가 true라도 현재 사용자가 로그인 상태임을 보장하지
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        // 로그인 필요
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else {
                        // 기타 에러
                        Log.e("checkAuthenticatedToken - 1", "카카오 로그인 실패", error)
                    }
                } else if (tokenInfo != null) {
                    Log.i(
                        "checkAuthenticatedToken - 2", "토큰 정보 보기 성공" +
                                "\n회원번호: ${tokenInfo.id}" +
                                "\n만료시간: ${tokenInfo.expiresIn} 초"
                    )
                } else {
                    // 토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                }
            }
        } else {
            //로그인 필요
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
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
                    if(isKakaoTalkLoginAvailable(this@LoginActivity))
                        loginWithKakaoTalk(this@LoginActivity, callback = callback)
                    else
                        loginWithKakaoAccount(this@LoginActivity, callback = callback)
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


    /*
    * 구글 소셜 로그인
    */
    
    private fun signInWithGoogle() {
        val signIntent = googleSignInClient.signInIntent
        googleSignInResultLauncher.launch(signIntent)
    }

    private val googleSignInResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.e(javaClass.simpleName, "result.resultCode : ${result.resultCode}!!!")
            Log.e(javaClass.simpleName, "RESULT_OK : $RESULT_OK")

            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                Log.e(javaClass.simpleName, "result.data : ${result.data}")
                Log.e(javaClass.simpleName, "task : $task")
                handleGoogleSignInResult(task)
            } else {
                Log.e(javaClass.simpleName, "googleSignInResultLauncher: error")
            }
        }

    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        Log.e(javaClass.simpleName, "handleGoogleSignInResult : 들어옴!!!")
        Log.e(javaClass.simpleName, "task : $task!!!")

        if (task.isSuccessful) {
            val account = task.result
            val email = account?.email
            val familyName = account?.familyName
            if (account != null && email != null && familyName != null) {
                firebaseUserAuthWithGoogle(account)
            }
        }
    }

    private fun firebaseUserAuthWithGoogle(account: GoogleSignInAccount) {
        Log.e(javaClass.simpleName, "firebaseUserAuthWithGoogle : 들어옴!!!")
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                onSignInSuccess()
                Log.d(javaClass.simpleName, "firebaseUserAuthWithGoogle : 성공!!!")
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
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