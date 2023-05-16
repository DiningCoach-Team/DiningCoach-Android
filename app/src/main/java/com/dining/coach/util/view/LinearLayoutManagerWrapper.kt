package com.dining.coach.util.view

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dining.coach.util.debug.ERROR
import com.dining.coach.util.debug.name

class LinearLayoutManagerWrapper: LinearLayoutManager {
    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        }catch (e: IndexOutOfBoundsException) {
            ERROR(this@LinearLayoutManagerWrapper.name, "slide is so fast")
        }
    }

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }

}