package com.example.ep3faith.timeline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.ep3faith.R
import com.example.ep3faith.database.FaithDatabase
import com.example.ep3faith.databinding.FragmentTimeLineBinding
import timber.log.Timber

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

        //get viewModel through factory
        val application = requireNotNull(this.activity).application
        val dataSource = FaithDatabase.getInstance(application).faithDatabaseDAO
        val viewModelFactory = TimeLineViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[TimeLineViewModel::class.java]

        binding.timelineViewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = PostAdapter(PostAdapter.PostFavoriteListener { postId ->
            viewModel.addFavorite(postId)
        })

        binding.postList.adapter = adapter

        viewModel.posts.observe(viewLifecycleOwner, Observer{
            it?.let{
                adapter.submitList(it)
            }
        })

        binding.nieuwePostButton.setOnClickListener (
            Navigation.createNavigateOnClickListener(R.id.action_timeLineFragment_to_postToevoegenFragment)
            )



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