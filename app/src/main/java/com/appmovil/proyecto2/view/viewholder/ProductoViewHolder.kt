package com.appmovil.proyecto2.view.viewholder

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.findNavController
import com.appmovil.proyecto2.R
import com.appmovil.proyecto2.model.Articulo
import java.text.NumberFormat
import java.util.Locale

class ProductoViewHolder (itemView: View,navController: NavController) : RecyclerView.ViewHolder(itemView) {
    val textProductName: TextView = itemView.findViewById(R.id.tvItemName)
    val textProductQty: TextView = itemView.findViewById(R.id.tvItemQty)
    val textProductPrice: TextView = itemView.findViewById(R.id.tvItemPrice)
    val cvItem: CardView = itemView.findViewById(R.id.cvItem)
    
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

    val navController = navController
    fun setItemInventory(item: Articulo) {
        textProductName.text = item.nombre
        textProductQty.text = "Cant: ${item.cantidad}"

        val priceAsDouble = item.precio.toDouble()

        // Formatear el precio con separadores de miles y dos decimales
        val formattedPrice = formatPrice(priceAsDouble)
        textProductPrice.text = "$formattedPrice"


        cvItem.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("clave", item)

            /*cvItem.startAnimation(
                AnimationUtils.loadAnimation(
                    cvItem.context,
                    R.anim.scale_item
                )
            )*/

            it.animate().scaleX(0.8f).scaleY(0.8f).setDuration(200).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
                //navController.navigate(R.id.action_homeFragment_to_detailsFragment,bundle)
                Toast.makeText(itemView.context, "Articulo:  ${item.nombre}", Toast.LENGTH_SHORT).show()
            }.start()
        }


    }
    private fun formatPrice(price: Double): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale("es", "ES"))
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2

        return "$" + numberFormat.format(price)
    }
}