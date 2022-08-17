package com.example.ep3faith.timeline

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.ep3faith.database.Post

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
        text = item.link
    }
}