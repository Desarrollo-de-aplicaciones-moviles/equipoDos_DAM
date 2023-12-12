package com.appmovil.proyecto2.viewmodel

import androidx.lifecycle.ViewModel
import com.appmovil.proyecto2.repository.InventoryRepository
import androidx.lifecycle.LiveData
import com.appmovil.proyecto2.model.Articulo
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val repository: InventoryRepository
) : ViewModel() {

    suspend fun guardarProducto(codigo: Int, nombre: String, precio: Int, cantidad: Int): Boolean {
        return repository.guardarProducto(codigo, nombre, precio, cantidad)
    }

    fun listarProductos(): LiveData<String> {
        return repository.listarProductos()
    }
}
