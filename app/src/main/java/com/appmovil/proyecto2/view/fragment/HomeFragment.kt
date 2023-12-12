package com.appmovil.proyecto2.view.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.appmovil.proyecto2.viewmodel.InventoryViewModel
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.appmovil.proyecto2.view.adapter.ProductosAdapter
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.appmovil.proyecto2.databinding.FragmentHomeBinding
import com.appmovil.proyecto2.model.Articulo
import com.appmovil.proyecto2.view.HomeActivity
import com.appmovil.proyecto2.view.LoginActivity
import com.appmovil.proyecto2.view.adapter.InventoryAdapter
import com.appmovil.proyecto2.viewmodel.InventoryViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.appmovil.proyecto2.model.Articulo
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val inventoryViewModel: InventoryViewModel by viewModels()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var viewModel: InventoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(InventoryViewModel::class.java)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("shared", Context.MODE_PRIVATE)
        dataLogin()
        setup()
        controladores()

        val navController = Navigation.findNavController(view)

        observerViewModel(navController)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        viewModel.listarProductos().observe(viewLifecycleOwner, Observer { productos ->

            val recyclerView = binding.recyclerViewProductos
            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager = layoutManager

            // Parsea la cadena de productos y crea una lista de objetos Articulo
            val productList = parseProductList(productos)

            val adapter = ProductosAdapter(requireContext(), productList)
            recyclerView.adapter = adapter
        })
    }

    private fun parseProductList(productos: String): List<Articulo> {
        val productList = mutableListOf<Articulo>()
        val lines = productos.split("\n")

        for (line in lines) {
            if (line.isNotBlank()) {
                // Utilizar expresión regular para dividir la línea
                val regex = Regex("Código: (\\d+) Nombre: (.+) Precio: (\\d+) Cantidad: (\\d+)")
                val matchResult = regex.find(line)

                if (matchResult != null && matchResult.groupValues.size == 5) {
                    try {
                        val codigo = matchResult.groupValues[1].toInt()
                        val nombre = matchResult.groupValues[2]
                        val precio = matchResult.groupValues[3].toInt()
                        val cantidad = matchResult.groupValues[4].toInt()

                        val articulo = Articulo(codigo, nombre, precio, cantidad)
                        productList.add(articulo)
                    } catch (e: Exception) {
                        Log.e("ParseError", "Error parsing line: $line", e)
                    }
                } else {
                    Log.e("ParseError", "Malformed line: $line")
                }
            }
        }
        Log.d("ParsedProducts", "Parsed product list: $productList")
        return productList
    }



    private fun setup() {
        /*binding.btnLogOut.setOnClickListener {
            logOut()
        }*/
    }

    private fun dataLogin() {
        val bundle = requireActivity().intent.extras
        val email = bundle?.getString("email")
        sharedPreferences.edit().putString("email",email).apply()
    }

    private fun logOut() {
        sharedPreferences.edit().clear().apply()
        FirebaseAuth.getInstance().signOut()
        (requireActivity() as HomeActivity).apply {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun controladores() {
        binding.fbagregar.setOnClickListener {
            findNavController().navigate(com.appmovil.proyecto2.R.id.action_homeFragment_to_addFragment)
        }
    }

    private fun observerViewModel(navController: NavController) {
        observerListInventory(navController)
    }

    private fun observerListInventory(navController: NavController) {

        inventoryViewModel.getInventoryList().observe(viewLifecycleOwner){ itemsList->
            val recycler = binding.recyclerview
            val layoutManager = LinearLayoutManager(context)
            recycler.layoutManager = layoutManager
            val adapter = InventoryAdapter(itemsList, navController)
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()
        }

    }

}