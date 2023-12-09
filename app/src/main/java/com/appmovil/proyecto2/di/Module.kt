package com.appmovil.proyecto2.di

import androidx.lifecycle.MutableLiveData
import com.appmovil.proyecto2.repository.InventoryRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideInventoryRepository(db: FirebaseFirestore, listProductos: MutableLiveData<String>): InventoryRepository {
        return InventoryRepository(db, listProductos)
    }
}