package com.example.bookshelf.App.ViewModel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookshelf.App.Model.Database.UserRepository
import com.example.bookshelf.App.Model.Item.User
import com.example.bookshelf.App.Utilities.PreferenceManager
import com.example.bookshelf.App.View.Activities.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    init {
        Log.d("Yatoro", "LogVMCreated")
    }

    val result = MutableLiveData<Boolean>()
    val skip = MutableLiveData<Boolean>()

    fun onButtonClicked(user: User) {
        GlobalScope.launch(Dispatchers.Main) {
            var isHave = false
            withContext(Dispatchers.IO) {
                isHave = repository.checkField(user)
            }
            result.value = isHave
            if (result.value!!){
                preferenceManager.putString("skip_key", "1")
            }
        }
    }

    fun checkskip(){
        if (preferenceManager.getString("skip_key") != null && preferenceManager.getString("skip_key") != "0") {
         skip.value = true
        }
    }

}