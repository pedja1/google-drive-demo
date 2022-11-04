package com.demo.googledrive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.drive.data.GlideApp
import com.demo.googledrive.databinding.FragmentPreviewBinding

class FragmentPreview : BaseFragment<FragmentPreviewBinding>() {

    private val previewUrl by lazy { com.demo.googledrive.ui.fragment.FragmentPreviewArgs.fromBundle(
        requireArguments()
    ).previewUrl }

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentPreviewBinding {
        return FragmentPreviewBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        GlideApp.with(requireContext()).load(previewUrl).into(binding.imageViewPreview)
    }
}