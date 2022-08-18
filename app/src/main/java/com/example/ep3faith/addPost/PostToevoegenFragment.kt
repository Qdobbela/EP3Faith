package com.example.ep3faith.addPost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.ep3faith.R
import com.example.ep3faith.database.FaithDatabase
import com.example.ep3faith.databinding.FragmentPostToevoegenBinding
import com.example.ep3faith.timeline.TimeLineViewModel
import com.example.ep3faith.timeline.TimeLineViewModelFactory
import kotlinx.coroutines.delay
import timber.log.Timber

class PostToevoegenFragment : Fragment() {

    private lateinit var viewModel: PostToevoegenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentPostToevoegenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_toevoegen, container, false)

        //get viewModel through factory
        val application = requireNotNull(this.activity).application
        val dataSource = FaithDatabase.getInstance(application).faithDatabaseDAO
        val viewModelFactory = PostToevoegenViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[PostToevoegenViewModel::class.java]

        binding.postToevoegenViewModel = viewModel
        binding.lifecycleOwner = this

        //Nieuwe post opslaan
        binding.postOpslaanButton.setOnClickListener() {
            viewModel.postOpslaan(binding.editCaptionEditView.text.toString(), binding.linkEditView.text.toString())
        }

        viewModel.saved.observe(viewLifecycleOwner, Observer {
            if(viewModel.saved.value == true){
                this.findNavController().navigate(R.id.action_postToevoegenFragment_to_timeLineFragment)
            }
        })

        return binding.root
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