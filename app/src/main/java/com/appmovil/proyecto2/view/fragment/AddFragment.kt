package com.appmovil.proyecto2.view.fragment

import android.graphics.Typeface
import androidx.core.content.ContextCompat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appmovil.proyecto2.R
import com.appmovil.proyecto2.databinding.FragmentAddBinding
import android.widget.ImageView
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.appmovil.proyecto2.viewmodel.InventoryViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private lateinit var viewModel: InventoryViewModel

    private var originalTextColor: Int = 0
    private var originalTextTypeface: Typeface? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(InventoryViewModel::class.java)
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

        binding.btnSave.setOnClickListener {
            val codigo = binding.editTextCodigoProducto.text.toString().toInt()
            val nombre = binding.editTextNombre.text.toString()
            val precio = binding.editTextPrecio.text.toString().toInt()
            val cantidad = binding.editTextCantidad.text.toString().toInt()

            viewModel.guardarProducto(codigo, nombre, precio, cantidad)
        }

        viewModel.getProductoGuardadoLiveData().observe(viewLifecycleOwner, { exitoso ->
            if (exitoso) {
                // Si la operación fue exitosa, navegar al fragmento del home
                findNavController().navigate(R.id.action_addFragment_to_homeFragment)
            } else {
                // Si hubo un error, mostrar un Toast
                Toast.makeText(requireContext(), "Error al guardar el producto", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun controladores() {
        binding.btnSave.setOnClickListener {
            val codigo = binding.editTextCodigoProducto.text.toString().toInt()
            val nombre = binding.editTextNombre.text.toString()
            val precio = binding.editTextPrecio.text.toString().toInt()
            val cantidad = binding.editTextCantidad.text.toString().toInt()

            viewModel.guardarProducto(codigo, nombre, precio, cantidad)
        }
    }

    private fun observarCampos() {
        //viewModel.listarProductos().observe(viewLifecycleOwner, Observer { productos ->
        //    binding.tvListProducto.text = productos
        //})
        val editTextCodigo = binding.editTextCodigoProducto
        val editTextNombre = binding.editTextNombre
        val editTextPrecio = binding.editTextPrecio
        val editTextCantidad = binding.editTextCantidad

        editTextCodigo.addTextChangedListener(textWatcher)
        editTextNombre.addTextChangedListener(textWatcher)
        editTextPrecio.addTextChangedListener(textWatcher)
        editTextCantidad.addTextChangedListener(textWatcher)
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