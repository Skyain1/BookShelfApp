package com.example.bookshelf.App.View.Listeners

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDeleteCallback(
    private val adapter: RecyclerView.Adapter<*>,
    private val onSwiped: (RecyclerView.ViewHolder) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // Не нужно обрабатывать перемещение элементов в данном случае
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwiped(viewHolder)
    }
}