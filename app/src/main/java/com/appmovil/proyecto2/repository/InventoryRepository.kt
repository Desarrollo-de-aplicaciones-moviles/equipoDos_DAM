package com.appmovil.proyecto2.repository

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.appmovil.proyecto2.model.Articulo
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class InventoryRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val listProductos: MutableLiveData<String>
){
    fun guardarProducto(codigo: Int, nombre: String, precio: Int, cantidad: Int, productoGuardado: MutableLiveData<Boolean>) {
        val articulo = Articulo(codigo, nombre, precio, cantidad)

        db.collection("articulo").document(articulo.codigo.toString()).set(
            hashMapOf(
                "codigo" to articulo.codigo,
                "nombre" to articulo.nombre,
                "precio" to articulo.precio,
                "cantidad" to articulo.cantidad
            )
        ).addOnSuccessListener {
            // Notificar que el producto se ha guardado exitosamente
            productoGuardado.postValue(true)
        }.addOnFailureListener {
            // Notificar que hubo un error al guardar el producto
            productoGuardado.postValue(false)
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

    fun actualizarProducto(codigo: Int, nombre: String, precio: Int, cantidad: Int, productoActualizado: MutableLiveData<Boolean>) {
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
    }
}