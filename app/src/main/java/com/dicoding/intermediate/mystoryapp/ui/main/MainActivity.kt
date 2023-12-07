package com.dicoding.intermediate.mystoryapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.intermediate.mystoryapp.R
import com.dicoding.intermediate.mystoryapp.data.adapter.StoryAdapter
import com.dicoding.intermediate.mystoryapp.data.ResultState
import com.dicoding.intermediate.mystoryapp.data.response.ListStoryItem
import com.dicoding.intermediate.mystoryapp.databinding.ActivityMainBinding
import com.dicoding.intermediate.mystoryapp.ui.add.AddActivity
import com.dicoding.intermediate.mystoryapp.ui.utils.ViewModelFactory
import com.dicoding.intermediate.mystoryapp.ui.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViewModel()
        setupRecyclerView()
        observeUserSession()
        observeStoryList()
        setupFAB()
        setupOptionMenu()
    }

    private fun initializeViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStoryList.layoutManager = layoutManager
    }

    private fun observeUserSession() {
        mainViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                navigateToWelcomeScreen()
            } else {
                mainViewModel.getAllStories(user.token)
            }
        }
    }


    private fun observeStoryList() {
        mainViewModel.storyList.observe(this) {
            when (it) {
                is ResultState.Loading -> showLoading(true)
                is ResultState.Error -> showLoading(false)
                is ResultState.Success -> {
                    showLoading(false)
                    displayStoryList(it.data)
                }
                else -> {}
            }
        }
    }

    private fun setupFAB() {
        binding.fabAddStory.setOnClickListener {
            startAddActivity()
        }
    }

    private fun setupOptionMenu() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_logout -> {
                    showLogoutConfirmationDialog()
                    true
                }
                R.id.menu_language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                else -> false
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.rvStoryList.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToWelcomeScreen() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }

    private fun startAddActivity() {
        val intent = Intent(this, AddActivity::class.java)
        startActivity(intent)
    }

    private fun displayStoryList(stories: List<ListStoryItem>) {
        adapter = StoryAdapter(stories)
        binding.rvStoryList.adapter = adapter
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.logout_alert_tittle))
            setMessage(getString(R.string.logout_alert_message))
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                mainViewModel.logout()
            }
            setNegativeButton(getString(R.string.no)) { _, _ -> }
            create().show()
        }
    }
}
