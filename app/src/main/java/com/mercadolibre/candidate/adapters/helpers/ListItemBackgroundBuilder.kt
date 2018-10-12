package com.mercadolibre.candidate.adapters.helpers

import android.view.View
import com.mercadolibre.candidate.R
import com.mercadolibre.candidate.constants.ALONE
import com.mercadolibre.candidate.constants.BOTTOM
import com.mercadolibre.candidate.constants.MIDDLE
import com.mercadolibre.candidate.constants.TOP
import com.mercadolibre.candidate.model.Base

class ListItemBackgroundBuilder private constructor() {

    private var itemTopBackground: Int = 0
    private var itemMiddleBackground: Int = 0
    private var itemBottomBackground: Int = 0
    private var itemAloneBackground: Int = 0

    init {
        itemTopBackground = R.drawable.shape_white_balloon_top_selector
        itemMiddleBackground = R.drawable.shape_white_balloon_middle_rect_selector
        itemBottomBackground = R.drawable.shape_white_balloon_bottom_selector
        itemAloneBackground = R.drawable.shape_white_balloon_alone_selector
    }

    private object Holder {
        val INSTANCE = ListItemBackgroundBuilder()
    }

    companion object {
        val instance: ListItemBackgroundBuilder by lazy { Holder.INSTANCE }
    }

    fun setBackgroundInAdapter(base: Base?, view: View) {
        when (base?.background) {
            TOP -> view.setBackgroundResource(itemTopBackground)
            MIDDLE -> view.setBackgroundResource(itemMiddleBackground)
            BOTTOM -> view.setBackgroundResource(itemBottomBackground)
            ALONE -> view.setBackgroundResource(itemAloneBackground)
        }
    }

    fun assignPortraitBackgroundPositions(arrayList: ArrayList<*>?) {
        when {
            arrayList == null -> return
            arrayList.size == 1 -> (arrayList[0] as Base).background = ALONE
            arrayList.size == 2 -> {
                (arrayList[0] as Base).background = TOP
                (arrayList[1] as Base).background = BOTTOM
            }
            arrayList.size > 2 -> for (i in arrayList.indices) {
                when {
                    i == 0 -> (arrayList[i] as Base).background = TOP
                    i + 1 == arrayList.size -> (arrayList[i] as Base).background = BOTTOM
                    else -> (arrayList[i] as Base).background = MIDDLE
                }

            }
        }
    }

    fun assignLandscapeBackgroundPositions(arrayList: ArrayList<*>?) {
        when {
            arrayList == null -> return
            arrayList.size > 0 -> for (i in arrayList.indices) {
                (arrayList[i] as Base).background = ALONE
            }
        }
    }

}