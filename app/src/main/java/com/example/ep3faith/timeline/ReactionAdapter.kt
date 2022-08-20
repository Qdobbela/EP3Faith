package com.example.ep3faith.timeline

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ep3faith.database.Reaction
import com.example.ep3faith.databinding.ReactionViewBinding
import timber.log.Timber

class ReactionAdapter(reactionsPassed: List<Reaction>): ListAdapter<Reaction, ReactionAdapter.ReactionViewHolder>(ReactionDiffCallback()) {

    private var reactions: MutableList<Reaction> = reactionsPassed.toMutableList()

    override fun onBindViewHolder(holder: ReactionViewHolder,position: Int) {
        holder.bind(reactions[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReactionViewHolder {
        return ReactionViewHolder.from(parent)
    }

    class ReactionViewHolder private constructor(val binding: ReactionViewBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: Reaction) {
            binding.reaction = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ReactionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ReactionViewBinding.inflate(layoutInflater,parent,false)
                return ReactionViewHolder(binding)
            }
        }
    }

    class ReactionDiffCallback :DiffUtil.ItemCallback<Reaction>() {
        override fun areItemsTheSame(oldItem: Reaction, newItem: Reaction): Boolean {
            return oldItem.reactionId == newItem.reactionId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Reaction, newItem: Reaction): Boolean {
            return oldItem == newItem
        }

    }


}