package com.appmovil.proyecto2.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appmovil.proyecto2.R
import androidx.navigation.fragment.findNavController
import com.appmovil.proyecto2.databinding.FragmentDetailsBinding
import com.appmovil.proyecto2.model.Articulo
import com.appmovil.proyecto2.viewmodel.InventoryViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: InventoryViewModel
    private val db = FirebaseFirestore.getInstance()
    private lateinit var receivedArticulo: Articulo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            dataArticulo()
        }
        controladores()
    }

    private fun controladores() {
        binding.btnDelete.setOnClickListener {
            lifecycleScope.launch {
                deleteArticulo()
            }
        }
    }
   private suspend fun dataArticulo() {
        val receivedBundle = arguments
        receivedArticulo = receivedBundle?.getSerializable("articulo") as? Articulo ?: return
        binding.tvItem.text = "${receivedArticulo.nombre}"
        binding.tvPrice.text = "$ ${receivedArticulo.precio}"
        binding.tvQuantity.text = "${receivedArticulo.cantidad}"
        binding.txtTotal.text = "$ ${
            viewModel.calcularValorTotalProducto(
                receivedArticulo.precio,
                receivedArticulo.cantidad
            )
        }"
    }
    private suspend fun deleteArticulo() {
        viewModel.eliminarProducto(receivedArticulo.codigo)
        viewModel.listarProductos()
        findNavController().popBackStack()
    }

}