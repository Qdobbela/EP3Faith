package com.example.ep3faith.timeline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ep3faith.database.Post
import com.example.ep3faith.databinding.PostViewBinding

class PostAdapter(val clickListener: PostFavoriteListener): ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback()) {


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        holder.bind(clickListener, getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder.from(parent)
    }

    class PostViewHolder private constructor(val binding: PostViewBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: PostFavoriteListener, item: Post) {
            binding.post = item
            binding.clickListener = clickListener
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

    class PostDiffCallback :DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

    }

    class PostFavoriteListener(val clickListener: (postId: Int) -> Unit) {
        fun onClick(post: Post) = clickListener(post.postId)
    }


}