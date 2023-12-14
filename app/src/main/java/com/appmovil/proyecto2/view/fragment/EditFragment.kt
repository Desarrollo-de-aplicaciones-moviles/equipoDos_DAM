package com.appmovil.proyecto2.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.appmovil.proyecto2.R
import com.appmovil.proyecto2.databinding.FragmentEditBinding
import com.appmovil.proyecto2.model.Articulo
import com.appmovil.proyecto2.viewmodel.InventoryViewModel
import dagger.hilt.android.AndroidEntryPoint

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
        val backButtonDet: ImageView = binding.root.findViewById(R.id.backButtonEdit)
        backButtonDet.setOnClickListener {
            findNavController().popBackStack()
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
        val nombre = binding.editTextNombre.text.toString()
        val cantidad = binding.editTextCantidad.text.toString().toInt()
        val precio = binding.editTextPrecio.text.toString().toInt()
        ViewModel.actualizarProducto(receivedArticulo.codigo, nombre, cantidad, precio)
        findNavController().navigate(R.id.action_editFragment_to_homeFragment)
    }

    private fun checkFieldsForEmptyValues() {
        val nombre = binding.editTextNombre.text.toString()
        val precio = binding.editTextPrecio.text.toString()
        val cantidad = binding.editTextCantidad.text.toString()

        binding.btnEditar.isEnabled = nombre.isNotEmpty() && precio.isNotEmpty() && cantidad.isNotEmpty()
    }


}