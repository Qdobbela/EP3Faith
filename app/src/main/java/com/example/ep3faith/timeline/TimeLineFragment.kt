package com.example.ep3faith.timeline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.ep3faith.R
import com.example.ep3faith.databinding.FragmentTimeLineBinding

/**
 * A simple [Fragment] subclass.
 * Use the [TimeLineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimeLineFragment : Fragment() {

    private lateinit var viewModel: TimeLineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentTimeLineBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_time_line, container, false)

        viewModel = ViewModelProvider(this)[TimeLineViewModel::class.java]

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TimeLineFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}