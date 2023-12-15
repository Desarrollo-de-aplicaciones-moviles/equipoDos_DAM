package com.appmovil.proyecto2.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.appmovil.proyecto2.R
import com.appmovil.proyecto2.databinding.ActivityLoginBinding
import com.appmovil.proyecto2.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        sharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE)
        sesion()
        setup()
        sentinel()
    }

    private fun sentinel() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateFields()
            }
        })

        binding.etPass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val passwordLength = s?.length ?: 0
                val tvPasswordError = binding.tvPasswordError
                val eyeIcon = binding.tilPass.endIconDrawable
                val tilPass = binding.tilPass
                val etPass = binding.etPass

                validateFields()

                if (passwordLength < 6) {
                    val colorRed = ContextCompat.getColor(this@LoginActivity, R.color.red)
                    binding.tilPass.boxStrokeColor = colorRed
                    binding.tilPass.defaultHintTextColor = ColorStateList.valueOf(colorRed)
                    tvPasswordError.visibility = View.VISIBLE
                } else {
                    val colorDefault = ContextCompat.getColor(this@LoginActivity, R.color.white)
                    binding.tilPass.boxStrokeColor = colorDefault
                    binding.tilPass.defaultHintTextColor = ColorStateList.valueOf(colorDefault)
                    tvPasswordError.visibility = View.GONE
                }

                if (passwordLength > 0) {
                    eyeIcon?.setTint(ContextCompat.getColor(this@LoginActivity, R.color.white))
                    tilPass.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                    tilPass.setEndIconOnClickListener {
                        val cursorPosition = etPass.selectionEnd
                        etPass.transformationMethod =
                            if (etPass.transformationMethod == PasswordTransformationMethod.getInstance())
                                HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
                        eyeIcon?.setTint(ContextCompat.getColor(this@LoginActivity, R.color.gray))

                        etPass.setSelection(cursorPosition)
                    }
                } else { //Si no hay texto en el campo
                    eyeIcon?.setTint(ContextCompat.getColor(this@LoginActivity, R.color.gray))
                    tilPass.endIconMode = TextInputLayout.END_ICON_NONE
                    tilPass.setEndIconOnClickListener(null)
                }
            }
        })
    }

    private fun validateFields() {
        val btnLogin = binding.btnLogin
        val btnRegister = binding.tvRegister
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPass.text.toString().trim()

        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 6

        if (isEmailValid && isPasswordValid) {
            btnLogin.isEnabled = true
            btnLogin.setTypeface(null, Typeface.BOLD)
            btnLogin.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.white))
            //btnLogin.setBackgroundColor(ContextCompat.getColor(this@LoginActivity, R.color.orange))

            btnRegister.isEnabled = true
            btnRegister.setTypeface(null, Typeface.BOLD)
            btnRegister.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.white))
        } else {
            btnLogin.isEnabled = false
            btnLogin.setTypeface(null, Typeface.NORMAL)
            btnLogin.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.gray))
            //btnLogin.setBackgroundColor(ContextCompat.getColor(this@LoginActivity, R.color.orange_disabled))

            btnRegister.isEnabled = false
            btnRegister.setTypeface(null, Typeface.NORMAL)
            btnRegister.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.gray))
        }
    }

    private fun setup() {
        binding.tvRegister.setOnClickListener {
            registerUser()
        }

        binding.btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun registerUser() {
        val email = binding.etEmail.text.toString()
        val pass = binding.etPass.text.toString()
        loginViewModel.registerUser(email, pass) { isRegister ->
            if (isRegister) {
                goToHome(email)
            } else {
                Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToHome(email: String?) {

        val bundle = intent.extras
        val closeApp = bundle?.getBoolean("widget", false)?:false
        if (!closeApp) {
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("email", email)
            }
            startActivity(intent)
            finish()
        }else{
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)

        }


    }

    private fun loginUser() {
        val email = binding.etEmail.text.toString()
        val pass = binding.etPass.text.toString()
        loginViewModel.loginUser(email, pass) { isLogin ->
            if (isLogin) {
                goToHome(email)
            } else {
                Toast.makeText(this, "Login incorrecto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sesion() {
        val email = sharedPreferences.getString("email", null)
        loginViewModel.sesion(email) { isEnableView ->
            if (isEnableView) {
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