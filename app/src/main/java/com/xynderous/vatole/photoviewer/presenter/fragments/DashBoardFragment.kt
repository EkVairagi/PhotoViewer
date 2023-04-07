package com.xynderous.vatole.photoviewer.presenter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xynderous.vatole.photoviewer.R
import com.xynderous.vatole.photoviewer.adapters.PhotosAdapter
import com.xynderous.vatole.photoviewer.base.BaseFragment
import com.xynderous.vatole.photoviewer.databinding.DashboardFragmentBinding
import com.xynderous.vatole.photoviewer.presenter.viewmodels.DashBoardViewModel
import com.xynderous.vatole.photoviewer.utils.dismissKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class DashBoardFragment : BaseFragment<DashboardFragmentBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> DashboardFragmentBinding
        get() = DashboardFragmentBinding::inflate

    private val viewModel: DashBoardViewModel by viewModels()
    private lateinit var photosAdapter: PhotosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.fetchPhotosAPI()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserver()
    }

    private fun initObserver() {
        lifecycleScope.launch {
            launch {
                viewModel.photosLiveData.collect { data ->
                    photosAdapter.differ.submitList(data)
                }
            }
        }
    }


    private fun initViews() {
        val gridLayoutManager = GridLayoutManager(context, 2)
        binding?.recyclerPopularPhotos?.layoutManager = gridLayoutManager
        photosAdapter = PhotosAdapter { photo, _ ->
            val bundle = bundleOf("photo" to photo)
            findNavController().navigate(
                R.id.action_dashboardFragment_to_photosDetailsFragment,
                bundle
            )
        }

        photosAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding?.recyclerPopularPhotos?.adapter = photosAdapter

        binding?.nestedScrollView?.setOnScrollChangeListener { v: NestedScrollView, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                viewModel.loadMorePhotos()
            }
        }

        binding?.inputSearchPhotos?.setEndIconOnClickListener {
            binding?.txtSearchPhotos?.setText("")
            binding?.tvPopular?.text = getString(R.string.popular_text)
            viewModel.fetchPhotos(1)
        }

        binding?.txtSearchPhotos?.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                binding?.txtSearchPhotos?.dismissKeyboard()
                performSearch(binding?.txtSearchPhotos?.text.toString())
            }
            false
        }
    }

    private fun performSearch(query: String) {
        binding?.txtSearchPhotos?.setText(query)
        binding?.tvPopular?.text = getString(R.string.search_results_for_str, query)
        viewModel.searchPhotos(query)
    }

}