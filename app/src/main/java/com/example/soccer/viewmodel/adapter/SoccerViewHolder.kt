package com.example.soccer.viewmodel.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.size.ViewSizeResolver
import com.example.soccer.databinding.ItemSoccerBinding
import com.example.soccer.model.data.api.Player

class SoccerViewHolder(
    private val binding: ItemSoccerBinding,
    private val onPlayerClicked: (Player) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(player: Player) {
        with(binding) {
            image.load(player.playerImage) {
                scale(Scale.FIT)
                size(ViewSizeResolver(root))
            }
            textName.text = player.playerName

            root.setOnClickListener {
                onPlayerClicked(player)
            }
        }
    }
}