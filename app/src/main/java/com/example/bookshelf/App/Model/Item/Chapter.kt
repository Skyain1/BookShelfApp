package com.example.bookshelf.App.Model.Item

data class Chapter(
    val Title: String,
    var ViewType: Int = 1 ,
    val Pages: List<CharSequence>,
    val Number: Int,
    var Success : Int=1
)
