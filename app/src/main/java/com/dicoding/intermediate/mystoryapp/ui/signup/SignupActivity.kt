package com.dicoding.intermediate.mystoryapp.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.intermediate.mystoryapp.R
import com.dicoding.intermediate.mystoryapp.customview.CustomButton
import com.dicoding.intermediate.mystoryapp.customview.CustomEmail
import com.dicoding.intermediate.mystoryapp.customview.CustomPass
import com.dicoding.intermediate.mystoryapp.data.ResultState
import com.dicoding.intermediate.mystoryapp.databinding.ActivitySignupBinding
import com.dicoding.intermediate.mystoryapp.ui.login.LoginActivity
import com.dicoding.intermediate.mystoryapp.ui.utils.ViewModelFactory

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var customButton: CustomButton
    private lateinit var customEmail: CustomEmail
    private lateinit var customPass: CustomPass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupActions()
        playAnimation()
    }

    private fun setupViews() {
        binding.apply {
            showPassword.setOnClickListener {
                togglePasswordVisibility()
            }

            textLogin.setOnClickListener {
                navigateToLoginScreen()
            }

            customButton = signupButton
            customButton.isEnabled = true
            customEmail = inputEmail
            customPass = inputPassword

            val factory: ViewModelFactory = ViewModelFactory.getInstance(this@SignupActivity)
            signUpViewModel = ViewModelProvider(this@SignupActivity, factory)[SignUpViewModel::class.java]
        }
    }

    private fun togglePasswordVisibility() {
        binding.inputPassword.transformationMethod =
            if (binding.showPassword.isChecked) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun setupActions() {
        binding.signupButton.setOnClickListener {
            binding.apply {
                if (inputName.error.isNullOrEmpty() && inputEmail.error.isNullOrEmpty() && inputPassword.error.isNullOrEmpty()) {
                    val name = inputName.text.toString().trim()
                    val email = inputEmail.text.toString().trim()
                    val password = inputPassword.text.toString().trim()
                    signUpViewModel.register(name, email, password)
                } else {
                    registerFailedToast()
                }
            }
        }

        binding.inputPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setCustomButtonEnable(s.isNotEmpty())
            }

            override fun afterTextChanged(s: Editable) {}
        })

        signUpViewModel.registerResponse.observe(this) { response ->
            when (response) {
                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    showLoading(false)
                    showSuccessDialog()
                }
                is ResultState.Error -> {
                    showLoading(false)
                    showErrorDialog()
                }
                else -> {
                }
            }
        }
    }

    private fun setCustomButtonEnable(isEnabled: Boolean) {
        binding.signupButton.isEnabled = isEnabled
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun registerFailedToast() {
        Toast.makeText(
            this,
            R.string.register_failed,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage(getString(R.string.register_dialog_message))
            setCancelable(false)
            setPositiveButton(getString(R.string.dialog_positive_button)) { _, _ ->
                navigateToLoginScreen()
            }
            create()
            show()
        }
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.register_failed_title))
            setMessage(getString(R.string.register_failed))
            create()
            show()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageSignup, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tittleSignup, View.ALPHA, 1f).setDuration(100)
        val name = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(100)
        val nameLayout = ObjectAnimator.ofFloat(binding.nameLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(100)
        val emailLayout = ObjectAnimator.ofFloat(binding.emailLayout, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(100)
        val passwordLayout = ObjectAnimator.ofFloat(binding.passwordLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)
        val showPass = ObjectAnimator.ofFloat(binding.showPassword, View.ALPHA, 1f).setDuration(100)
        val textLogin = ObjectAnimator.ofFloat(binding.textLogin, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                name,
                nameLayout,
                emailTextView,
                emailLayout,
                password,
                passwordLayout,
                showPass,
                textLogin,
                signup
            )
            startDelay = 100
        }.start()
    }
}