package com.dicoding.intermediate.mystoryapp

import com.dicoding.intermediate.mystoryapp.data.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                photoUrl = "url_$i",
                createdAt = "created_at_$i",
                name = "author_$i",
                description = "story_$i",
                lon = 0.0,
                id = "$i",
                lat = 0.0
            )
            items.add(story)
        }
        return items
    }
}