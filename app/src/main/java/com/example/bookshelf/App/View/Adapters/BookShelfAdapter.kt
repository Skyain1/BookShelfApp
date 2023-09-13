package com.example.bookshelf.App.View.Adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookshelf.App.Model.Item.Book
import com.example.bookshelf.App.View.Listeners.BookShelf_Listener
import com.example.bookshelf.databinding.BookViewholderBinding

class BookShelfAdapter(
    private val books: List<Book>,
    private val bookshelfListener: BookShelf_Listener,
) :
    RecyclerView.Adapter<BookShelfAdapter.BookShelfViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookShelfViewHolder {
        val binding = BookViewholderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookShelfViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookShelfViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book)
    }


    override fun getItemCount() = books.size
    inner class BookShelfViewHolder(val binding: BookViewholderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
             binding.Skin.setImageBitmap(decodebytes(book.Skin))
            binding.Skin.setOnClickListener {
                bookshelfListener.onBookClicked(book)
            }

        }

        private fun decodebytes(base64: String): Bitmap {
            val byteArray = android.util.Base64.decode(base64, android.util.Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    }



}