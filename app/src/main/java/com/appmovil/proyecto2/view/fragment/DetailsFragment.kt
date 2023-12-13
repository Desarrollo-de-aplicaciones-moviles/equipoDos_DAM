package com.appmovil.proyecto2.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.appmovil.proyecto2.R
import androidx.navigation.fragment.findNavController
import com.appmovil.proyecto2.databinding.FragmentDetailsBinding
import com.appmovil.proyecto2.model.Articulo
import com.appmovil.proyecto2.viewmodel.InventoryViewModel
import com.google.firebase.firestore.FirebaseFirestore
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: InventoryViewModel by viewModels()
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
        dataArticulo()
        controladores()
        val backButtonDet: ImageView = binding.root.findViewById(R.id.backButtonDet)
        backButtonDet.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun controladores() {
        binding.btnDelete.setOnClickListener {
            deleteArticulo()
        }
    }
   @SuppressLint("SetTextI18n")
   private fun dataArticulo() {
        val receivedBundle = arguments
        receivedArticulo = receivedBundle?.getSerializable("articulo") as? Articulo ?: return
        binding.tvItem.text = receivedArticulo.nombre
        binding.tvPrice.text = "$ ${receivedArticulo.precio}"
        binding.tvQuantity.text = "${receivedArticulo.cantidad}"
        binding.txtTotal.text = "$ ${
            viewModel.calcularValorTotalProducto(
                receivedArticulo.precio,
                receivedArticulo.cantidad
            )
        }"
    }
    private fun deleteArticulo() {
        viewModel.eliminarProducto(receivedArticulo.codigo)
        viewModel.listarProductos()
        findNavController().popBackStack()
    }

}