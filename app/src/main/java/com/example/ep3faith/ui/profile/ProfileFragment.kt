package com.example.ep3faith.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.text.set
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ep3faith.MainActivity
import com.example.ep3faith.R
import com.example.ep3faith.database.FaithDatabase
import com.example.ep3faith.databinding.FragmentProfileBinding
import timber.log.Timber

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false )
        Timber.i("called ViewModelProvider")

        //get viewModel through factory
        val application = requireNotNull(this.activity).application
        val dataSource = FaithDatabase.getInstance(application).faithDatabaseDAO
        val viewModelFactory = ProfileViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        binding.profileViewModel = viewModel
        binding.lifecycleOwner = this


        binding.usernameTextView.text = viewModel.user.value?.username
        viewModel.user.value?.profilePicture?.let {
            binding.profilePictureImageView.setImageURI(Uri.parse(it))
        }

        binding.wijzigButton.setOnClickListener{
            binding.wijzigButton.visibility = View.GONE
            binding.usernameTextView.visibility = View.GONE
            binding.opslaanButton.visibility = View.VISIBLE
            binding.usernameEditPlainText.visibility = View.VISIBLE
            binding.usernameEditPlainText.setText(viewModel.user.value?.username)
            binding.profielfotoButton.visibility = View.VISIBLE
        }

        binding.opslaanButton.setOnClickListener{
            imageUri.let {
                viewModel.updateUser(binding.usernameEditPlainText.text.toString(),imageUri)
            }
            //viewModel.changeUsername(binding.usernameEditPlainText.text.toString())

            binding.wijzigButton.visibility = View.VISIBLE
            binding.usernameTextView.visibility = View.VISIBLE
            binding.opslaanButton.visibility = View.GONE
            binding.usernameEditPlainText.visibility = View.GONE
            binding.profielfotoButton.visibility = View.GONE
        }

        binding.logoutButton.setOnClickListener {
            (activity as MainActivity).logout()
        }

        val getImage =  registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            binding.profilePictureImageView.setImageURI(uri)
            if (uri != null) {
                imageUri = uri
            }
        }

        binding.profielfotoButton.setOnClickListener {
            getImage.launch(arrayOf("image/*"))
        }

        viewModel.user.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.profilePictureImageView.setImageURI(Uri.parse(it.profilePicture))
            }
        })

        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}