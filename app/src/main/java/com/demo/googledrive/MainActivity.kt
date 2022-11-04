package com.demo.googledrive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.demo.googledrive.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host: NavHostFragment? = supportFragmentManager.findFragmentById(R.id.nav_host) as? NavHostFragment?

        host?.navController?.navInflater?.inflate(R.navigation.default_navigation)?.apply {
            setStartDestination(this@MainActivity.getStartDestination())
            host.navController.graph = this
        }

    }

    private fun getStartDestination(): Int {
        return if(GoogleSignIn.getLastSignedInAccount(this) == null) {
            R.id.fragmentLogin
        } else {
            R.id.fragmentFiles
        }
    }
}