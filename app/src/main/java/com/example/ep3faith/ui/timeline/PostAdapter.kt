package com.example.ep3faith.ui.timeline

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ep3faith.database.PostWithReactions
import com.example.ep3faith.database.User
import com.example.ep3faith.databinding.PostViewBinding
import timber.log.Timber

class PostAdapter(val clickListener: PostFavoriteListener, val reactionListener: AddReactionListener, val deletePostClickListener: DeletePostClickListener, val editPostClickListener: EditPostClickListener, val deleteReactionClickListener: ReactionAdapter.DeleteReactionClickListener, val user: User): ListAdapter<PostWithReactions, PostAdapter.PostViewHolder>(PostDiffCallback()) {

    override fun onBindViewHolder(holder: PostViewHolder,position: Int) {
        user.let {
            holder.bind(clickListener, reactionListener, deletePostClickListener, editPostClickListener, deleteReactionClickListener, it, getItem(position)!!, position)
            Timber.i("binding with user: %s", user.email)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder.from(parent)
    }

    class PostViewHolder private constructor(val binding: PostViewBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: PostFavoriteListener, reactionListener: AddReactionListener, deletePostClickListener: DeletePostClickListener,editPostClickListener: EditPostClickListener, deleteReactionClickListener: ReactionAdapter.DeleteReactionClickListener, user: User, item: PostWithReactions, position: Int) {
            binding.position = position
            binding.postAndReactions = item

            //clickListeners
            binding.clickListener = clickListener
            binding.reactionClickListener = reactionListener
            binding.deletePostClickListener = deletePostClickListener
            binding.editPostClickListener = editPostClickListener

            val reactionAdapter = ReactionAdapter(item.reactions, deleteReactionClickListener, user)
            binding.reactionList.adapter = reactionAdapter
            reactionAdapter.submitList(item.reactions)

            Timber.i("emails: %s %s", item.post.emailUser, user.email)
            if(item.post.emailUser == user.email){
                binding.deletePostButton.visibility = View.VISIBLE
                binding.postAanpassenButton.visibility = View.VISIBLE
            } else{
                binding.deletePostButton.visibility = View.GONE
                binding.postAanpassenButton.visibility = View.GONE
            }

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

    class DeletePostClickListener(val clickListener: (postId: Int) -> Unit) {
        fun onClick(postWithReactions: PostWithReactions) = clickListener(postWithReactions.post.postId)
    }

    class EditPostClickListener(val clickListener: (postId: Int) -> Unit) {
        fun onClick(postWithReactions: PostWithReactions) = clickListener(postWithReactions.post.postId)
    }


}