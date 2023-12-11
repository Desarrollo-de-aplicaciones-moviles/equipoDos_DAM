package com.appmovil.proyecto2.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.appmovil.proyecto2.model.Articulo
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class InventoryRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val listProductos: MutableLiveData<String>
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
}