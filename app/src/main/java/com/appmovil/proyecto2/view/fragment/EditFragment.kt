package com.appmovil.proyecto2.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appmovil.proyecto2.R
import com.appmovil.proyecto2.databinding.FragmentEditBinding
import com.appmovil.proyecto2.model.Articulo
import com.appmovil.proyecto2.viewmodel.InventoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditFragment : Fragment() {
    private lateinit var binding: FragmentEditBinding
    private val ViewModel: InventoryViewModel by viewModels()
    private lateinit var receivedArticulo: Articulo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataArticulo()
        controladores()
        val backButtonEdit: ImageView = binding.root.findViewById(R.id.backButtonEdit)
        backButtonEdit.setOnClickListener {it.animate().scaleX(0.8f).scaleY(0.8f).setDuration(200).withEndAction {
            it.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
            findNavController().popBackStack()}.start()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun dataArticulo() {
        val receivedBundle = arguments
        receivedArticulo = receivedBundle?.getSerializable("clave") as? Articulo ?: return
        binding.tvIDtoEdit.text = "Id: ${receivedArticulo.codigo}"
        binding.editTextNombre.setText(receivedArticulo.nombre)
        binding.editTextCantidad.setText(receivedArticulo.cantidad.toString())
        binding.editTextPrecio.setText(receivedArticulo.precio.toString())
    }

    private fun controladores() {
        binding.btnEditar.setOnClickListener {
            editArticulo()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                checkFieldsForEmptyValues()
            }
        }

        binding.editTextNombre.addTextChangedListener(textWatcher)
        binding.editTextPrecio.addTextChangedListener(textWatcher)
        binding.editTextCantidad.addTextChangedListener(textWatcher)
    }

    private fun editArticulo() {
        lifecycleScope.launch {
            try {
                val nombre = binding.editTextNombre.text.toString()
                val precio = binding.editTextPrecio.text.toString().toDouble()
                val cantidad = binding.editTextCantidad.text.toString().toInt()
                val exitoso = ViewModel.guardarProducto(receivedArticulo.codigo, nombre, precio, cantidad)
                if (exitoso) {
                    findNavController().navigate(R.id.action_editFragment_to_homeFragment)
                } else {
                    Toast.makeText(
                        context,
                        "Ha ocurrido un error al editar el producto",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("AddFragment", "Error al intentar editar el producto", e)
                Toast.makeText(context, "Ha ocurrido un error inesperado", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun checkFieldsForEmptyValues() {
        val nombre = binding.editTextNombre.text.toString()
        val precio = binding.editTextPrecio.text.toString()
        val cantidad = binding.editTextCantidad.text.toString()

        binding.btnEditar.isEnabled = nombre.isNotEmpty() && precio.isNotEmpty() && cantidad.isNotEmpty()
    }


}