package com.appmovil.proyecto2.view.viewholder

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.appmovil.proyecto2.R
import com.appmovil.proyecto2.databinding.ItemInventoryBinding
import com.appmovil.proyecto2.model.Articulo
import java.text.DecimalFormat

class InventoryViewHolder(
    binding: ItemInventoryBinding,
    navController: NavController) : RecyclerView.ViewHolder(binding.root) {
    val bindingItem = binding
    val navController = navController

    fun setItemInventory(item: Articulo) {
        bindingItem.tvItemName.text = item.nombre
        bindingItem.tvItemQty.text = "Cant: " + item.cantidad.toString()
        bindingItem.tvItemPrice.text = formatPrice(item.precio)
        bindingItem.cvItem.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("clave", item)
            //navController.navigate(R.id.action_homeItemsFragment_to_EditItemFragment,bundle)
        }
        /*bindingItem.ivEditItem.setOnClickListener {
            it.animate().scaleX(0.8f).scaleY(0.8f).setDuration(200).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
            }
            editItemListener.onEditItemClick(item)
        }
        bindingItem.ivDelete.setOnClickListener {
            it.animate().scaleX(0.8f).scaleY(0.8f).setDuration(200).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
            }
            deleteItemListener.onDeleteItemClick(Item)
        }*/

        bindingItem.cvItem.startAnimation(
            AnimationUtils.loadAnimation(
                bindingItem.cvItem.context,
                R.anim.scale_item
            )
        )
    }

    private fun formatPrice(price:Int):String{
        val formatter = DecimalFormat("#,##")
        return "$ "+formatter.format(price).replace(',','.')
    }

    interface EditItemListener {
        fun onEditItemClick(item: Articulo)
    }

    interface DeleteItemListener {
        fun onDeleteItemClick(item: Articulo)
    }

}