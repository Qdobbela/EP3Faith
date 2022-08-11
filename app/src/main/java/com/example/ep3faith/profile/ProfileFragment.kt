package com.example.ep3faith.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.ep3faith.R
import com.example.ep3faith.database.FaithDatabase
import com.example.ep3faith.databinding.FragmentProfileBinding
import timber.log.Timber

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    private lateinit var wijzigButton: Button
    private lateinit var opslaanButton: Button
    private lateinit var editUsername: EditText
    private lateinit var usernameText: TextView

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

        wijzigButton = binding.wijzigButton
        opslaanButton = binding.opslaanButton
        usernameText = binding.usernameTextView
        editUsername = binding.usernameEditPlainText

        usernameText.text = viewModel.username.value
        editUsername.setText(viewModel.username.value)

        wijzigButton.setOnClickListener{
            wijzigButton.visibility = View.GONE
            usernameText.visibility = View.GONE
            opslaanButton.visibility = View.VISIBLE
            editUsername.visibility = View.VISIBLE
        }

        opslaanButton.setOnClickListener{
            viewModel.changeUsername(editUsername.text.toString())

            wijzigButton.visibility = View.VISIBLE
            usernameText.visibility = View.VISIBLE
            opslaanButton.visibility = View.GONE
            editUsername.visibility = View.GONE
        }

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