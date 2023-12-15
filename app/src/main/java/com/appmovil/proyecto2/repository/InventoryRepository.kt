package com.appmovil.proyecto2.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appmovil.proyecto2.model.Articulo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class InventoryRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val listProductos: MutableLiveData<String>,
    private val inventoryList: MutableLiveData<MutableList<Articulo>>

) {
    suspend fun guardarProducto(codigo: Int, nombre: String, precio: Int, cantidad: Int): Boolean {
        return try {
            db.collection("articulo").document(codigo.toString()).set(
                hashMapOf(
                    "codigo" to codigo,
                    "nombre" to nombre,
                    "precio" to precio,
                    "cantidad" to cantidad
                )
            ).await()
            true
        } catch (e: Exception) {
            // Error al guardar el producto
            Log.e("InventoryRepository", "Error al guardar el producto", e)
            false
        }
    }

    fun listarProductos(): LiveData<String> {
        db.collection("articulo").get().addOnSuccessListener {
            var data = ""
            for (document in it.documents) {
                data += "Código: ${document.get("codigo")} " +
                        "Nombre: ${document.get("nombre")} " +
                        "Precio: ${document.get("precio")} " +
                        "Cantidad: ${document.get("cantidad")}\n\n"
            }
            // Notificar la lista de productos
            listProductos.postValue(data)
        }
        return listProductos
    }

    fun actualizarProducto(
        codigo: Int,
        nombre: String,
        precio: Int,
        cantidad: Int,
        productoActualizado: MutableLiveData<Boolean>
    ) {
        db.collection("articulo").document(codigo.toString()).update(
            hashMapOf(
                "nombre" to nombre,
                "precio" to precio,
                "cantidad" to cantidad
            ) as Map<String, Any>
        ).addOnSuccessListener {
            // Notificar que el producto se ha actualizado exitosamente
            productoActualizado.postValue(true)
        }.addOnFailureListener {
            // Notificar que hubo un error al actualizar el producto
            productoActualizado.postValue(false)
        }
    }

    fun eliminarProducto(codigo: Int, productoEliminado: MutableLiveData<Boolean>) {
        db.collection("articulo").document(codigo.toString()).delete().addOnSuccessListener {
            // Notificar que el producto se ha eliminado exitosamente
            productoEliminado.postValue(true)
        }.addOnFailureListener {
            // Notificar que hubo un error al eliminar el producto
            productoEliminado.postValue(false)
        }
        fun getInventory(): LiveData<MutableList<Articulo>> {
            db.collection("articulo").get().addOnSuccessListener {
                var data: MutableList<Articulo> = mutableListOf()
                for (document in it.documents) {
                    val item = Articulo(
                        document.get("codigo").toString().toInt(),
                        document.get("nombre").toString(),
                        document.get("precio").toString().toInt(),
                        document.get("cantidad").toString().toInt()
                    )
                    data.add(item)
                }
                // Notificar la lista de productos
                inventoryList.value = data
            }
            return inventoryList
        }
    }

    fun totalInventario(): LiveData<Double> {
        val totalLiveData = MutableLiveData<Double>()

        db.collection("articulo").get().addOnSuccessListener { querySnapshot ->
            var totalInventario = 0.0

            for (document in querySnapshot.documents) {
                val precio = document.get("precio").toString().toDouble()
                val cantidad = document.get("cantidad").toString().toInt()

                totalInventario += precio * cantidad
            }
            totalLiveData.postValue(totalInventario)
        }

        return totalLiveData
    }
}