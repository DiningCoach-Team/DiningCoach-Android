package com.dining.coach.util.view

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import com.dining.coach.util.debug.DEBUG
import com.dining.coach.util.debug.name
import com.google.android.material.textfield.TextInputEditText

/**
 * @since 2023.06.07
 * @author 강범석
 * @constructor Context Object, Attribute from xml
 * @property onKeyPreIme If the user presses somewhere else, adjust the focus state.
 */
class DCMaterialEditText: TextInputEditText, OnFocusChangeListener {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        onFocusChangeListener = this
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                clearFocus()
            }
        }

        return super.onKeyPreIme(keyCode, event)
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        DEBUG(this@DCMaterialEditText.name, "status : $p1")
    }
}