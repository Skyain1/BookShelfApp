package com.example.bookshelf.App.View.ViewHolders

import androidx.recyclerview.widget.RecyclerView
import com.example.bookshelf.App.Model.Item.Chapter
import com.example.bookshelf.App.View.Listeners.Chapter_Listener
import com.example.bookshelf.R
import com.example.bookshelf.databinding.ChapterStockViewholderBinding

class StockChapterViewHolder (private val binding: ChapterStockViewholderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(chapter: Chapter,listener: Chapter_Listener){
        binding.title.text = chapter.Title
        binding.number.text = chapter.Number.toString()

        binding.btBookmarks.setOnClickListener {
            listener.onChapterClicked(chapter)
        }

        if (chapter.Success==0){
            binding.galochka.setImageResource(R.color.white)
        }else{
            binding.galochka.setImageResource(R.drawable.icon_check)
        }
    }

}