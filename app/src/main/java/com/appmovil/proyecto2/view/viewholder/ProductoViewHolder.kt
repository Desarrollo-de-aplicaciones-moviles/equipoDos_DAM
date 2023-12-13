package com.appmovil.proyecto2.view.viewholder

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.findNavController
import com.appmovil.proyecto2.R
import com.appmovil.proyecto2.model.Articulo

class ProductoViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textProductName: TextView = itemView.findViewById(R.id.tvNombreProducto)
    val textProductId: TextView = itemView.findViewById(R.id.tvIdProducto)
    val textProductPrice: TextView = itemView.findViewById(R.id.tvPrecioProducto)
    val cvChallenges: CardView = itemView.findViewById(R.id.cvChallenges)

    init {
        cvChallenges.setOnClickListener {
            val bundle = Bundle()
            val idString = textProductId.text.toString()
            val idNumber = idString.replace("ID: ", "").trim().toInt()
            val priceString = textProductPrice.text.toString()
            val priceNumber = priceString.replace("$", "").replace(",", "").trim().toInt()

            bundle.putSerializable("articulo", Articulo(idNumber, textProductName.text.toString(), priceNumber, 1))
            it.findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
        }
    }
}