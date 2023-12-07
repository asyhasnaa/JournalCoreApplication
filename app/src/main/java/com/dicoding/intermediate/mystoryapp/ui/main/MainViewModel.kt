package com.dicoding.intermediate.mystoryapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.intermediate.mystoryapp.data.datastore.UserRepository
import com.dicoding.intermediate.mystoryapp.data.response.UserModel
import com.dicoding.intermediate.mystoryapp.data.response.ListStoryItem
import kotlinx.coroutines.launch
import com.dicoding.intermediate.mystoryapp.data.ResultState

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _storyList = MediatorLiveData<ResultState<List<ListStoryItem>>>()
    val storyList: LiveData<ResultState<List<ListStoryItem>>> = _storyList

    fun getAllStories(token: String) {
        val liveData = repository.getAllStories(token)
        _storyList.addSource(liveData) { result ->
            _storyList.value = result
        }
    }
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}