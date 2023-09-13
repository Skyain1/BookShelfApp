package com.example.bookshelf.App.ViewModel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookshelf.App.Model.Item.MoreMarks
import com.example.bookshelf.App.Utilities.CharSequenceAdapter
import com.example.bookshelf.App.Utilities.PreferenceManager
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(private val preferenceManager: PreferenceManager) :
    ViewModel() {
    val image = MutableLiveData<Bitmap>()
    val marks = MutableLiveData<MoreMarks>()

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

    fun setMarks(){
        if (preferenceManager.getString("bookmarks_key") != null) {
            val jsonBookFromPrefs = preferenceManager.getString("bookmarks_key")
            val gson = GsonBuilder()
                .registerTypeAdapter(CharSequence::class.java, CharSequenceAdapter())
                .create()
            marks.value = gson.fromJson(jsonBookFromPrefs, MoreMarks::class.java)
        }
    }

    fun deleteMark(){
        val gson = GsonBuilder()
            .registerTypeAdapter(CharSequence::class.java, CharSequenceAdapter())
            .create()
        val jsonBook = gson.toJson(marks.value)
        preferenceManager.putString("bookmarks_key", jsonBook)
    }

}