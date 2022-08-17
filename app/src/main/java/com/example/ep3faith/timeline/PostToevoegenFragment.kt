package com.example.ep3faith.timeline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ep3faith.R

class PostToevoegenFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_post_toevoegen, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PostToevoegenFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}