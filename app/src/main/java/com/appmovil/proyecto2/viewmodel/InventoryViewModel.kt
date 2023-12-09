package com.appmovil.proyecto2.viewmodel

import androidx.lifecycle.ViewModel
import com.appmovil.proyecto2.repository.InventoryRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val repository: InventoryRepository
) : ViewModel() {
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