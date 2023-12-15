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
    private val productoGuardado = MutableLiveData<Boolean>()
    val productoActualizado = MutableLiveData<Boolean>()
    val productoEliminado = MutableLiveData<Boolean>()

    suspend fun guardarProducto(codigo: Int, nombre: String, precio: Int, cantidad: Int): Boolean {
        return repository.guardarProducto(codigo, nombre, precio, cantidad)
    }

    fun listarProductos(): LiveData<String> {
        return repository.listarProductos()
    }

    fun actualizarProducto(codigo: Int, nombre: String, precio: Int, cantidad: Int): Boolean {
        repository.actualizarProducto(codigo, nombre, precio, cantidad, productoActualizado)
        return productoActualizado.value ?: false
    }

    fun eliminarProducto(codigo: Int): Boolean {
        repository.eliminarProducto(codigo, productoEliminado)
        return productoEliminado.value ?: false
    }

    fun calcularValorTotalProducto(precio: Int, cantidad: Int): Int {
        return precio * cantidad
    }

    fun obtenerTotalProductos(): LiveData<Double> {
        return repository.totalInventario()
    }
}
