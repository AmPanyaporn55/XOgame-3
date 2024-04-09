package com.example.xogame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GameHistoryAdapter : RecyclerView.Adapter<GameHistoryAdapter.GameHistoryViewHolder>() {

    private var gameHistories: List<GameHistory> = emptyList() // รายการประวัติการเล่น

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_history, parent, false)
        return GameHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameHistoryViewHolder, position: Int) {
        val gameHistory = gameHistories[position]
        holder.bind(gameHistory)
    }

    override fun getItemCount(): Int {
        return gameHistories.size
    }

    fun setGameHistories(gameHistories: List<GameHistory>) {
        this.gameHistories = gameHistories
        notifyDataSetChanged()
    }

    inner class GameHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        private val winnerTextView: TextView = itemView.findViewById(R.id.winnerTextView)
        private val scoreTextView: TextView = itemView.findViewById(R.id.scoreTextView)

        fun bind(gameHistory: GameHistory) {
            dateTextView.text = gameHistory.date
            timeTextView.text = gameHistory.time
            winnerTextView.text = gameHistory.winner
            scoreTextView.text = gameHistory.scoreX.toString()
            scoreTextView.text = gameHistory.scoreO.toString()

        }
    }

}
