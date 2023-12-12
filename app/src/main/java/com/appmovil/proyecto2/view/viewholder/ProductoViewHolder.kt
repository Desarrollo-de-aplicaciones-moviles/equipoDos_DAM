package com.appmovil.proyecto2.view.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appmovil.proyecto2.R

class ProductoViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textProductName: TextView = itemView.findViewById(R.id.tvItemName)
    val textProductQty: TextView = itemView.findViewById(R.id.tvItemQty)
    val textProductPrice: TextView = itemView.findViewById(R.id.tvItemPrice)
}