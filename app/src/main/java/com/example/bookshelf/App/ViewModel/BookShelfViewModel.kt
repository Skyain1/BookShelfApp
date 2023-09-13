package com.example.bookshelf.App.ViewModel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookshelf.App.Model.Item.Library
import com.example.bookshelf.App.Utilities.CharSequenceAdapter
import com.example.bookshelf.App.Utilities.PreferenceManager
import com.example.bookshelf.App.View.Adapters.BookShelfAdapter
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookShelfViewModel @Inject constructor(private val preferenceManager: PreferenceManager) :
    ViewModel() {
    val image = MutableLiveData<Bitmap>()
    val library = MutableLiveData<Library>()

    fun setMainImage() {
        if (preferenceManager.getString("image_key") != null) {
            val encodedByteArray = preferenceManager.getString("image_key")
            if (encodedByteArray != null) {
                val byteArray =
                    android.util.Base64.decode(encodedByteArray, android.util.Base64.DEFAULT)
                val imbm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                image.value = imbm
            }
        }
    }

    fun setBookList(){
        if (preferenceManager.getString("book_key") != null) {
            val jsonBookFromPrefs = preferenceManager.getString("book_key")
            val gson = GsonBuilder()
                .registerTypeAdapter(CharSequence::class.java, CharSequenceAdapter())
                .create()
            library.value = gson.fromJson(jsonBookFromPrefs, Library::class.java)
        }
    }
}