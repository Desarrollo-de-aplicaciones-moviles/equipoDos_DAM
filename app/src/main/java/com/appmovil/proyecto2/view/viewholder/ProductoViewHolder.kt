package com.appmovil.proyecto2.view.viewholder

import android.os.Bundle
import android.view.View

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.appmovil.proyecto2.R
import com.appmovil.proyecto2.model.Articulo

class ProductoViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textProductName: TextView = itemView.findViewById(R.id.tvNombreProducto)
    val textProductId: TextView = itemView.findViewById(R.id.tvIdProducto)
    val textProductPrice: TextView = itemView.findViewById(R.id.tvPrecioProducto)

    fun bind(articulo: Articulo) {
        textProductName.text = articulo.nombre
        textProductId.text = articulo.codigo.toString()
        textProductPrice.text = articulo.precio.toString()

        itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("articulo", articulo)
            }
            it.findNavController().navigate(R.id.action_homeFragment_to_detailsFragment)
        }
    }
}