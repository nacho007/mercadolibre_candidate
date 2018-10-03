package com.mercadolibre.candidate.interfaces

import com.mercadolibre.candidate.model.ProductItem

interface OnProductItemClickListener {
    fun onProductItemClick(productItem: ProductItem)
}