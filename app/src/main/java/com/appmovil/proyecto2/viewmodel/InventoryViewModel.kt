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
    private val productoActualizado = MutableLiveData<Boolean>()
    private val productoEliminado = MutableLiveData<Boolean>()

    fun guardarProducto(codigo: Int, nombre: String, precio: Int, cantidad: Int) {
        repository.guardarProducto(codigo, nombre, precio, cantidad, productoGuardado)
    }

    fun getProductoGuardadoLiveData(): LiveData<Boolean> {
        return productoGuardado
    }

    fun listarProductos(): LiveData<String> {
        return repository.listarProductos()
    }

    suspend fun actualizarProducto(codigo: Int, nombre: String, precio: Int, cantidad: Int): Boolean {
        repository.actualizarProducto(codigo, nombre, precio, cantidad, productoActualizado)
        return productoActualizado.value ?: false
    }

    suspend fun eliminarProducto(codigo: Int): Boolean {
        repository.eliminarProducto(codigo, productoEliminado)
        return productoEliminado.value ?: false
    }

    suspend fun calcularValorTotalProducto(precio: Int, cantidad: Int): Int {
        return precio * cantidad
    }
}