package com.demo.googledrive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.demo.googledrive.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<T: ViewBinding> : Fragment() {

    private var _binding: T? = null
        private set
    protected val binding get() = _binding!!

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bind(inflater, container).run {
            _binding = this
            root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun showErrorWithFallback(message: String?) {
        Snackbar.make(
            binding.root,
            message ?: getString(R.string.generic_error),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    protected abstract fun bind(inflater: LayoutInflater, container: ViewGroup?): T
}