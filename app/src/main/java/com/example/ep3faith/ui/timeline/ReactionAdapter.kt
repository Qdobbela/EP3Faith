package com.example.ep3faith.ui.timeline

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ep3faith.database.PostWithReactions
import com.example.ep3faith.database.Reaction
import com.example.ep3faith.database.User
import com.example.ep3faith.databinding.ReactionViewBinding

class ReactionAdapter(reactionsPassed: List<Reaction>, val deleteReactionClickListener: DeleteReactionClickListener, val user: User): ListAdapter<Reaction, ReactionAdapter.ReactionViewHolder>(ReactionDiffCallback()) {

    private var reactions: MutableList<Reaction> = reactionsPassed.toMutableList()

    override fun onBindViewHolder(holder: ReactionViewHolder,position: Int) {
        holder.bind(reactions[position], deleteReactionClickListener, user)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReactionViewHolder {
        return ReactionViewHolder.from(parent)
    }

    class ReactionViewHolder private constructor(val binding: ReactionViewBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: Reaction, deleteReactionClickListener: DeleteReactionClickListener, user: User) {
            binding.reaction = item
            binding.executePendingBindings()
            binding.deleteReactionClickListener = deleteReactionClickListener

            if(user.email == item.reactionUserEmail){
                binding.deleteReactionButton.visibility = View.VISIBLE
            } else{
                binding.deleteReactionButton.visibility = View.GONE
            }
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

    class DeleteReactionClickListener(val clickListener: (reactionId: Int) -> Unit) {
        fun onClick(reaction: Reaction) = clickListener(reaction.reactionId)
    }


}