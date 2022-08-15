package com.example.ep3faith.timeline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ep3faith.R
import com.example.ep3faith.database.Post
import timber.log.Timber

class PostAdapter: RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    var data = listOf<Post>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = data[position]

        holder.username.text = item.username
        holder.caption.text = item.caption
        holder.link.text = item.link
        holder.postImage.setImageResource(R.drawable.grandma_tree)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.post_view, parent, false)

        Timber.i("onCreate is called")
        return PostViewHolder(view)
    }

    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val username: TextView = itemView.findViewById(R.id.usernameTextView)
        val postImage: ImageView = itemView.findViewById(R.id.postImageView)
        val caption: TextView = itemView.findViewById(R.id.captionTextView)
        val link: TextView = itemView.findViewById(R.id.linkTextView)
    }
}