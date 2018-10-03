package com.mercadolibre.candidate.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mercadolibre.candidate.R
import com.mercadolibre.candidate.interfaces.OnProductItemClickListener
import com.mercadolibre.candidate.model.ProductItem

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

        val context: Context = v.context

        fun setItem(productItem: ProductItem) {

//            v.row_bank_textview_name.text = bank.name
//
//            GlideHelper.getInstance().setImage(context, v.row_bank_imageview_flag, BANKS_IMAGE_BASE_URL + bank.image,
//                    GlideHelper.PlaceHolderType.CAMERA, false)
//
//            v.row_bank_imageview_tick.visibility = if (bank.selected) {
//                View.VISIBLE
//            } else {
//                View.GONE
//            }
//
//            ListItemBackgroundBuilder.getInstance(context).setBackgroundInadapter(bank, v.row_bank_relativelayout)

        }

        fun bind(productItem: ProductItem, listener: OnProductItemClickListener) {
            itemView.setOnClickListener {
                listener.onProductItemClick(productItem)
            }
        }

    }

}