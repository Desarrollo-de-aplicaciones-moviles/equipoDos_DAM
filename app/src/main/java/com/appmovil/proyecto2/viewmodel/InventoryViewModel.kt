package com.appmovil.proyecto2.viewmodel

import androidx.lifecycle.ViewModel
import com.appmovil.proyecto2.repository.InventoryRepository
import androidx.lifecycle.LiveData
import com.appmovil.proyecto2.model.Articulo


class InventoryViewModel : ViewModel() {
    private val repository = InventoryRepository()

    fun guardarProducto(codigo: Int, nombre: String, precio: Int, cantidad: Int) {
        repository.guardarProducto(codigo, nombre, precio, cantidad)
    }

    fun listarProductos(): LiveData<String> {
        return repository.listarProductos()
    }

    fun getInventoryList(): LiveData<MutableList<Articulo>> {
        return repository.getInventory()
    }
}