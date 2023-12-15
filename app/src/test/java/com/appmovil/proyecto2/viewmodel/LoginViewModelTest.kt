package com.appmovil.proyecto2.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.appmovil.proyecto2.repository.LoginRepository
import com.google.common.base.CharMatcher.any
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.AdditionalMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Necesario para manejar operaciones asincrónicas de coroutines en las pruebas
    private val testDispatcher = TestCoroutineDispatcher()


    @Mock
    private lateinit var repository: LoginRepository

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        loginViewModel = LoginViewModel(repository)

        // Inicializamos el dispatcher de coroutines para pruebas
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun cleanup() {
        // Restauramos el dispatcher de coroutines después de la prueba
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `sesion should disable view when email is null`() {
        // Arrange
        val email: String? = null

        // Act
        loginViewModel.sesion(email) { isEnableView ->
            // Assert
            assertFalse(isEnableView)
        }
    }

}
