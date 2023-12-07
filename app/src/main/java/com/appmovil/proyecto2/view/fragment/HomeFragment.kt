package com.appmovil.proyecto2.view.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.library.R
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.appmovil.proyecto2.databinding.FragmentHomeBinding
import com.appmovil.proyecto2.view.HomeActivity
import com.appmovil.proyecto2.view.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("shared", Context.MODE_PRIVATE)
        dataLogin()
        setup()
        controladores()
    }

    private fun setup() {
        binding.btnLogOut.setOnClickListener {
            logOut()
        }
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

}