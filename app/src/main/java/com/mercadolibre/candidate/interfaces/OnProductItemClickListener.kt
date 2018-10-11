package com.mercadolibre.candidate.interfaces

import android.view.View
import com.mercadolibre.candidate.model.ProductItem

interface OnProductItemClickListener {
    fun onProductItemClick(productItem: ProductItem?, view: View)
}