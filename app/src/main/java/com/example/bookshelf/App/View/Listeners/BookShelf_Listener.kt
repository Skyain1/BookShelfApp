package com.example.bookshelf.App.View.Listeners

import com.example.bookshelf.App.Model.Item.Book

interface BookShelf_Listener {
    fun onBookClicked(book: Book)
}