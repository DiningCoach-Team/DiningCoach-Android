package com.dining.coach.util.view.snackbar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.dining.coach.databinding.SnackbarOneButtonBinding
import com.dining.coach.util.func.dp
import com.google.android.material.snackbar.Snackbar

class DCSnackbar(
    private val view: View,
    private val message: String,
    private val type: Type,
    private val action: (() -> Unit)? = null,
    private val anchorView: View? = null
) {
    private val snackbar = Snackbar.make(view, message, 0).apply {
        duration = 3000
        anchorView = anchorView
    }

    private val layout = snackbar.view as Snackbar.SnackbarLayout

    @get:SuppressLint("InternalInsetResource", "DiscouragedApi")
    private val navigationBarHeight get() = view.context.resources.getDimensionPixelOffset(
        view.context.resources.getIdentifier(
            "navigation_bar_height",
            "dimen",
            "android"
        )
    )

    companion object {
        fun make(view: View, message: String, type: Type, action: (() -> Unit)?, anchorView: View?) = DCSnackbar(view, message, type, action, anchorView)
    }

    init {
        resetView()
        initViewForType()
    }

    private fun resetView() {
        layout.removeAllViews()
        snackbar.anchorView = anchorView
    }

    private fun initViewForType() {
        layout.run {
            setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.transparent))

            val binding = when(type) {
                Type.EXPLAIN_FOR_ALARM -> provideViewOfType<SnackbarOneButtonBinding>()
            }

            addView(binding.root)
        }
    }

    fun show() {
        snackbar.dismiss()
        snackbar.show()
    }

    fun dismiss() {
        snackbar.dismiss()
    }

    private inline fun <reified T : ViewBinding> provideViewOfType(): T {
        return when (type) {
            Type.EXPLAIN_FOR_ALARM -> {
                layout.setPadding(55.dp, 0, 55.dp, 24.dp)
                val view = SnackbarOneButtonBinding.inflate(LayoutInflater.from(view.context), null, false)
                view.initData()
                view
            }
        } as T
    }

    private fun SnackbarOneButtonBinding.initData() {
        txtExplain.text = message
        btnAction.setOnClickListener {
            action?.invoke()
        }
    }

    enum class Type {
        EXPLAIN_FOR_ALARM
    }
}