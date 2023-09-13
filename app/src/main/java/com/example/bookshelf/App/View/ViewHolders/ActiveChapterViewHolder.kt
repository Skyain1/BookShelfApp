package com.example.bookshelf.App.View.ViewHolders

import androidx.recyclerview.widget.RecyclerView
import com.example.bookshelf.App.Model.Item.Chapter
import com.example.bookshelf.R
import com.example.bookshelf.databinding.ChapterActiveViewholderBinding

class ActiveChapterViewHolder(private val binding: ChapterActiveViewholderBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bind(chapter: Chapter){
            binding.title.text = chapter.Title
            binding.number.text = chapter.Number.toString()
                binding.galochka.setImageResource(0)
        }

}