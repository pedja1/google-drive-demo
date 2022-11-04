package com.demo.googledrive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.demo.googledrive.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope

class FragmentLogin : BaseFragment<FragmentLoginBinding>() {

    companion object {
        private val SCOPE_DRIVE_FULL = Scope(Scopes.DRIVE_FULL)
    }

    private val googleSignInClient by lazy {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(SCOPE_DRIVE_FULL)
            .build()
        GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
    }

    private val activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        try {
            val googleSignInAccount = GoogleSignIn.getSignedInAccountFromIntent(it.data).getResult(ApiException::class.java)
            checkPermissionAndContinue(googleSignInAccount)
        } catch (e: Exception) {
            showErrorWithFallback(e.message)
        }
    }

    private fun checkPermissionAndContinue(googleSignInAccount: GoogleSignInAccount?) {
        if (GoogleSignIn.hasPermissions(googleSignInAccount, SCOPE_DRIVE_FULL)) {
            findNavController().navigate(com.demo.googledrive.ui.fragment.FragmentLoginDirections.actionFragmentLoginToFragmentFiles())
        } else {
            //TODO error
        }
    }

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonSignIn.setOnClickListener {
            activityLauncher.launch(googleSignInClient.signInIntent)
        }
        checkPermissionAndContinue(GoogleSignIn.getLastSignedInAccount(requireContext()))
    }

}