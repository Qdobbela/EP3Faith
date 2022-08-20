package com.example.ep3faith.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ep3faith.R
import com.example.ep3faith.database.FaithDatabase
import com.example.ep3faith.databinding.FragmentFavoritesBinding
import com.example.ep3faith.profile.ProfileViewModel
import com.example.ep3faith.timeline.PostAdapter
import com.example.ep3faith.timeline.TimeLineViewModel
import com.example.ep3faith.timeline.TimeLineViewModelFactory
import timber.log.Timber

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

        val adapter = PostAdapter(PostAdapter.PostFavoriteListener { postId ->
            viewModel.removeFavorite(postId)
        }, PostAdapter.AddReactionListener { postId, pos ->

        })

        binding.favoriteList.adapter = adapter

        viewModel.favorites.observe(viewLifecycleOwner, Observer{
            it?.let{
                adapter.submitList(it)
            }
        })

        return binding.root

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