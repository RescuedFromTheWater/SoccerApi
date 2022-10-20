package com.example.soccer.viewmodel.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.soccer.databinding.ItemSoccerBinding
import com.example.soccer.model.Lce
import com.example.soccer.model.data.api.Player

class SoccerAdapter(
    context: Context,
    private val onSoccerClicked: (Player) -> Unit
) : ListAdapter<Lce<Player>, SoccerViewHolder>(DIFF_CALLBACK) {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoccerViewHolder {
        return SoccerViewHolder(
            binding = ItemSoccerBinding.inflate(layoutInflater, parent, false),
            onPlayerClicked = onSoccerClicked
        )
    }

    override fun onBindViewHolder(holder: SoccerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Lce<Player>>() {
            override fun areItemsTheSame(
                oldItem: Player,
                newItem: Player
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Player,
                newItem: Player
            ): Boolean {
                return (oldItem.playerName == newItem.playerName &&
                        oldItem.playerImage == newItem.playerImage)

            }
        }
    }
}