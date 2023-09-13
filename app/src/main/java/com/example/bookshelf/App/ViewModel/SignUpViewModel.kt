package com.example.bookshelf.App.ViewModel

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookshelf.App.Model.Database.UserRepository
import com.example.bookshelf.App.Model.Item.User
import com.example.bookshelf.App.Utilities.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: UserRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    val result = MutableLiveData<Boolean>()

    fun onButtonClicked(email: String, password: String) {
        val user = User(email = email, password = password)

        if (validateInput(email, password)) {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    repository.insert(user)
                }
                result.value = true
            }
        } else {
            result.value = false
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
               return true
            } else {
                result.value = false
            }
        }
        return false
    }
}