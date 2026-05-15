package com.example.sealspeak

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class VocabularyAdapter(
    private val categories: List<VocabularyCategory>,
    private val onClick: (VocabularyCategory) -> Unit
) : RecyclerView.Adapter<VocabularyAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val tvCount: TextView = view.findViewById(R.id.tvCount)
        val card: CardView = view.findViewById(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vocabulary, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.tvCategory.text = category.category
        holder.tvCount.text = "${category.words.size} palabras"
        holder.card.setOnClickListener { onClick(category) }
    }

    override fun getItemCount() = categories.size
}