package com.example.bookshelf.App.View.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Patterns
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import com.example.bookshelf.App.ViewModel.SignUpViewModel
import com.example.bookshelf.R
import com.example.bookshelf.databinding.ActivitySignUpBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val vm: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSignButtonSettings()

        binding.toLogin.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
        }

        vm.result.observe(this) {
            if (it) {
                successfulRegistration()
            } else {
                showErrorToast("Ошибка регистрации")
            }
        }
    }

    private fun setSignButtonSettings() {
        val btn = this.findViewById<androidx.cardview.widget.CardView>(R.id.btnProgress)
        val bar = btn.findViewById<ProgressBar>(R.id.progressBar)
        val toVibrate = getSystemService(VIBRATOR_SERVICE) as Vibrator

        btn.setOnLongClickListener {
            object : CountDownTimer(200, 1) {
                override fun onTick(millisUntilFinished: Long) {
                    if (!btn.isPressed) {
                        bar.progress = 0
                        cancel()
                    } else {
                        bar.progress = 15 + bar.progress
                    }
                }

                override fun onFinish() {
                    bar.progress = 0
                    val effect =
                        VibrationEffect.createOneShot(100, 1)
                    toVibrate.vibrate(effect)

                    vm.onButtonClicked(
                        binding.email.text.toString(),
                        binding.password.text.toString(),)
                    Toast.makeText(applicationContext, "Done!", Toast.LENGTH_SHORT).show()
                }
            }.start()
            true
        }
    }

    private fun successfulRegistration() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun showErrorToast(error: String) {
        Toast.makeText(this@SignUpActivity, error, Toast.LENGTH_SHORT).show()
    }
}