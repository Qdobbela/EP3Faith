package com.example.ep3faith.ui.timeline

import android.net.Uri
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.ep3faith.database.post.PostWithReactions
import com.example.ep3faith.database.reaction.DatabaseReaction
import com.example.ep3faith.domain.Reaction

@BindingAdapter("usernameString")
fun TextView.setUsernameString(item: PostWithReactions?){
    item?.let {
        text = item.post.username
    }
}

@BindingAdapter("captionString")
fun TextView.setCaptionString(item: PostWithReactions?){
    item?.let {
        text = item.post.caption
    }
}

@BindingAdapter("linkString")
fun TextView.setLinkString(item: PostWithReactions?){
    item?.let {
        val link = URLSpan(item.post.link)
        this.movementMethod = LinkMovementMethod.getInstance()
        text = link.url
    }
}

@BindingAdapter("imageUri")
fun ImageView.setImageUri(item: PostWithReactions?){
    item?.let {
        setImageURI(Uri.parse(item.post.picture))
    }
}

@BindingAdapter("reactionUser")
fun TextView.setReactionUsernameString(item: DatabaseReaction?){
    item?.let {
        text = item.reactionUser
    }
}
@BindingAdapter("reaction")
fun TextView.setReactionString(item: DatabaseReaction?){
    item?.let {
        text = item.reactionText
    }
}
