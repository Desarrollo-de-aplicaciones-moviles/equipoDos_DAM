package com.appmovil.proyecto2.viewmodel

import androidx.lifecycle.ViewModel
import com.appmovil.proyecto2.repository.InventoryRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class InventoryViewModel : ViewModel() {
    private val repository = InventoryRepository()
    private val productoGuardado = MutableLiveData<Boolean>()

    fun guardarProducto(codigo: Int, nombre: String, precio: Int, cantidad: Int) {
        repository.guardarProducto(codigo, nombre, precio, cantidad, productoGuardado)
    }

    fun getProductoGuardadoLiveData(): LiveData<Boolean> {
        return productoGuardado
    }

    fun listarProductos(): LiveData<String> {
        return repository.listarProductos()
    }
}