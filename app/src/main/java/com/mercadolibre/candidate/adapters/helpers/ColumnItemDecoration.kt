package com.mercadolibre.candidate.adapters.helpers

import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

class ColumnItemDecoration(private var padding: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutParams = view.layoutParams as GridLayoutManager.LayoutParams
        val gridLayoutManager = parent.layoutManager as GridLayoutManager?
        val position = parent.getChildViewHolder(view).adapterPosition
        val spanSize = gridLayoutManager!!.spanSizeLookup.getSpanSize(position).toFloat()
        val totalSpanSize = gridLayoutManager.spanCount.toFloat()

        val n = totalSpanSize / spanSize // num columns
        val c = layoutParams.spanIndex / spanSize // column index

        val leftPadding = padding * ((n - c) / n)
        val rightPadding = padding * ((c + 1) / n)

        outRect.left = leftPadding.toInt()
        outRect.right = rightPadding.toInt()
        outRect.bottom = padding
    }
}