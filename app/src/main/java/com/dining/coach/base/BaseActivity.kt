package com.dining.coach.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dining.coach.ui.main.MainActivity
import com.dining.coach.ui.splash.SplashActivity
import com.dining.coach.util.view.GridLayoutManagerWrapper
import com.dining.coach.util.view.LinearLayoutManagerWrapper
import com.dining.coach.util.view.OSController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author 강범석
 * @since 2023.05.26
 * @param resId Activity Resource ID
 */
abstract class BaseActivity<T : ViewDataBinding>(private val resId: Int): AppCompatActivity(), OnClickListener, OSController {

    private lateinit var baseViewModel: BaseViewModel
    protected lateinit var bind: T

    abstract fun createActivity(): BaseViewModel

    /**************************************************************************************************
     * LIFE CYCLE
     **************************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, resId)
        baseViewModel = createActivity()
        initBaseUI()
    }

    /**************************************************************************************************
     * INIT UI
     **************************************************************************************************/
    private fun initBaseUI() {
        when (this) {
            is SplashActivity -> {
                setPrimaryOSBars(this)
            }

            else -> {
                setOsBarsColor(this, OSController.IconColor.WHITE)
            }
        }
    }

    /**************************************************************************************************
     * EVENT
     **************************************************************************************************/

    fun setOnClickListeners(vararg views: View) {
        try {
            views.forEach { view ->
                view.setOnClickListener(this)
            }
        }catch (e: Exception) {
            // TODO : 상호작용 오류. 재 Inflate 요망.
            CoroutineScope(Dispatchers.Main).launch {
                launch {
                    bind = DataBindingUtil.setContentView(this@BaseActivity, resId)
                }.join()

                launch {
                    setOnClickListeners(*views)
                }
            }
        }
    }

    override fun onClick(p0: View?) {

    }

    /**************************************************************************************************
     * OPTION
     **************************************************************************************************/

    /**
     * @suppress Inconsistency detected, Invalid view holder adapter positionViewHolder
     * @param list = vararg variable of RecyclerView
     * @author 강범석
     * @see
     * 삼성 Device에서 발생하는 Issue
     * LayoutManager 사용 시, 에러 발생
     * supportsPredictiveItemAnimations()와 삼성 핸드폰 애니메이션이 충돌하여 발생하는 현상
     * 해당 상황 솔루션으로 Method 제공합니다.
     */
    protected fun wrapHorizontalRecyclerView(vararg list: RecyclerView) {
        list.forEach { view ->
            val manager = LinearLayoutManagerWrapper(this, LinearLayoutManager.HORIZONTAL, false)
            view.layoutManager = manager
        }
    }

    protected fun wrapVerticalRecyclerView(vararg list: RecyclerView) {
        list.forEach { view ->
            val manager = LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false)
            view.layoutManager = manager
        }
    }

    protected fun wrapGridRecyclerView(list: RecyclerView, spanCount: Int) {
        val manager = GridLayoutManagerWrapper(this, spanCount)
        list.layoutManager = manager
    }

    protected inline fun <reified T : AppCompatActivity> gotoActivityWithClear() {
        val intent = Intent(this, T::class.java)
        overridePendingTransition(0, 0)
        startActivity(intent)
        finish()
    }
}