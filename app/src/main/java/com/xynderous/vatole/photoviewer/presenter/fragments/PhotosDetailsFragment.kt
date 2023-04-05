package com.xynderous.vatole.photoviewer.presenter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.xynderous.vatole.photoviewer.base.BaseFragment
import com.xynderous.vatole.photoviewer.databinding.FragmentPhotoDetailsBinding
import com.xynderous.vatole.photoviewer.domain.model.PhotoModel
import com.xynderous.vatole.photoviewer.presenter.viewmodels.PhotoDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotosDetailsFragment : BaseFragment<FragmentPhotoDetailsBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPhotoDetailsBinding
        get() = FragmentPhotoDetailsBinding::inflate
    private val viewModel: PhotoDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val photo = arguments?.getParcelable<PhotoModel>("photo")

        if (photo == null) {
            findNavController().popBackStack()
            return
        }
        initObserver()
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.loadPhotosById(photo.id ?: "")
        }
    }

    private fun initObserver() {

        lifecycleScope.launch {
            launch {
                viewModel.photoModelLiveDataByAPI.collect { photos ->
                    binding?.tvUserName?.text = photos?.user?.name
                    binding?.tvLocation?.text = photos?.user?.location
                    binding?.tvDesc?.text = photos?.alt_description
                    binding?.photoView?.load(photos?.urls?.full)
                }
            }
        }
    }
}