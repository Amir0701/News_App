package com.example.newsapp.presentation.view

import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LastItemMarginRecyclerDecorator: RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val margin = 16f
        val space = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            margin,
            view.resources.displayMetrics
        ).toInt()

        val itemPosition = parent.getChildAdapterPosition(view)
        if(itemPosition == parent.adapter!!.itemCount - 1){
            outRect.bottom = space
        }
    }
}