package com.example.ep3faith.ui.addPost

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ep3faith.R
import com.example.ep3faith.database.FaithDatabase
import com.example.ep3faith.database.post.DatabasePost
import com.example.ep3faith.databinding.FragmentPostToevoegenBinding

class EditPostFragment : Fragment() {

    private lateinit var viewModel: EditPostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentPostToevoegenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_toevoegen, container, false)

        // get viewModel through factory
        val application = requireNotNull(this.activity).application
        val dataSource = FaithDatabase.getInstance(application).faithDatabaseDAO
        val viewModelFactory = EditPostViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[EditPostViewModel::class.java]
        var imageUri: Uri = Uri.EMPTY

        binding.lifecycleOwner = this

        // ARGS

        val args = EditPostFragmentArgs.fromBundle(requireArguments())
        viewModel.getPost(args.postId)

        viewModel.post.observe(
            viewLifecycleOwner,
            Observer {
                initEdit(binding, viewModel.post)
            }
        )

        // post updaten
        binding.postOpslaanButton.setOnClickListener() {
            viewModel.editPost(args.postId, binding.editCaptionEditView.text.toString(), binding.linkEditView.text.toString(), imageUri)
        }

        viewModel.saved.observe(
            viewLifecycleOwner,
            Observer {
                if (viewModel.saved.value == true) {
                    this.findNavController().navigate(R.id.action_editPostFragment_to_timeLineFragment)
                }
            }
        )

        val getImage = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            binding.imageView2.setImageURI(uri)
            if (uri != null) {
                imageUri = uri
            }
        }

        binding.choosePictureButton.setOnClickListener {
            getImage.launch(arrayOf("image/*"))
        }
        return binding.root
    }

    private fun initEdit(binding: FragmentPostToevoegenBinding, post: LiveData<DatabasePost>) {
        binding.linkEditView.setText(post.value?.link)
        binding.imageView2.setImageURI(Uri.parse(post.value?.picture))
        binding.editCaptionEditView.setText(post.value?.caption)
        binding.postOpslaanButton.setText("Update")
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            EditPostFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
