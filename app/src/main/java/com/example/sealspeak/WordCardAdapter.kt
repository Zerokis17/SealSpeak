package com.example.sealspeak

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WordCardAdapter(
    private val words: List<Map<String, String>>
) : RecyclerView.Adapter<WordCardAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEn: TextView = view.findViewById(R.id.tvEn)
        val tvEs: TextView = view.findViewById(R.id.tvEs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_word_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvEn.text = words[position]["en"]
        holder.tvEs.text = words[position]["es"]
    }

    override fun getItemCount() = words.size
}