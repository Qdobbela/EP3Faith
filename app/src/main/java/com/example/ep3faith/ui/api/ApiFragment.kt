package com.example.ep3faith.ui.api

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.ep3faith.R
import com.example.ep3faith.databinding.FragmentApiBinding

class ApiFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentApiBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_api, container, false)

        val viewModel = ApiViewModel(application = Application())
        viewModel.getJoke()

        binding.apiViewModel = viewModel

        binding.newJokeButton.setOnClickListener {
            viewModel.getJoke()
            binding.dadJokeTextView.text = viewModel.response.value
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ApiFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
