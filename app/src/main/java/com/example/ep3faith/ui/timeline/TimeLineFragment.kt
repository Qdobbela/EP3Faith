package com.example.ep3faith.ui.timeline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.ep3faith.R
import com.example.ep3faith.database.FaithDatabase
import com.example.ep3faith.databinding.FragmentTimeLineBinding
import kotlinx.coroutines.delay
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

        viewModel.user.observe(viewLifecycleOwner, Observer {
            viewModel.user.value?.let { it1 -> Timber.i("Got the user: %s", it1.email) }
            initAdapter(binding)
        })

        binding.nieuwePostButton.setOnClickListener (
            Navigation.createNavigateOnClickListener(R.id.action_timeLineFragment_to_postToevoegenFragment)
            )

        return binding.root
    }

    private fun initAdapter(binding: FragmentTimeLineBinding) {
        val adapter =
            viewModel.user.value?.let {
                PostAdapter(
                    PostAdapter.PostFavoriteListener { postId ->
                        viewModel.addFavorite(postId)
                    },
                    PostAdapter.AddReactionListener { postId, pos ->
                        Timber.i("saving Reaction, recyclerpos: %s", pos)
                        val view = binding.postList.getChildAt(pos)
                        Timber.i("view: %s", view.toString())
                        val editText: EditText = view.findViewById(R.id.addReactionEditView)
                        viewModel.addReaction(editText.text.toString(), postId)
                    },
                    PostAdapter.DeletePostClickListener{ postId ->
                        viewModel.deletePost(postId)
                    }
                    ,it
                )
            }

        binding.postList.adapter = adapter

        viewModel.posts.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (adapter != null) {
                    adapter.submitList(it)
                }
            }
        })
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