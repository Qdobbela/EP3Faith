package com.example.ep3faith.timeline

import android.net.Uri
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.ep3faith.database.Post
import timber.log.Timber

@BindingAdapter("usernameString")
fun TextView.setUsernameString(item: Post?){
    item?.let {
        text = item.username
    }
}

@BindingAdapter("captionString")
fun TextView.setCaptionString(item: Post?){
    item?.let {
        text = item.caption
    }
}

@BindingAdapter("linkString")
fun TextView.setLinkString(item: Post?){
    item?.let {
        val link = URLSpan(item.link)
        this.movementMethod = LinkMovementMethod.getInstance()
        text = link.url
    }
}

@BindingAdapter("imageUri")
fun ImageView.setImageUri(item: Post?){
    item?.let {
        setImageURI(Uri.parse(item.picture))
    }
}