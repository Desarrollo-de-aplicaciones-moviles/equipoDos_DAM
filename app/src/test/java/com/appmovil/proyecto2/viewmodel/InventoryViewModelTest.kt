package com.appmovil.proyecto2.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.appmovil.proyecto2.repository.InventoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
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
        val precio = 10.0
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

    @Test
    fun `test metodo actualizarProducto`() = runBlockingTest {
        //given
        val codigo = 15
        val nombre = "NuevoNombre"
        val precio = 20.0
        val cantidad = 8

        `when`(repository.actualizarProducto(codigo, nombre, precio, cantidad, inventoryViewModel.productoActualizado))
            .thenAnswer {
                inventoryViewModel.productoActualizado.postValue(true)
            }

        // Llamamos al método que queremos probar
        val result = inventoryViewModel.actualizarProducto(codigo, nombre, precio, cantidad)

        // Verificamos que el método en el repositorio haya sido llamado con los mismos argumentos
        verify(repository).actualizarProducto(codigo, nombre, precio, cantidad, inventoryViewModel.productoActualizado)

        // Verificamos que el resultado sea el esperado
        assert(result)
        // Verificamos que el LiveData en el ViewModel ha sido actualizado
        assertEquals(true, inventoryViewModel.productoActualizado.value)
    }

    @Test
    fun `test metodo eliminarProducto`() = runBlockingTest {
        //given
        val codigo = 15

        `when`(repository.eliminarProducto(codigo, inventoryViewModel.productoEliminado))
            .thenAnswer {
                inventoryViewModel.productoEliminado.postValue(true)
            }

        // Llamamos al método que queremos probar
        val result = inventoryViewModel.eliminarProducto(codigo)

        // Verificamos que el método en el repositorio haya sido llamado con los mismos argumentos
        verify(repository).eliminarProducto(codigo, inventoryViewModel.productoEliminado)

        // Verificamos que el resultado sea el esperado
        assert(result)
        // Verificamos que el LiveData en el ViewModel ha sido actualizado
        assertEquals(true, inventoryViewModel.productoEliminado.value)
    }

    @Test
    fun `test metodo calcularValorTotalProducto`() {
        // Given
        val precio = 10.0
        val cantidad = 5

        // When
        val result = inventoryViewModel.calcularValorTotalProducto(precio, cantidad)

        // Then
        assertEquals(50.0, result,0.001)
    }

    @Test
    fun `test metodo obtenerTotalProductos`() {
        // Simula la respuesta del repositorio
        val totalMock = MutableLiveData<Double>()
        totalMock.value = 100.0
        `when`(repository.totalInventario()).thenReturn(totalMock)

        // Observador para el LiveData que se espera sea actualizado
        val observer = mock(Observer::class.java) as Observer<Double>

        // Observar el LiveData y verificar si se actualiza correctamente
        inventoryViewModel.obtenerTotalProductos().observeForever(observer)

        // Verifica que el método en el repositorio haya sido llamado
        verify(repository).totalInventario()

        // Verifica que el LiveData en el ViewModel ha sido actualizado con la data simulada
        verify(observer).onChanged(100.0)
    }
}








