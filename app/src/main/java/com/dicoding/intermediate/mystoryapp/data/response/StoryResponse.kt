package com.dicoding.intermediate.mystoryapp.data.response

import com.google.gson.annotations.SerializedName

data class StoryResponse(

    @field:SerializedName("listStory")
    val listStory: List<ListStoryItem> = emptyList(),

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

