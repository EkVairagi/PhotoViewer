package com.xynderous.vatole.photoviewer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.xynderous.vatole.photoviewer.base.BaseFragment
import com.xynderous.vatole.photoviewer.databinding.FragmentPhotoDetailsBinding
import com.xynderous.vatole.photoviewer.model.PhotoModel
import com.xynderous.vatole.photoviewer.ui.viewmodels.PhotoDetailsViewModel

class PhotosDetailsFragment : BaseFragment<FragmentPhotoDetailsBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPhotoDetailsBinding
        get() = FragmentPhotoDetailsBinding::inflate
    private val viewModel: PhotoDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var photo = arguments?.getParcelable<PhotoModel>("photo")
        if (photo == null) {
            findNavController().popBackStack()
            return
        }
        initObserver()
        viewModel.initPhotoModel(photo)
    }

    private fun initObserver() {
        viewModel.photoModelLiveData.observe(viewLifecycleOwner) { photo ->
            binding?.photoView?.load(photo.urls?.full)
        }
    }
}