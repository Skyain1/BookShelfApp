package com.example.bookshelf.App.View.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookshelf.App.Model.Item.BookMark
import com.example.bookshelf.App.View.Listeners.BookMark_Listener
import com.example.bookshelf.databinding.BookmarkViewholderBinding

class BookmarkAdapter(
    private var items: ArrayList<BookMark>,
    private val listener: BookMark_Listener,
) :
    RecyclerView.Adapter<BookmarkAdapter.BookMarkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkViewHolder {
        val binding = BookmarkViewholderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookMarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookMarkViewHolder, position: Int) {
        val bookmark = items[position]
        holder.bind(bookmark)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class BookMarkViewHolder(val binding: BookmarkViewholderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bookMark: BookMark) {
            binding.btBookmarks.setOnClickListener {
                listener.onBookMarkClicked(bookMark)
            }
            binding.title.text = bookMark.BookName + " - ${bookMark.Date}"
            binding.number.text = (bookMark.Page).toString()
        }
    }
}