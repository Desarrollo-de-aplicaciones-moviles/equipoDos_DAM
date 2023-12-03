package com.appmovil.proyecto2.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.appmovil.proyecto2.R
import com.appmovil.proyecto2.databinding.ActivityLoginBinding
import com.appmovil.proyecto2.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        sharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE)
        sesion()
        setup()
        sentinel()
    }

    private fun sentinel(){
        binding.etPass.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val passwordLength = s?.length ?: 0
                val tvPasswordError = binding.tvPasswordError

                if (passwordLength < 6) {
                    val colorOrange = ContextCompat.getColor(this@LoginActivity, R.color.orange)
                    binding.tilPass.boxStrokeColor =
                        colorOrange
                    binding.tilPass.defaultHintTextColor =
                        ColorStateList.valueOf(colorOrange)
                    tvPasswordError.visibility = View.VISIBLE
                } else {
                    val colorDefault = ContextCompat.getColor(this@LoginActivity, R.color.white)
                    binding.tilPass.boxStrokeColor =
                        colorDefault
                    binding.tilPass.defaultHintTextColor =
                        ColorStateList.valueOf(colorDefault)
                    tvPasswordError.visibility = View.GONE
                }
            }
        })
    }

    private fun setup() {
        binding.tvRegister.setOnClickListener {
           registerUser()
        }

        binding.btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun registerUser(){
        val email = binding.etEmail.text.toString()
        val pass = binding.etPass.text.toString()
        loginViewModel.registerUser(email,pass) { isRegister ->
            if (isRegister) {
                goToHome(email)
            } else {
                Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToHome(email: String?){
        val intent = Intent (this, HomeActivity::class.java).apply {
            putExtra("email",email)
        }
        startActivity(intent)
        finish()
    }

    private fun loginUser(){
        val email = binding.etEmail.text.toString()
        val pass = binding.etPass.text.toString()
       loginViewModel.loginUser(email,pass){ isLogin ->
           if (isLogin){
               goToHome(email)
           }else {
               Toast.makeText(this, "Login incorrecto", Toast.LENGTH_SHORT).show()
           }
       }
    }

    private fun sesion(){
        val email = sharedPreferences.getString("email",null)
        loginViewModel.sesion(email){ isEnableView ->
            if (isEnableView){
                binding.clContenedor.visibility = View.INVISIBLE
                goToHome(email)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.clContenedor.visibility = View.VISIBLE
    }
}