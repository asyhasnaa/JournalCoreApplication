@file:Suppress("unused")

package com.dicoding.intermediate.mystoryapp.data.datastore

import androidx.lifecycle.LiveData

import androidx.lifecycle.liveData
import com.dicoding.intermediate.mystoryapp.data.ResultState
import com.dicoding.intermediate.mystoryapp.data.UserPreference
import com.dicoding.intermediate.mystoryapp.data.response.UserModel
import com.dicoding.intermediate.mystoryapp.data.response.AddStoryResponse
import com.dicoding.intermediate.mystoryapp.data.response.ListStoryItem
import com.dicoding.intermediate.mystoryapp.data.response.LoginResponse
import com.dicoding.intermediate.mystoryapp.data.response.RegisterResponse
import com.dicoding.intermediate.mystoryapp.data.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val localDataRepository: LocalDataRepository
) {

    fun getSession(): Flow<UserModel> = userPreference.getSession()

    suspend fun logout() {
        userPreference.logout()
    }

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<ResultState<RegisterResponse>> = createResultLiveData {
        apiService.register(name, email, password)
    }

    fun login(email: String, password: String): LiveData<ResultState<LoginResponse>> = createResultLiveData {
        val response = apiService.login(email, password)
        val token = response.loginResult.token
        saveSession(UserModel(email, token))
        response
    }

    fun getAllStories(token: String): LiveData<ResultState<List<ListStoryItem>>> = createResultLiveData {
        apiService.getAllStories("Bearer $token").listStory
    }

    fun addStory(token: String, file: MultipartBody.Part, description: RequestBody): LiveData<ResultState<AddStoryResponse>> =
        createResultLiveData {
            apiService.uploadStory("Bearer $token", file, description)
        }

    fun getLocale(): Flow<String> = localDataRepository.getLocaleSetting()

    suspend fun saveLocale(locale: String) {
        localDataRepository.saveLocaleSetting(locale)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            localDataRepository: LocalDataRepository
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference, localDataRepository).also { instance = it }
            }
    }

    private inline fun <T> createResultLiveData(crossinline networkCall: suspend () -> T): LiveData<ResultState<T>> =
        liveData(Dispatchers.IO) {
            emit(ResultState.Loading)
            try {
                val response = networkCall()
                emit(ResultState.Success(response))
            } catch (e: Exception) {
                emit(ResultState.Error(e.message.toString()))
            }
        }

    private suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }
}
