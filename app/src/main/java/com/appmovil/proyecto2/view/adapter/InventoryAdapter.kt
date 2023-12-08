package com.appmovil.proyecto2.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.appmovil.proyecto2.databinding.ItemInventoryBinding
import com.appmovil.proyecto2.model.Articulo
import com.appmovil.proyecto2.view.viewholder.InventoryViewHolder

class InventoryAdapter (private val listChallenge:MutableList<Articulo>, private val navController: NavController):RecyclerView.Adapter<InventoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val binding = ItemInventoryBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return InventoryViewHolder(binding,navController)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val item = listChallenge[position]
        holder.setItemInventory(item)

    }

    override fun getItemCount(): Int {
        return listChallenge.size
    }

}