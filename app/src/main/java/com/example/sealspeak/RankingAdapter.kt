package com.example.sealspeak

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class RankingAdapter(
    private val users: List<RankingUser>
) : RecyclerView.Adapter<RankingAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPosition: TextView = view.findViewById(R.id.tvPosition)
        val tvNickname: TextView = view.findViewById(R.id.tvNickname)
        val tvPoints: TextView = view.findViewById(R.id.tvPoints)
        val card: CardView = view.findViewById(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ranking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.tvNickname.text = user.nickname
        holder.tvPoints.text = "${user.points} pts"

        when (user.position) {
            1 -> {
                holder.tvPosition.text = "🥇"
                holder.card.setCardBackgroundColor(0xFFFFF9C4.toInt())
            }
            2 -> {
                holder.tvPosition.text = "🥈"
                holder.card.setCardBackgroundColor(0xFFF5F5F5.toInt())
            }
            3 -> {
                holder.tvPosition.text = "🥉"
                holder.card.setCardBackgroundColor(0xFFFFE0B2.toInt())
            }
            else -> {
                holder.tvPosition.text = "${user.position}"
                holder.card.setCardBackgroundColor(0xFFFFFFFF.toInt())
            }
        }
    }

    override fun getItemCount() = users.size
}