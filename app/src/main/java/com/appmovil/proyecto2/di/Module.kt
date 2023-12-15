package com.appmovil.proyecto2.di

import androidx.lifecycle.MutableLiveData
import com.appmovil.proyecto2.model.Articulo
import com.appmovil.proyecto2.repository.InventoryRepository
import com.appmovil.proyecto2.repository.LoginRepository
import com.appmovil.proyecto2.viewmodel.LoginViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideFirestoreInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideListProductos(): MutableLiveData<String> {
        return MutableLiveData()
    }

    @Provides
    @Singleton
    fun provideInventoryList(): MutableLiveData<MutableList<Articulo>> {
        return MutableLiveData()
    }

    @Provides
    @Singleton
    fun provideInventoryRepository(db: FirebaseFirestore, listProductos: MutableLiveData<String>, inventoryList:MutableLiveData<MutableList<Articulo>>): InventoryRepository {
        return InventoryRepository(db, listProductos, inventoryList)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(): LoginRepository {
        return LoginRepository()
    }

    @Provides
    @Singleton
    fun provideLoginViewModel(repository: LoginRepository): LoginViewModel {
        return LoginViewModel(repository)
    }
}