package com.dicoding.intermediate.mystoryapp.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dicoding.intermediate.mystoryapp.R
import com.dicoding.intermediate.mystoryapp.data.response.ListStoryItem
import com.dicoding.intermediate.mystoryapp.databinding.ActivityDetailBinding

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private lateinit var storyItem: ListStoryItem
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        getIntentData()
        setData()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = getString(R.string.detail_page_title)
            setDisplayHomeAsUpEnabled(true)
            setBackgroundDrawable(ContextCompat.getDrawable(this@DetailActivity, R.color.orange))
        }
    }

    private fun getIntentData() {
        storyItem = intent.getParcelableExtra("storyItem")!!
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setData() {
        Glide.with(this)
            .load(storyItem.photoUrl)
            .fitCenter()
            .into(binding.ivProfilePhoto)

        binding.tvName.text = storyItem.name
        binding.tvDescription.text = storyItem.description
    }
}
