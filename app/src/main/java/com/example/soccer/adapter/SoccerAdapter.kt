package com.example.soccer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.soccer.databinding.ItemSoccerBinding
import com.example.soccer.model.Player

class SoccerAdapter(
    private val onSoccerClicked: (Player) -> Unit
) : ListAdapter<Paging<Player>, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Paging.Content -> TYPE_SOCCER
            Paging.Loading -> TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_SOCCER -> {
                SoccerViewHolder(
                    binding = ItemSoccerBinding.inflate(layoutInflater, parent, false),
                    onPlayerClicked = onSoccerClicked
                )
            }
            TYPE_LOADING -> {
                LoadingViewHolder(
                    binding = ItemSoccerBinding.inflate(layoutInflater, parent, false)
                )
            }
            else -> error("Unsupported view Type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val player = (getItem(position) as? Paging.Content)?.data ?: return
        (holder as? SoccerViewHolder)?.bind(player)
    }

    companion object {

        private const val TYPE_SOCCER = 1
        private const val TYPE_LOADING = 1

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Paging<Player>>() {
            override fun areItemsTheSame(
                oldItem: Paging<Player>,
                newItem: Paging<Player>
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Paging<Player>,
                newItem: Paging<Player>
            ): Boolean {
                val oldPlayer = oldItem as? Paging.Content
                val newPlayer = newItem as? Paging.Content
                return oldPlayer?.data == newPlayer?.data
            }
        }
    }
}