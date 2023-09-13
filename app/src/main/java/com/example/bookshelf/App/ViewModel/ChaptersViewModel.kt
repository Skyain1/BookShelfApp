package com.example.bookshelf.App.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookshelf.App.Model.Item.Book
import com.example.bookshelf.App.Model.Item.Chapter
import com.example.bookshelf.App.Model.Item.Library
import com.example.bookshelf.App.Utilities.CharSequenceAdapter
import com.example.bookshelf.App.Utilities.PreferenceManager
import com.example.bookshelf.App.View.Adapters.ChaptersAdapter
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChaptersViewModel @Inject constructor(private val preferenceManager: PreferenceManager) :
    ViewModel() {

    val AllChapters = MutableLiveData<ArrayList<Chapter>>()
    val bokk = MutableLiveData<Book>()


    fun setChapters(curname: String, page: Int) {
        if (preferenceManager.getString("book_key") != null) {
            val jsonBookFromPrefs = preferenceManager.getString("book_key")
            val gson = GsonBuilder()
                .registerTypeAdapter(CharSequence::class.java, CharSequenceAdapter())
                .create()

            val list = ArrayList<Chapter>()
            val bookFromPrefs = gson.fromJson(jsonBookFromPrefs, Library::class.java)
            val library = bookFromPrefs
            bokk.value = library.books.find { it.Name == curname }

            if (bokk != null) {
                for (sec in bokk.value!!.Chapters) {

                    list.add(sec)
                }

                var foundChapter: Chapter? = null

                for (chapter in list) {
                    if (chapter.Number <= page) {
                        foundChapter = chapter
                        chapter.Success = 1
                    } else {
                        chapter.Success = 0
                    }
                }
                list[list.indexOf(foundChapter)].ViewType = 2
                AllChapters.value = list
            }
        }
    }

}