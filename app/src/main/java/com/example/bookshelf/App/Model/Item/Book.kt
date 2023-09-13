package com.example.bookshelf.App.Model.Item

import com.kursx.parser.fb2.Section

data class Book(
    val id: Int,
    val Skin: String,
    val Name: String,
    val Author: String,
    val AllCount : Int,
    val Chapters: ArrayList<Chapter>,
    var ProgressPage : Int,
    var isActive : Int
)
