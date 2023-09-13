package com.example.bookshelf.App.View.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.bookshelf.App.Model.Item.BookMark
import com.example.bookshelf.App.Model.Item.MoreMarks
import com.example.bookshelf.App.View.Adapters.BookmarkAdapter
import com.example.bookshelf.App.View.Listeners.BookMark_Listener
import com.example.bookshelf.App.View.Listeners.SwipeToDeleteCallback
import com.example.bookshelf.App.ViewModel.BookmarksViewModel
import com.example.bookshelf.databinding.ActivityBookmarksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarksActivity : AppCompatActivity(), BookMark_Listener {

    private lateinit var binding: ActivityBookmarksBinding
    private val vm: BookmarksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm.setMainImage()
        vm.setMarks()

        binding.back.setOnClickListener {
            onBackPressed()
        }
        vm.image.observe(this) {
            binding.roundedImageView.setImageBitmap(it)
        }

        vm.marks.observe(this){
            val adapter = BookmarkAdapter(it.bookmarkis, this)
            val swipeToDeleteCallback = SwipeToDeleteCallback(adapter) { viewHolder ->
                // Обработка свайпа элемента
                val position = viewHolder.adapterPosition
                adapter.removeItem(position)
                vm.deleteMark()
            }
            binding.chapters.adapter = adapter
            adapter.notifyDataSetChanged()
            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(binding.chapters)
        }
    }

    override fun onBookMarkClicked(bookMark: BookMark) {
        val intent = Intent(this@BookmarksActivity, ReaderActivity::class.java)
        intent.putExtra("Chapter", bookMark.Page)
        intent.putExtra("ClickedBook", bookMark.BookName)
        startActivity(intent)
    }
}