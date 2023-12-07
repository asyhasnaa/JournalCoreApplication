package com.dicoding.intermediate.mystoryapp.ui.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.intermediate.mystoryapp.R
import com.dicoding.intermediate.mystoryapp.ui.main.MainActivity
import com.dicoding.intermediate.mystoryapp.ui.utils.ViewModelFactory
import com.dicoding.intermediate.mystoryapp.ui.utils.getImageUri
import com.dicoding.intermediate.mystoryapp.ui.utils.reduceFileImage
import com.dicoding.intermediate.mystoryapp.ui.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import com.dicoding.intermediate.mystoryapp.data.ResultState
import com.dicoding.intermediate.mystoryapp.databinding.ActivityAddBinding
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private var currentImageUri: Uri? = null
    private lateinit var addStoryViewModel: AddViewModel

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        addStoryViewModel = ViewModelProvider(this, factory)[AddViewModel::class.java]

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        addStoryViewModel.uploadStoryResponse.observe(this) {
            when (it) {
                is ResultState.Loading

                -> {
                    showLoading(true)
                }
                is ResultState.Success -> {
                    showLoading(false)
                    AlertDialog.Builder(this).apply {
                        setTitle("Yeah!")
                        setMessage(getString(R.string.upload_dialog_message))
                        setCancelable(false)
                        setPositiveButton(getString(R.string.dialog_positive_button)) { _, _ ->
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }
                is ResultState.Error -> {
                    showLoading(false)
                }

                else -> {}
            }
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }

        setupActionBar()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = getString(R.string.upload_page_title)
            setDisplayHomeAsUpEnabled(true)
            setBackgroundDrawable(ContextCompat.getDrawable(this@AddActivity, R.color.orange))
        }
    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        }else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun uploadImage() {
        var token: String
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = binding.edtDescription.text.toString()
            showLoading(true)

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            addStoryViewModel.getSession().observe(this) { user ->
                token = user.token
                addStoryViewModel.uploadStory(token, multipartBody, requestBody)
            }

        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}