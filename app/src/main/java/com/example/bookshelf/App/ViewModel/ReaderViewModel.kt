package com.example.bookshelf.App.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookshelf.App.Model.Item.Book
import com.example.bookshelf.App.Model.Item.BookMark
import com.example.bookshelf.App.Model.Item.Library
import com.example.bookshelf.App.Model.Item.MoreMarks
import com.example.bookshelf.App.Model.Item.WeekStat
import com.example.bookshelf.App.Utilities.CharSequenceAdapter
import com.example.bookshelf.App.Utilities.PreferenceManager
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(private val preferenceManager: PreferenceManager) :
    ViewModel() {
    val bokk = MutableLiveData<Book>()
    val library = MutableLiveData<Library>()
    private val marks = MutableLiveData<MoreMarks>()
    private var ReadCount = 0
    private lateinit var weekStat: WeekStat

    val ActiveBookmark = MutableLiveData<Boolean>()
    val SwipeBookMark = MutableLiveData<Boolean>()

    fun setBook(curName: String) {
        if (preferenceManager.getString("book_key") != null) {
            val jsonBookFromPrefs = preferenceManager.getString("book_key")
            val gson = GsonBuilder()
                .registerTypeAdapter(CharSequence::class.java, CharSequenceAdapter())
                .create()
            val bookFromPrefs = gson.fromJson(jsonBookFromPrefs, Library::class.java)
            library.value = bookFromPrefs
            bokk.value = library.value!!.books.find { it.Name == curName }
        }
    }

    fun setMarks(currentPosition: Int) {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        val newBM = BookMark(bokk.value!!.Name, dateFormat.format(currentDate), currentPosition)

        if (preferenceManager.getString("bookmarks_key") != null) {
            val jsonBookFromPrefs = preferenceManager.getString("bookmarks_key")
            val gson = GsonBuilder()
                .registerTypeAdapter(CharSequence::class.java, CharSequenceAdapter())
                .create()
            val bookmrki = gson.fromJson(jsonBookFromPrefs, MoreMarks::class.java)
            if (bookmrki.bookmarkis.any { it.BookName == newBM.BookName && (it.Page) == newBM.Page }) {
               ActiveBookmark.value=true
                Log.d("Yatoro", "оно есть")
            }
            marks.value = bookmrki
        }
    }

    fun checkBookMark(currentPosition: Int){
        val gson = GsonBuilder()
            .registerTypeAdapter(CharSequence::class.java, CharSequenceAdapter())
            .create()

        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        val newBM = BookMark(bokk.value!!.Name, dateFormat.format(currentDate), currentPosition+1)
        if (preferenceManager.getString("bookmarks_key") != null && !marks.value!!.bookmarkis.any { it.BookName == newBM.BookName && (it.Page) == newBM.Page}) {
            ActiveBookmark.value=true
            marks.value!!.bookmarkis.add(newBM)
            val jsonBook = gson.toJson(marks.value)
            Log.d("Yatoro",(marks.value!!.bookmarkis.any { it.BookName == newBM.BookName && (it.Page) == (newBM.Page)}).toString())
            Log.d("Yatoro","добавляет")
            preferenceManager.putString("bookmarks_key", jsonBook)

        } else {
            val newBmList = ArrayList<BookMark>()
            newBmList.add(newBM)
            val nbmarks = MoreMarks(newBmList)
            if (preferenceManager.getString("bookmarks_key") != null &&(marks.value!!.bookmarkis.any { it.BookName == newBM.BookName && (it.Page) == (newBM.Page)}) ) {
                marks.value!!.bookmarkis.remove(newBM)
                val jsonBook = gson.toJson(marks.value)
                preferenceManager.putString("bookmarks_key", jsonBook)
                ActiveBookmark.value=false

            } else {
                val jsonBook = gson.toJson(nbmarks)
                ActiveBookmark.value=true
                preferenceManager.putString("bookmarks_key", jsonBook)
                Log.d("Yatoro","не то что надо")
            }
        }
    }

    fun swipeView(dir : Boolean, currentPosition: Int){
        if (dir) {
            ReadCount++
        }

        if (preferenceManager.getString("bookmarks_key") != null) {

            val jsonBookFromPrefs = preferenceManager.getString("bookmarks_key")
            val gson = GsonBuilder()
                .registerTypeAdapter(CharSequence::class.java, CharSequenceAdapter())
                .create()
             val bookmrki = gson.fromJson(jsonBookFromPrefs, MoreMarks::class.java)
            SwipeBookMark.value = bookmrki.bookmarkis.any { it.BookName == bokk.value!!.Name && (it.Page) == (currentPosition+1)}
        }
    }

    fun stop (currentPosition: Int){

        for (book in library.value!!.books) {
            book.isActive = 0
            if (book == bokk.value) {
                book.isActive = 1
            }
        }
        library.value!!.books[library.value!!.books.indexOf(bokk.value)].ProgressPage =
            currentPosition + 1
        val gson = GsonBuilder()
            .registerTypeAdapter(CharSequence::class.java, CharSequenceAdapter())
            .create()
        val jsonBook = gson.toJson(library.value)
        preferenceManager.putString("book_key", jsonBook)


        if (preferenceManager.getString("daystat_key") != null) {

            val jsonBookFromPrefs = preferenceManager.getString("daystat_key")
            val gson = GsonBuilder()
                .registerTypeAdapter(CharSequence::class.java, CharSequenceAdapter())
                .create()
            weekStat = gson.fromJson(jsonBookFromPrefs, WeekStat::class.java)

            Log.d("Yatoro", weekStat.toString())

            if (weekStat.Days != getCurrentDayOfYear()) {
                weekStat.Days = getCurrentDayOfYear()
                getTodayDayOfWeekName2()
                val jsonStat = gson.toJson(weekStat)
                preferenceManager.putString("daystat_key", jsonStat)
                Log.d("Yatoro", "День не тот же")
            } else {
                getTodayDayOfWeekName()
                val jsonStat = gson.toJson(weekStat)
                preferenceManager.putString("daystat_key", jsonStat)
                Log.d("Yatoro", " Тот же")
            }

        } else {
            val stat = WeekStat(0, 0, 0, 0, 0, 0, 0, getCurrentDayOfYear())
            val jsonStat = gson.toJson(stat)
            preferenceManager.putString("daystat_key", jsonStat)
        }
        ReadCount = 0
    }

    private fun getTodayDayOfWeekName() {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        when (dayOfWeek) {
            Calendar.MONDAY -> weekStat.Mo += (ReadCount)
            Calendar.TUESDAY -> weekStat.Tu += (ReadCount)
            Calendar.WEDNESDAY -> weekStat.We += (ReadCount)
            Calendar.THURSDAY -> weekStat.Th += (ReadCount)
            Calendar.FRIDAY -> weekStat.Fr += (ReadCount)
            Calendar.SATURDAY -> weekStat.Sa += (ReadCount)
            Calendar.SUNDAY -> weekStat.Su += (ReadCount)
            else -> Log.d("Yatoro", "Неверный день недели")
        }
    }

    private fun getTodayDayOfWeekName2() {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        when (dayOfWeek) {
            Calendar.MONDAY -> weekStat.Mo = (ReadCount - 1)
            Calendar.TUESDAY -> weekStat.Tu = (ReadCount - 1)
            Calendar.WEDNESDAY -> weekStat.We = (ReadCount - 1)
            Calendar.THURSDAY -> weekStat.Th = (ReadCount - 1)
            Calendar.FRIDAY -> weekStat.Fr = (ReadCount - 1)
            Calendar.SATURDAY -> weekStat.Sa = (ReadCount - 1)
            Calendar.SUNDAY -> weekStat.Su = (ReadCount - 1)
            else -> Log.d("Yatoro", "Неверный день недели")
        }
    }

    private fun getCurrentDayOfYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_YEAR)
    }
}