package com.xynderous.vatole.photoviewer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xynderous.vatole.photoviewer.R
import com.xynderous.vatole.photoviewer.adapters.PhotosAdapter
import com.xynderous.vatole.photoviewer.base.BaseFragment
import com.xynderous.vatole.photoviewer.databinding.DashboardFragmentBinding
import com.xynderous.vatole.photoviewer.ui.viewmodels.DashBoardViewModel
import com.xynderous.vatole.photoviewer.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardFragment : BaseFragment<DashboardFragmentBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> DashboardFragmentBinding
        get() = DashboardFragmentBinding::inflate


    private val viewModel: DashBoardViewModel by viewModels()

    private lateinit var photosAdapter: PhotosAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObserver()
    }

    private fun initObserver() {

        viewModel.uiStateLiveData.observe(viewLifecycleOwner) { state ->

            when (state) {
                is LoadingState -> {
                    binding?.recyclerPopularPhotos.makeVisibleIf(false)
                    binding?.progressPhotos.makeVisibleIf(true)
                }

                is LoadingNextPageState -> {
                    binding?.progressPhotos.makeVisibleIf(false)
                }

                is ContentState -> {
                    binding?.recyclerPopularPhotos.makeVisibleIf(true)
                    binding?.progressPhotos.makeVisibleIf(false)
                }

                is ErrorState -> {
                    binding?.progressPhotos.makeVisibleIf(false)
                    binding?.nestedScrollView?.showSnack(state.message, getString(R.string.retry)) {
                        viewModel.retry()
                    }
                }

                is ErrorNextPageState -> {
                    binding?.nestedScrollView?.showSnack(state.message, getString(R.string.retry)) {
                        viewModel.retry()
                    }
                }
                else -> {}
            }

        }

        viewModel.photosLiveData.observe(viewLifecycleOwner) { photos ->
            photosAdapter.differ.submitList(photos)
        }

    }

    private fun initViews() {
        val gridLayoutManager = GridLayoutManager(context, 2)
        binding?.recyclerPopularPhotos?.layoutManager = gridLayoutManager

        photosAdapter = PhotosAdapter { photo, _ ->
            var bundle = bundleOf("photo" to photo)
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

        binding?.txtSearchPhotos?.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                binding?.txtSearchPhotos?.dismissKeyboard()
                performSearch(binding?.txtSearchPhotos?.text.toString())
                true
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