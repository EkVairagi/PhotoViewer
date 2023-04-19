package com.xynderous.vatole.photoviewer.presenter.photo_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.xynderous.vatole.photoviewer.presenter.base.BaseFragment
import com.xynderous.vatole.photoviewer.databinding.FragmentPhotoDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotosDetailsFragment : BaseFragment<FragmentPhotoDetailsBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPhotoDetailsBinding
        get() = FragmentPhotoDetailsBinding::inflate
    private val viewModel: PhotoDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val photo = arguments?.getString("photo")
        if (photo == null) {
            findNavController().popBackStack()
            return
        }

        savedInstanceState?.let { state ->
            viewModel.restoreState(state)
        } ?: viewModel.loadPhotosById(photo)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    private fun initObserver() {

        lifecycle.coroutineScope.launchWhenCreated {
            viewModel.photoDetails.collect {
                if (it.isLoading) {
                }
                if (it.error.isNotBlank()) {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                }
                it.data?.apply {
                    binding?.tvUserName?.text = user?.name
                    binding?.tvLocation?.text = user?.location
                    binding?.tvDesc?.text = alt_description
                    binding?.photoView?.load(urls?.full)
                }
            }
        }

    }
}