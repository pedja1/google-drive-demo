package com.demo.googledrive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.drive.core.model.Result
import com.demo.drive.core.utils.mimetype.FileType
import com.demo.drive.core.utils.mimetype.toFileType
import com.demo.googledrive.R
import com.demo.googledrive.databinding.FragmentFilesBinding
import com.demo.googledrive.ui.adapter.FileListAdapter
import com.demo.googledrive.ui.viewmodel.FilesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FragmentFiles : BaseFragment<FragmentFilesBinding>() {

    private val filesAdapter by lazy { FileListAdapter() }
    private val viewModel by viewModels<FilesViewModel>()

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentFilesBinding {
        return FragmentFilesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.adapter = filesAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(
            requireContext(), resources.getInteger(
                R.integer.files_grid_span_count
            )
        )

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.files.collectLatest { pagingData ->
                        filesAdapter.submitData(pagingData)
                    }
                }
                launch {
                    viewModel.navigationRoute.collectLatest {
                        binding.textRoute.text = it
                    }
                }
                launch {
                    viewModel.downloadStartResult.collectLatest {
                        when (it) {
                            is Result.Error -> showErrorWithFallback(it.exception.message)
                            is Result.Success -> {
                                Snackbar.make(
                                    binding.root,
                                    getString(R.string.downloading_scheduled_toast),
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                }
                launch {
                    viewModel.logoutResult.collectLatest {
                        when (it) {
                            is Result.Error -> {
                                showErrorWithFallback(it.exception.message)
                            }
                            is Result.Success -> {
                                findNavController().navigate(com.demo.googledrive.ui.fragment.FragmentFilesDirections.actionFragmentFilesToFragmentLogin())
                            }
                        }
                    }
                }
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            filesAdapter.refresh()
        }

        filesAdapter.addLoadStateListener {
            updateViewState(it.refresh)
        }

        filesAdapter.onItemClick {
            when (it.mimeType.toFileType()) {
                FileType.FOLDER -> {
                    viewModel.openFolder(it)
                }
                FileType.DOCUMENT,
                FileType.AUDIO,
                FileType.VIDEO,
                FileType.IMAGE,
                FileType.ARCHIVE,
                FileType.FONT,
                FileType.TEXT,
                FileType.OTHER -> {
                    val thumbnailLink = it.thumbnailLink
                    if (!thumbnailLink.isNullOrEmpty()) {
                        findNavController().navigate(
                            com.demo.googledrive.ui.fragment.FragmentFilesDirections.actionFragmentFilesToFragmentPreview(
                                thumbnailLink
                            )
                        )
                    } else {
                        Snackbar.make(
                            binding.root,
                            R.string.preview_not_supported,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        filesAdapter.onMenuItemClick { driveFile, itemId ->
            //TODO implement actions
            when (itemId) {
                R.id.delete -> {
                    Snackbar.make(binding.root, R.string.not_yet_implemented, Snackbar.LENGTH_SHORT)
                        .show()
                }
                R.id.rename -> {
                    Snackbar.make(binding.root, R.string.not_yet_implemented, Snackbar.LENGTH_SHORT)
                        .show()
                }
                R.id.download -> {
                    viewModel.downloadFile(driveFile)
                }
            }
        }

        var initialTextChangeTriggered = false
        binding.editTextSearch.doOnTextChanged { text, _, _, _ ->
            //ignore initial trigger of doOnTextChanged
            //when fragment view is recreated, viewModel.searchFiles
            //is triggered with null value which has the effect of reseting to root folder
            //this is a quick and dirty solution for that
            if(initialTextChangeTriggered) {
                viewModel.searchFiles(text?.toString())
            }
            initialTextChangeTriggered = true
        }

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!viewModel.navigateBack()) {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        })

        binding.imageViewMore.setOnClickListener {
            val popup = PopupMenu(it.context, it)
            popup.menuInflater.inflate(R.menu.file_list_options, popup.menu)
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.logout -> {
                        viewModel.logout()
                    }
                }
                true
            }
            popup.show()
        }
    }

    private fun updateViewState(loadState: LoadState) {
        when (loadState) {
            is LoadState.Error -> {
                binding.swipeRefreshLayout.isRefreshing = false
                binding.recyclerView.isVisible = false
                binding.textViewError.isVisible = true
                binding.textInputLayoutSearch.isEnabled = true
                binding.textViewError.text =
                    loadState.error.message ?: getString(R.string.generic_error)
            }
            LoadState.Loading -> {
                binding.swipeRefreshLayout.isRefreshing = true
                binding.recyclerView.isVisible = true
                binding.textViewError.isVisible = false
                binding.textInputLayoutSearch.isEnabled = false
            }
            is LoadState.NotLoading -> {
                binding.swipeRefreshLayout.isRefreshing = false
                binding.recyclerView.isVisible = true
                binding.textViewError.isVisible = false
                binding.textInputLayoutSearch.isEnabled = true
                binding.recyclerView.scrollToPosition(0)
            }
        }
    }
}