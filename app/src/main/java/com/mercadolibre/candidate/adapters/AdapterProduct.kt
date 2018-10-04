package com.mercadolibre.candidate.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mercadolibre.candidate.R
import com.mercadolibre.candidate.adapters.helpers.ListItemBackgroundBuilder
import com.mercadolibre.candidate.interfaces.OnProductItemClickListener
import com.mercadolibre.candidate.model.ProductItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_product_item.view.*

class AdapterProduct(var productItem: ArrayList<ProductItem>, var listener: OnProductItemClickListener) : RecyclerView.Adapter<AdapterProduct.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_product_item, parent, false)
        return ProductViewHolder(v)
    }


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.setItem(productItem[position])
        holder.bind(productItem[position], listener)
    }


    override fun getItemCount(): Int {
        return productItem.size
    }

    class ProductViewHolder(val v: View) : RecyclerView.ViewHolder(v) {

        private val context: Context = v.context

        fun setItem(productItem: ProductItem) {
            v.adapter_product_item_text_view_title.text = productItem.title
            v.adapter_product_item_text_view_price.text = context.getString(R.string.product_item_price, productItem.currencyId, productItem.price)
            v.adapter_product_item_text_view_condition.text = productItem.condition

            Picasso.get().load(productItem.thumbnail).into(v.adapter_product_item_image_view)
            ListItemBackgroundBuilder.instance.setBackgroundInAdapter(productItem, v.adapter_product_item_constraintlayout)
        }

        fun bind(productItem: ProductItem, listener: OnProductItemClickListener) {
            itemView.setOnClickListener {
                listener.onProductItemClick(productItem)
            }
        }

    }

}