package com.appmovil.proyecto2.view.fragment

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appmovil.proyecto2.R
import com.appmovil.proyecto2.databinding.FragmentAddBinding
import com.appmovil.proyecto2.model.Articulo
import android.widget.ImageView
import android.widget.EditText
import android.widget.Toast
import android.text.Editable
import android.text.TextWatcher
import com.google.firebase.firestore.FirebaseFirestore


class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private val db = FirebaseFirestore.getInstance()
    private var originalTextColor: Int = 0
    private var originalTextTypeface: Typeface? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater)
        val backIcon: ImageView = binding.root.findViewById(R.id.backButton)
        backIcon.setOnClickListener {
            it.animate().scaleX(0.8f).scaleY(0.8f).setDuration(200).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
                activity?.onBackPressedDispatcher?.onBackPressed()
            }.start()
        }
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controladores()
        observarCampos()

        val btnGuardar = binding.btnSave
        originalTextColor = btnGuardar.currentTextColor
        originalTextTypeface = btnGuardar.typeface
    }

    private fun controladores() {
        binding.btnSave.setOnClickListener {
            guardarProducto()
            listarProducto()
            limpiarCampos()
        }
    }

    private fun guardarProducto() {
        val codigo = binding.editTextCodigoProducto.text.toString()
        val nombre = binding.editTextNombre.text.toString()
        val precio = binding.editTextPrecio.text.toString()
        val cantidad = binding.editTextCantidad.text.toString()

        if (codigo.isNotEmpty() && nombre.isNotEmpty() && precio.isNotEmpty() && cantidad.isNotEmpty()) {
            val articulo = Articulo(codigo.toInt(), nombre, precio.toInt(), cantidad.toInt())

            db.collection("articulo").document(articulo.codigo.toString()).set(
                hashMapOf(
                    "codigo" to articulo.codigo,
                    "nombre" to articulo.nombre,
                    "precio" to articulo.precio,
                    "cantidad" to articulo.cantidad
                )
            )

            Toast.makeText(context, "Articulo guardado", Toast.LENGTH_SHORT).show()
            limpiarCampos()
            listarProducto()
        } else {
            Toast.makeText(context, "Llene los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun listarProducto(){
        db.collection("articulo").get().addOnSuccessListener {
            var data = ""
            for (document in it.documents) {
                // Aquí puedes personalizar cómo deseas mostrar cada artículo en la lista
                data += "Código: ${document.get("codigo")} " +
                        "Nombre: ${document.get("nombre")} " +
                        "Precio: ${document.get("precio")} " +
                        "Cantidad: ${document.get("cantidad")}\n\n"

            }
            binding.tvListProducto.text = data
        }
    }
    private fun limpiarCampos() {
        binding.editTextCodigoProducto.setText("")
        binding.editTextNombre.setText("")
        binding.editTextPrecio.setText("")
        binding.editTextCantidad.setText("")
    }

    private fun observarCampos() {
        val editTextCodigo = binding.editTextCodigoProducto
        val editTextNombre = binding.editTextNombre
        val editTextPrecio = binding.editTextPrecio
        val editTextCantidad = binding.editTextCantidad

        setFocusChangeListener(editTextCodigo)
        setFocusChangeListener(editTextNombre)
        setFocusChangeListener(editTextPrecio)
        setFocusChangeListener(editTextCantidad)

        editTextCodigo.addTextChangedListener(textWatcher)
        editTextNombre.addTextChangedListener(textWatcher)
        editTextPrecio.addTextChangedListener(textWatcher)
        editTextCantidad.addTextChangedListener(textWatcher)
    }

    private fun setFocusChangeListener(editText: EditText) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                editText.setBackgroundResource(R.drawable.bg_highlighted)
            } else {
                editText.setBackgroundResource(R.drawable.bg_redondo)
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            //
        }

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            //
        }

        override fun afterTextChanged(editable: Editable?) {
            // Verifica si todos los campos están llenos para habilitar o deshabilitar el botón
            val codigo = binding.editTextCodigoProducto.text.toString()
            val nombre = binding.editTextNombre.text.toString()
            val precio = binding.editTextPrecio.text.toString()
            val cantidad = binding.editTextCantidad.text.toString()

            val btnGuardar = binding.btnSave
            if (codigo.isNotEmpty() && nombre.isNotEmpty() && precio.isNotEmpty() && cantidad.isNotEmpty()) {
                // Habilita el botón
                btnGuardar.isEnabled = true
                btnGuardar.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
                btnGuardar.setTypeface(null, Typeface.BOLD)
            }
            else{
                // Deshabilita el botón
                btnGuardar.isEnabled = false
                btnGuardar.setTextColor(originalTextColor)
                btnGuardar.setTypeface(originalTextTypeface, Typeface.NORMAL)
                btnGuardar.text = "Guardar"
            }
        }
    }

}