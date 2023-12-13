package com.appmovil.proyecto2.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.appmovil.proyecto2.repository.InventoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class InventoryViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: InventoryRepository

    private lateinit var inventoryViewModel: InventoryViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        inventoryViewModel = InventoryViewModel(repository)
    }

    @Test
    fun `test metodo guardarProducto`() = runBlockingTest {
        //given
        val codigo = 15
        val nombre = "Item1"
        val precio = 10
        val cantidad = 5

        `when`(repository.guardarProducto(codigo, nombre, precio, cantidad))
            .thenReturn(true)

        // Llamamos al método que queremos probar
        val result = inventoryViewModel.guardarProducto(codigo, nombre, precio, cantidad)

        // Verificamos que el método en el repositorio haya sido llamado con los mismos argumentos
        verify(repository).guardarProducto(codigo, nombre, precio, cantidad)

        // Verificamos que el resultado sea el esperado
        assert(result)
    }


    @Test
    fun `test metodo listarProductos`() {
        // Dado que listarProductos no es suspendido, no es necesario usar coroutines en las pruebas

        // Simula la respuesta del repositorio
        val productosMock = "Código: 1 Nombre: Producto1 Precio: 10 Cantidad: 5\n\n" +
                "Código: 2 Nombre: Producto2 Precio: 15 Cantidad: 8\n\n"
        `when`(repository.listarProductos()).thenReturn(MutableLiveData(productosMock))

        // Observador para el LiveData que se espera sea actualizado
        val observer = mock(Observer::class.java) as Observer<String>

        // Observar el LiveData y verificar si se actualiza correctamente
        inventoryViewModel.listarProductos().observeForever(observer)

        // Verifica que el método en el repositorio haya sido llamado
        verify(repository).listarProductos()

        // Verifica que el LiveData en el ViewModel ha sido actualizado con la data simulada
        verify(observer).onChanged(productosMock)
    }
}








