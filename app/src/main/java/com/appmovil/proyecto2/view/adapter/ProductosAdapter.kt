package com.appmovil.proyecto2.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.appmovil.proyecto2.R
import com.appmovil.proyecto2.model.Articulo
import com.appmovil.proyecto2.view.viewholder.ProductoViewHolder
import java.text.NumberFormat
import java.util.*

class ProductosAdapter(private val context: Context, private val productList: List<Articulo>, private val navController: NavController) :
    RecyclerView.Adapter<ProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_inventory, parent, false)
        return ProductoViewHolder(view, navController)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val product = productList[position]

        holder.textProductName.text = product.nombre
        holder.textProductQty.text = "Cant: ${product.cantidad}"

        // Convertir el precio a Double antes de formatear
        val priceAsDouble = product.precio.toDouble()

        // Formatear el precio con separadores de miles y dos decimales
        val formattedPrice = formatPrice(priceAsDouble)
        holder.textProductPrice.text = "$formattedPrice"

        holder.setItemInventory(product)

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    private fun formatPrice(price: Double): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale("es", "ES"))
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2

        return "$" + numberFormat.format(price)
    }
}


