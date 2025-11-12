package org.sportsnow.tv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.sportsnow.tv.data.model.Match
import org.sportsnow.tv.databinding.ItemMatchBinding

/**
 * Simple RecyclerView adapter for Matches.
 */
class MatchAdapter(private val listener: MatchClickListener) :
    ListAdapter<Match, MatchAdapter.VH>(DIFF) {

    interface MatchClickListener {
        fun onMatchClicked(match: Match)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(private val binding: ItemMatchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Match) {
            binding.title.text = "${item.homeTeam} vs ${item.awayTeam}"
            binding.subtitle.text = "Score: ${item.homeScore} - ${item.awayScore}  |  ${item.status}"
            binding.root.isFocusable = true
            binding.root.setOnClickListener { listener.onMatchClicked(item) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Match>() {
            override fun areItemsTheSame(oldItem: Match, newItem: Match): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Match, newItem: Match): Boolean = oldItem == newItem
        }
    }
}
