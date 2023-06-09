package com.xynderous.vatole.photoviewer.ui.photo_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.xynderous.vatole.photoviewer.data.model.DomainPhotoModel
import com.xynderous.vatole.photoviewer.ui.base.BaseFragment
import com.xynderous.vatole.photoviewer.databinding.FragmentPhotoDetailsBinding
import com.xynderous.vatole.photoviewer.ui.base.BaseState
import com.xynderous.vatole.photoviewer.utils.makeVisibleIf
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
            viewModel.photoDetails.collect { state ->
                when (state) {
                    is BaseState.Loading -> {
                        binding?.pbLoading?.makeVisibleIf(true)
                    }
                    is BaseState.Data<*> -> {
                        binding?.pbLoading?.makeVisibleIf(false)
                        val photos = state.photos as DomainPhotoModel
                        binding?.tvUserName?.text = photos.user?.name
                        binding?.tvLocation?.text = photos.user?.location
                        binding?.tvDesc?.text = photos.alt_description
                        binding?.photoView?.load(photos.urls?.full)
                    }
                    is BaseState.Error -> {
                        binding?.pbLoading?.makeVisibleIf(false)
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}