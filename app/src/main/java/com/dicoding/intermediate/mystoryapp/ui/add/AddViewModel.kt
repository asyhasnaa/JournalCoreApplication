package com.dicoding.intermediate.mystoryapp.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.intermediate.mystoryapp.data.response.UserModel
import com.dicoding.intermediate.mystoryapp.data.datastore.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.dicoding.intermediate.mystoryapp.data.ResultState
import com.dicoding.intermediate.mystoryapp.data.response.AddStoryResponse

class AddViewModel(private val repository: UserRepository): ViewModel() {

    private val _addStoryResponse = MediatorLiveData<ResultState<AddStoryResponse>>()
    val uploadStoryResponse: LiveData<ResultState<AddStoryResponse>> = _addStoryResponse

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody) {
        val liveData = repository.addStory(token, file, description)
        _addStoryResponse.addSource(liveData) { result ->
            _addStoryResponse.value = result
        }
    }
}