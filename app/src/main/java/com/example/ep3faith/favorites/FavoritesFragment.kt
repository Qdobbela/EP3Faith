package com.example.ep3faith.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.ep3faith.R
import com.example.ep3faith.databinding.FragmentFavoritesBinding
import com.example.ep3faith.profile.ProfileViewModel
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

        Timber.i("called ViewModelProvider")
        viewModel = ViewModelProvider(this)[FavoritesViewModel::class.java]

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