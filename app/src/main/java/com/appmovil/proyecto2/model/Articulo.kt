package com.appmovil.proyecto2.model

import java.io.Serializable

data class Articulo(
    val codigo:Int,
    val nombre: String,
    val precio:Int,
    val cantidad:Int
): Serializable
