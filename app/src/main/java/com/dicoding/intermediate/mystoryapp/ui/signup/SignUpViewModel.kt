package com.dicoding.intermediate.mystoryapp.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.intermediate.mystoryapp.data.datastore.UserRepository
import com.dicoding.intermediate.mystoryapp.data.response.RegisterResponse
import com.dicoding.intermediate.mystoryapp.data.ResultState

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {

    private val _registerResponse = MediatorLiveData<ResultState<RegisterResponse>>()
    val registerResponse: LiveData<ResultState<RegisterResponse>> get() = _registerResponse

    fun register(name: String, email: String, password: String) {
        val registrationResult = repository.register(name, email, password)
        _registerResponse.addSource(registrationResult) { result ->
            _registerResponse.value = result
        }
    }
}
