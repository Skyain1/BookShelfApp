package com.example.bookshelf.App.View.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.bookshelf.App.Model.Item.Book

import com.example.bookshelf.App.Model.Item.Chapter
import com.example.bookshelf.App.Model.Item.Library
import com.example.bookshelf.App.Utilities.CharSequenceAdapter
import com.example.bookshelf.App.Utilities.PreferenceManager
import com.example.bookshelf.App.View.Adapters.ChaptersAdapter
import com.example.bookshelf.App.View.Listeners.Chapter_Listener
import com.example.bookshelf.App.ViewModel.BookmarksViewModel
import com.example.bookshelf.App.ViewModel.ChaptersViewModel
import com.example.bookshelf.databinding.ActivityChaptersBinding
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChaptersActivity : AppCompatActivity(), Chapter_Listener {

    private lateinit var binding: ActivityChaptersBinding
    private val vm: ChaptersViewModel by viewModels()

    private lateinit var curname : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChaptersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener { onBackPressed() }


        curname = intent.getStringExtra("ChapetrsBook").toString()
        val page = intent.getIntExtra("ChaptersPage", 1)
        curname.let { vm.setChapters(it, page) }

        vm.AllChapters.observe(this) {
            binding.chapters.adapter = ChaptersAdapter(it, this)

            val dd = (vm.bokk.value!!.AllCount).toDouble()
            binding.pb.setProgressPercentage(((page.toDouble() / dd)) * 100.toDouble(), true)
        }

    }


    override fun onChapterClicked(chapter: Chapter) {
        Log.d("Yatoro", chapter.Title)
        val intent = Intent(this@ChaptersActivity, ReaderActivity::class.java)
        intent.putExtra("Chapter", chapter.Number)
        intent.putExtra("ClickedBook", curname)
        startActivity(intent)
    }
}