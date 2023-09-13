package com.example.bookshelf.App.View.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookshelf.App.Model.Item.Page
import com.example.bookshelf.R

class PagerAdapter (private val pages: List<Page>) : RecyclerView.Adapter<PagerAdapter.PageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.page_viewholder, parent, false)
        return PageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        val pageContent = pages[position]
        holder.bind(pageContent)
    }

    override fun getItemCount() = pages.size

    class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pageTextView: TextView = itemView.findViewById(R.id.body)
        private val title : TextView = itemView.findViewById(R.id.title)
        fun bind(pageContent: Page) {
            pageTextView.text = pageContent.text
            if(pageContent.ViewType!=2){
                title.visibility = View.GONE
            }else{
                title.visibility = View.VISIBLE
            }
            title.text = pageContent.Title
        }
    }
}