package com.example.ep3faith.ui.inbox

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ep3faith.R
import com.example.ep3faith.database.FaithDatabase
import com.example.ep3faith.databinding.FragmentFavoritesBinding
import com.example.ep3faith.ui.timeline.PostAdapter
import com.example.ep3faith.ui.timeline.ReactionAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [InboxFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InboxFragment : Fragment() {

    private lateinit var viewModel: InboxViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentFavoritesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)

        //get viewModel through factory
        val application = requireNotNull(this.activity).application
        val dataSource = FaithDatabase.getInstance(application).faithDatabaseDAO
        val viewModelFactory = InboxViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[InboxViewModel::class.java]

        viewModel.user.observe(viewLifecycleOwner, Observer {
            if(viewModel.user.value?.counselor == false){
                binding.favoriteList.visibility = View.GONE
                binding.enkelVoorBegeleidersTextView.visibility = View.VISIBLE
            } else{
                viewModel.gatherInbox()
            }
        })

        binding.inboxViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.user.observe(viewLifecycleOwner, Observer {
            initAdapter(binding)
        })

        return binding.root

    }

    private fun initAdapter(binding: FragmentFavoritesBinding) {
        val adapter =
            viewModel.user.value?.let {
                PostAdapter(PostAdapter.PostFavoriteListener { postId ->
                    //
                },
                    PostAdapter.AddReactionListener { postId, pos ->
                        val view = binding.favoriteList.getChildAt(pos)
                        val editText: EditText = view.findViewById(R.id.addReactionEditView)
                        viewModel.addReaction(editText.text.toString(), postId)
                    },
                    PostAdapter.DeletePostClickListener{ postId ->
                        //
                    },
                    PostAdapter.EditPostClickListener{ postId ->
                        //
                    },
                    ReactionAdapter.DeleteReactionClickListener{ reactionId ->
                        //
                    },
                    it)
            }

        binding.favoriteList.adapter = adapter

        viewModel.Inbox.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (adapter != null) {
                    adapter.submitList(it)
                }
            }
        })
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            InboxFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}