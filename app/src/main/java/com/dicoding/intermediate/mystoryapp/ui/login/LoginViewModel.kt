package com.dicoding.intermediate.mystoryapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.intermediate.mystoryapp.data.datastore.UserRepository
import com.dicoding.intermediate.mystoryapp.data.response.LoginResponse
import com.dicoding.intermediate.mystoryapp.data.ResultState

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResponse = MutableLiveData<ResultState<LoginResponse>>()
    val loginResponse: LiveData<ResultState<LoginResponse>> get() = _loginResponse

    fun login(email: String, password: String) {
        val loginResult = repository.login(email, password)
        loginResult.observeForever { result ->
            _loginResponse.value = result
        }
    }
}
