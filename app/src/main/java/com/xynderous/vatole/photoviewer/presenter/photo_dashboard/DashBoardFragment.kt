package com.xynderous.vatole.photoviewer.presenter.photo_dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xynderous.vatole.photoviewer.R
import com.xynderous.vatole.photoviewer.base.BaseFragment
import com.xynderous.vatole.photoviewer.data.model.PhotoModel
import com.xynderous.vatole.photoviewer.databinding.DashboardFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardFragment : BaseFragment<DashboardFragmentBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> DashboardFragmentBinding
        get() = DashboardFragmentBinding::inflate
    private val viewModel: DashBoardViewModel by viewModels()
    private lateinit var photosAdapter: PhotosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { state ->
            viewModel.restoreState(state)
        } ?: viewModel.fetchPhotosAPI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserver()
    }

    private fun initObserver() {
        lifecycle.coroutineScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.photoDetails.collect {
                    if (it.isLoading) {
                    }
                    if (it.error.isNotBlank()) {
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    }
                    it.data?.let { data ->
                        photosAdapter.differ.submitList(data)
                    }
                }
            }


        }
    }

    private fun initViews() {
        val gridLayoutManager = GridLayoutManager(context, 2)
        binding?.recyclerPopularPhotos?.layoutManager = gridLayoutManager
        photosAdapter = PhotosAdapter { photo, _ ->
            val bundle = bundleOf("photo" to photo.id)
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