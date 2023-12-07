package com.dicoding.intermediate.mystoryapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.intermediate.mystoryapp.R
import com.dicoding.intermediate.mystoryapp.data.ResultState
import com.dicoding.intermediate.mystoryapp.databinding.ActivityLoginBinding
import com.dicoding.intermediate.mystoryapp.ui.main.MainActivity
import com.dicoding.intermediate.mystoryapp.ui.signup.SignupActivity
import com.dicoding.intermediate.mystoryapp.ui.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupAction()
        playAnimation()
    }

    private fun setupViews() {
        binding.apply {
            showPassword.setOnClickListener {
                showPassword()
            }

            textRegister.setOnClickListener {
                navigateToSignupScreen()
            }

            loginButton.setOnClickListener {
                val emailInput = inputEmail.text.toString().trim()
                val passwordInput = inputPassword.text.toString().trim()
                if (emailInput.isNotEmpty() && passwordInput.isNotEmpty()) {
                    loginViewModel.login(emailInput, passwordInput)
                }
            }

            inputPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    setButtonEnable(s.isNotEmpty())
                }

                override fun afterTextChanged(s: Editable) {}
            })
        }
    }

    private fun showPassword() {
        binding.inputPassword.transformationMethod =
            if (binding.showPassword.isChecked) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }
    }

    private fun navigateToSignupScreen() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }

    private fun setButtonEnable(isEnabled: Boolean) {
        binding.loginButton.isEnabled = isEnabled
    }

    private fun setupAction() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        loginViewModel.loginResponse.observe(this) { response ->
            when (response) {
                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    showLoading(false)
                    showSuccessDialog()
                }
                else -> {
                    showErrorDialog()
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Welcome back!")
            setMessage(getString(R.string.login_dialog))
            setPositiveButton(getString(R.string.dialog_positive_button)) { _, _ ->
                navigateToMainActivity()
            }
            create()
            show()
        }
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.login_failed_title))
            setMessage(getString(R.string.login_failed))
            create()
            show()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title =
            ObjectAnimator.ofFloat(binding.tittleLogin, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageLogin, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(100)
        val emailLayout =
            ObjectAnimator.ofFloat(binding.emailLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(100)
        val passwordLayout =
            ObjectAnimator.ofFloat(binding.passwordLayout, View.ALPHA, 1f).setDuration(100)
        val showPassword =
            ObjectAnimator.ofFloat(binding.showPassword, View.ALPHA, 1f).setDuration(100)
        val login =
            ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)
        val createAcc =
            ObjectAnimator.ofFloat(binding.textRegister, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailLayout,
                passwordTextView,
                passwordLayout,
                showPassword,
                login,
                createAcc
            )
            startDelay = 100
        }.start()
    }
}