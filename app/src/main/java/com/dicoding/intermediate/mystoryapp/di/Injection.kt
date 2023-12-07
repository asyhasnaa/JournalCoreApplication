package com.dicoding.intermediate.mystoryapp.di

import android.content.Context
import com.dicoding.intermediate.mystoryapp.data.datastore.UserRepository
import com.dicoding.intermediate.mystoryapp.data.datastore.LocalDataRepository
import com.dicoding.intermediate.mystoryapp.data.UserPreference
import com.dicoding.intermediate.mystoryapp.data.dataStore
import com.dicoding.intermediate.mystoryapp.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val locale = LocalDataRepository.getInstance(context.dataStore)
        return UserRepository.getInstance(apiService, pref, locale)
    }
}