package com.dining.coach.base

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
 * @see
 * DCApplcation activity's base class
 * If you want base function in activity, recommend that create here.
 */
abstract class BaseActivity<T : ViewDataBinding>(private val resId: Int): AppCompatActivity(), OnClickListener, OSController {

    private lateinit var baseViewModel: BaseViewModel
    protected lateinit var bind: T

    private val etRectList = mutableListOf<Rect>()

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

        detectEditText()
    }

    /**
     *  @since 2023.06.07
     *  @author 강범석
     *  @param view for start to recursive function
     *  @see
     *  When view is onInflateFinished, detect EditText in Binding.root
     *  for there's rect values.
     */
    private fun detectEditText(view: View = bind.root) {
        try {
            if (view is ViewGroup) {
                view.children.forEach {
                    detectEditText(it)
                }
            } else if (view is EditText) {
                val rect = Rect()
                view.getGlobalVisibleRect(rect)
                etRectList.add(rect)
            }
        } catch (e: Exception) {
            return
        }
    }

    /**************************************************************************************************
     * EVENT
     **************************************************************************************************/

    protected fun setOnClickListeners(vararg views: View) {
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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.run {
            adjustFocusEditText(this,
                isNotEditText = {
                    hideKeyboard()
                }
            )
        }

        return super.dispatchTouchEvent(ev)
    }

    /**
     * @since 2023.06.07
     * @author 강범석
     * @param ev dispatchTouchEvent()'s MotionEvent
     * @param isNotEditText high-order-function for not click editText
     * @see
     * Compare the Rect values of editText to determine whether the user clicks on EditText or not.
     */
    private fun adjustFocusEditText(ev: MotionEvent, isNotEditText: () -> Unit) {
        etRectList.forEach { rect ->
            if (rect.contains(ev.rawX.toInt(), ev.rawY.toInt())) return@forEach
        }

        isNotEditText.invoke()
    }

    /**
     *  @author 강범석
     *  @since 2023.06.07
     *  @see
     *  Clear Focus with Hide keyboard in device
     */
    private fun hideKeyboard() {
        currentFocus?.run {
            clearFocus()
            WindowCompat.getInsetsController(window, this).hide(WindowInsetsCompat.Type.ime())
        }
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