package com.appmovil.proyecto2.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appmovil.proyecto2.R
import com.appmovil.proyecto2.model.Articulo
import com.appmovil.proyecto2.view.viewholder.ProductoViewHolder

class ProductosAdapter(private val context: Context, private val productList: List<Articulo>) :
    RecyclerView.Adapter<ProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_inventory, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val product = productList[position]

        holder.textProductName.text = product.nombre
        holder.textProductId.text = "ID: ${product.codigo}"
        holder.textProductPrice.text = "\$${String.format("%,d", product.precio)}"
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}

