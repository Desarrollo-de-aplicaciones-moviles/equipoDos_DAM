package com.appmovil.proyecto2.repository

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.appmovil.proyecto2.model.Articulo
import androidx.lifecycle.MutableLiveData

class InventoryRepository {
    private val db = FirebaseFirestore.getInstance()
    private val listProductos = MutableLiveData<String>()
    private val inventoryList = MutableLiveData<MutableList<Articulo>>()

    fun guardarProducto(codigo: Int, nombre: String, precio: Int, cantidad: Int) {
        val articulo = Articulo(codigo, nombre, precio, cantidad)

        db.collection("articulo").document(articulo.codigo.toString()).set(
            hashMapOf(
                "codigo" to articulo.codigo,
                "nombre" to articulo.nombre,
                "precio" to articulo.precio,
                "cantidad" to articulo.cantidad
            )
        )

        // Notificar que el producto se ha guardado
        listProductos.postValue("Articulo guardado")
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
    fun getInventory(): LiveData<MutableList<Articulo>> {
        db.collection("articulo").get().addOnSuccessListener {
            var data:MutableList<Articulo> = mutableListOf()
            for (document in it.documents) {
                val item = Articulo(document.get("codigo").toString().toInt()
                    ,document.get("nombre").toString()
                    ,document.get("precio").toString().toInt()
                    ,document.get("cantidad").toString().toInt())
                data.add(item)
            }
            // Notificar la lista de productos
            inventoryList.value = data
        }
        return inventoryList
    }
}