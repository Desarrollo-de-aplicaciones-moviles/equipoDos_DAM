package com.appmovil.proyecto2.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.appmovil.proyecto2.model.Articulo
import com.appmovil.proyecto2.repository.InventoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class InventoryViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var inventoryViewModel: InventoryViewModel
    private lateinit var repository: InventoryRepository

    @Before
    fun setup() {
        repository = mock(InventoryRepository::class.java)
        inventoryViewModel = InventoryViewModel(repository)
    }

    @Test
    fun `test metodo guardarProducto`() = runBlocking {
        // given
        Dispatchers.setMain(UnconfinedTestDispatcher())
        val articulo = Articulo(codigo = 1, nombre = "Item1", precio = 10, cantidad = 5)

        // Simular el comportamiento del repositorio al guardar el producto
        `when`(repository.guardarProducto(
            eq(articulo.codigo),
            eq(articulo.nombre),
            eq(articulo.precio),
            eq(articulo.cantidad),
            any()
        )).thenAnswer { invocation ->
            // Devolver el artículo que se pasó como argumento al repositorio
            val inventoryArgument = invocation.getArgument<Articulo>(3)
            inventoryArgument
        }

        // Llamamos al método que queremos probar
        inventoryViewModel.guardarProducto(
            articulo.codigo,
            articulo.nombre,
            articulo.precio,
            articulo.cantidad
        )

        // Verificamos que el estado de progreso sea falso después de la operación
        verify(repository).guardarProducto(
            eq(articulo.codigo),
            eq(articulo.nombre),
            eq(articulo.precio),
            eq(articulo.cantidad),
            any()
        )

        // Esperamos a que la operación asíncrona se complete
        //advanceTimeBy(1000)

        // Verificamos que el LiveData tiene el valor esperado (puede variar dependiendo de tu implementación)
        assertEquals(true, inventoryViewModel.getProductoGuardadoLiveData().value)

        // Restablecemos el hilo principal
        Dispatchers.resetMain()
    }


    @Test
    fun `test metodo listarProductos`() {

    }

}







