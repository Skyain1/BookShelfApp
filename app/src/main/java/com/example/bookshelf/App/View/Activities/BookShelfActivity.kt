package com.example.bookshelf.App.View.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.bookshelf.App.Model.Item.Book
import com.example.bookshelf.App.View.Adapters.BookShelfAdapter
import com.example.bookshelf.App.View.Listeners.BookShelf_Listener
import com.example.bookshelf.App.ViewModel.BookShelfViewModel
import com.example.bookshelf.databinding.ActivityBookShelfBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookShelfActivity : AppCompatActivity(), BookShelf_Listener {

    private lateinit var binding: ActivityBookShelfBinding
    private val vm: BookShelfViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookShelfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vm.setMainImage()
        vm.setBookList()

        vm.image.observe(this) {
            binding.roundedImageView.setImageBitmap(it)
        }

        vm.library.observe(this){
            val adapter = BookShelfAdapter(it.books, this)
            binding.bookshelf.adapter = adapter
            if (it.books.size == 1) {
                binding.counter.text = it.books.size.toString() + " book"
            } else {
                binding.counter.text = it.books.size.toString() + " books"
            }
        }

        binding.toBookMark.setOnClickListener {
            startActivity(Intent(this@BookShelfActivity, BookmarksActivity::class.java))
        }

        binding.toMenu.setOnClickListener {
            startActivity(Intent(this@BookShelfActivity, ProfileActivity::class.java))
        }
        binding.roundedImageView.setOnClickListener {
            startActivity(Intent(this@BookShelfActivity, ProfileActivity::class.java))
        }
    }

    override fun onBookClicked(book: Book) {
        val intent = Intent(this@BookShelfActivity,ReaderActivity::class.java)
        intent.putExtra("ClickedBook",book.Name)
        startActivity(intent)
    }
}
