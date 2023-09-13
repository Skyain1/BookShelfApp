package com.example.bookshelf.App.View.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import com.example.bookshelf.App.Model.Item.User
import com.example.bookshelf.App.ViewModel.LoginViewModel
import com.example.bookshelf.R
import com.example.bookshelf.databinding.ActivityLoginBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val vm: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSignButtonSettings()

        vm.checkskip()

        vm.skip.observe(this) {
            if (it) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }
        }

        binding.toSign.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }

        vm.result.observe(this) {
            if (it) {
                successfulAuthorization()
            } else {
                showErrorToast("Ошибка входа")
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

                    val user = User(
                        email = binding.email.text.toString(),
                        password = binding.password.text.toString()
                    )
                    vm.onButtonClicked(user)
                    Toast.makeText(applicationContext, "Done!", Toast.LENGTH_SHORT).show()
                }
            }.start()
            true
        }
    }

    private fun successfulAuthorization() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showErrorToast(error: String) {
        Toast.makeText(this@LoginActivity, error, Toast.LENGTH_SHORT).show()
    }
}