package com.example.ep3faith.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.ep3faith.R
import com.example.ep3faith.database.FaithDatabase
import com.example.ep3faith.databinding.FragmentFavoritesBinding
import com.example.ep3faith.ui.timeline.PostAdapter
import com.example.ep3faith.ui.timeline.ReactionAdapter
import com.example.ep3faith.ui.timeline.TimeLineFragmentDirections

/**
 * A simple [Fragment] subclass.
 * Use the [FavoritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritesFragment : Fragment() {

    private lateinit var viewModel: FavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentFavoritesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)

        //get viewModel through factory
        val application = requireNotNull(this.activity).application
        val dataSource = FaithDatabase.getInstance(application).faithDatabaseDAO
        val viewModelFactory = FavoritViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[FavoritesViewModel::class.java]

        binding.favoriteViewModel = viewModel
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
                    viewModel.removeFavorite(postId)
                },
                PostAdapter.AddReactionListener { postId, pos ->

                },
                PostAdapter.DeletePostClickListener{ postId ->

                },
                PostAdapter.EditPostClickListener{ postId ->

                }, 
                ReactionAdapter.DeleteReactionClickListener{ reactionId ->  

                },
                it)
            }

        binding.favoriteList.adapter = adapter

        viewModel.favorites.observe(viewLifecycleOwner, Observer {
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
            FavoritesFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}