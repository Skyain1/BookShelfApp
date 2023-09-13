package com.example.bookshelf.App.View.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookshelf.App.Model.Item.Chapter
import com.example.bookshelf.App.View.Listeners.Chapter_Listener
import com.example.bookshelf.App.View.ViewHolders.ActiveChapterViewHolder
import com.example.bookshelf.App.View.ViewHolders.StockChapterViewHolder
import com.example.bookshelf.databinding.ChapterActiveViewholderBinding
import com.example.bookshelf.databinding.ChapterStockViewholderBinding
import java.lang.IllegalArgumentException

class ChaptersAdapter(private val list: ArrayList<Chapter>, val listener : Chapter_Listener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        const val STOCK_VIEW=1
        const val ACTIVE_VIEW=2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            STOCK_VIEW-> StockChapterViewHolder(ChapterStockViewholderBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            ACTIVE_VIEW-> ActiveChapterViewHolder(ChapterActiveViewholderBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            else-> throw IllegalArgumentException("invalid item type")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return when(list[position].ViewType){
            STOCK_VIEW ->(holder as StockChapterViewHolder).bind(list[position],listener)
            ACTIVE_VIEW ->(holder as ActiveChapterViewHolder).bind(list[position])
            else-> throw IllegalArgumentException("invalid item type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].ViewType
    }
}