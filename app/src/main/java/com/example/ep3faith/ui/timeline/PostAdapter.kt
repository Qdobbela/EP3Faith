package com.example.ep3faith.ui.timeline

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ep3faith.database.PostWithReactions
import com.example.ep3faith.databinding.PostViewBinding

class PostAdapter(val clickListener: PostFavoriteListener, val reactionListener: AddReactionListener): ListAdapter<PostWithReactions, PostAdapter.PostViewHolder>(PostDiffCallback()) {

    private lateinit var reactionText: String

    override fun onBindViewHolder(holder: PostViewHolder,position: Int) {
        holder.bind(clickListener, reactionListener, getItem(position)!!, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder.from(parent)
    }

    class PostViewHolder private constructor(val binding: PostViewBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: PostFavoriteListener, reactionListener: AddReactionListener, item: PostWithReactions, position: Int) {
            binding.position = position
            binding.postAndReactions = item
            binding.clickListener = clickListener
            binding.reactionClickListener = reactionListener
            val reactionAdapter = ReactionAdapter(item.reactions)
            binding.reactionList.adapter = reactionAdapter
            reactionAdapter.submitList(item.reactions)
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): PostViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PostViewBinding.inflate(layoutInflater,parent,false)
                return PostViewHolder(binding)
            }
        }
    }

    class PostDiffCallback :DiffUtil.ItemCallback<PostWithReactions>() {
        override fun areItemsTheSame(oldItem: PostWithReactions, newItem: PostWithReactions): Boolean {
            return oldItem.post.postId == newItem.post.postId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: PostWithReactions, newItem: PostWithReactions): Boolean {
            return oldItem == newItem
        }

    }

    class PostFavoriteListener(val clickListener: (postId: Int) -> Unit) {
        fun onClick(postWithReactions: PostWithReactions) = clickListener(postWithReactions.post.postId)
    }

    class AddReactionListener(val clickListener: (postId: Int, position: Int) -> Unit) {
        fun onClick(postWithReactions: PostWithReactions, position: Int) = clickListener(postWithReactions.post.postId, position)
    }


}