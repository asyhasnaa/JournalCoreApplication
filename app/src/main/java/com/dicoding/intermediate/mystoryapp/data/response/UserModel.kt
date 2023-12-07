package com.dicoding.intermediate.mystoryapp.data.response

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)