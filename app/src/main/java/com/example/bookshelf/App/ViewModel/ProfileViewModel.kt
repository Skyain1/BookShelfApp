package com.example.bookshelf.App.ViewModel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextPaint
import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookshelf.App.Domain.PageSplitter
import com.example.bookshelf.App.Model.Item.Book
import com.example.bookshelf.App.Model.Item.Chapter
import com.example.bookshelf.App.Model.Item.Library
import com.example.bookshelf.App.Utilities.CharSequenceAdapter
import com.example.bookshelf.App.Utilities.PreferenceManager
import com.example.bookshelf.R
import com.google.gson.GsonBuilder
import com.kursx.parser.fb2.FictionBook
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val preferenceManager: PreferenceManager) :
    ViewModel() {

    val image = MutableLiveData<Bitmap>()
    val library = MutableLiveData<Library>()

    fun putImage(imageByteArray: ByteArray) {
        val encodedByteArray = Base64.encodeToString(imageByteArray, Base64.DEFAULT)
        preferenceManager.putString("image_key", encodedByteArray)
    }

    fun logOut() {
        preferenceManager.putString("skip_key", "0")
    }

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

    fun setMainBook() {
        if (preferenceManager.getString("book_key") != null) {
            val jsonBookFromPrefs = preferenceManager.getString("book_key")
            val gson = GsonBuilder()
                .registerTypeAdapter(CharSequence::class.java, CharSequenceAdapter())
                .create()
            val bookFromPrefs = gson.fromJson(jsonBookFromPrefs, Library::class.java)
            library.value = bookFromPrefs
        }
    }

    fun parseFb2(fb2: FictionBook, pageSplitter: PageSplitter, textPaint: TextPaint) {
        val chapters = ArrayList<Chapter>()
        var countpages = 0;
        for (sec in fb2.body.sections) {
            // pageSplitter.append("\n \n", textPaint)
            for (ele in sec.elements) {
                if (ele.text != null) {
                    pageSplitter.append(ele.text + "\n\n ", textPaint)
                }
            }
            chapters.add(
                Chapter(
                    sec.titles[0].paragraphs[0].text,
                    1,
                    pageSplitter.getPages(),
                    countpages + 1
                )
            )
            countpages += pageSplitter.getPages().size
            Log.d("Yatoro", countpages.toString())
        }

        val book = Book(
            1,
            fb2.binaries.get("cover.jpg")?.binary.toString(),
            fb2.title,
            "Author Name",
            countpages,
            chapters,
            0,
            0
        )
        val gson = GsonBuilder()
            .registerTypeAdapter(CharSequence::class.java, CharSequenceAdapter())
            .create()

        if (preferenceManager.getString("book_key") != null) {
            val jsonBookFromPrefs = preferenceManager.getString("book_key")
            val gson = GsonBuilder()
                .registerTypeAdapter(CharSequence::class.java, CharSequenceAdapter())
                .create()
            val libraryFromPrefs = gson.fromJson(jsonBookFromPrefs, Library::class.java)
            val booksFromPrefs = libraryFromPrefs?.books
            if (!booksFromPrefs.isNullOrEmpty()) {
                booksFromPrefs.add(book)
            }
            val library = booksFromPrefs?.let { Library(it) }
            val jsonBook = gson.toJson(library)
            preferenceManager.putString("book_key", jsonBook)
            Log.d("Yatoro", "Не первая книга и она по счёту ${booksFromPrefs?.size}")
        } else {
            val library = Library(listOf(book) as MutableList<Book>)
            val jsonBook = gson.toJson(library)
            preferenceManager.putString("book_key", jsonBook)
        }
    }
}