package com.example.ep3faith.ui.timeline

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ep3faith.database.reaction.DatabaseReaction
import com.example.ep3faith.database.user.DatabaseUser
import com.example.ep3faith.databinding.ReactionViewBinding

class ReactionAdapter(reactionsPassed: List<DatabaseReaction>, val deleteReactionClickListener: DeleteReactionClickListener, val user: DatabaseUser) : ListAdapter<DatabaseReaction, ReactionAdapter.ReactionViewHolder>(ReactionDiffCallback()) {

    private var reactions: MutableList<DatabaseReaction> = reactionsPassed.toMutableList()

    override fun onBindViewHolder(holder: ReactionViewHolder, position: Int) {
        holder.bind(reactions[position], deleteReactionClickListener, user)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReactionViewHolder {
        return ReactionViewHolder.from(parent)
    }

    class ReactionViewHolder private constructor(val binding: ReactionViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DatabaseReaction, deleteReactionClickListener: DeleteReactionClickListener, user: DatabaseUser) {
            binding.reaction = item
            binding.executePendingBindings()
            binding.deleteReactionClickListener = deleteReactionClickListener

            if (user.email == item.reactionUserEmail) {
                binding.deleteReactionButton.visibility = View.VISIBLE
            } else {
                binding.deleteReactionButton.visibility = View.GONE
            }
        }

        companion object {
            fun from(parent: ViewGroup): ReactionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ReactionViewBinding.inflate(layoutInflater, parent, false)
                return ReactionViewHolder(binding)
            }
        }
    }

    class ReactionDiffCallback : DiffUtil.ItemCallback<DatabaseReaction>() {
        override fun areItemsTheSame(oldItem: DatabaseReaction, newItem: DatabaseReaction): Boolean {
            return oldItem.reactionId == newItem.reactionId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: DatabaseReaction, newItem: DatabaseReaction): Boolean {
            return oldItem == newItem
        }
    }

    class DeleteReactionClickListener(val clickListener: (reactionId: Int) -> Unit) {
        fun onClick(reaction: DatabaseReaction) = clickListener(reaction.reactionId)
    }
}
