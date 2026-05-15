package com.example.sealspeak

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class VideoLessonAdapter(
    private val lessons: List<VideoLesson>,
    private val onClick: (VideoLesson) -> Unit
) : RecyclerView.Adapter<VideoLessonAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvLevel: TextView = view.findViewById(R.id.tvLevel)
        val card: CardView = view.findViewById(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video_lesson, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = lessons[position]
        holder.tvTitle.text = "🎬 ${lesson.title}"
        holder.tvLevel.text = lesson.level
        holder.card.setOnClickListener { onClick(lesson) }
    }

    override fun getItemCount() = lessons.size
}