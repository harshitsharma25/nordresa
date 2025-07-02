package com.nordresa.travel

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.nordresa.travel.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Connect BottomNavigationView with NavController (NavigationUI)
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        // Handle conditional start destination
        val destination = intent.getStringExtra("destination")
        if (destination == "signin") {
            println("--> user not signed in -> go to signIn fragment")
            navController.navigate(R.id.signInFragment)
        } // else it defaults to home

        // Hide BottomNav for specific destinations (e.g., OrderSummaryFragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                R.id.signInFragment -> hideBottomNav()
                R.id.signUpFragment -> hideBottomNav()
                R.id.searchFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }
    }

    private fun hideBottomNav() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    private fun showBottomNav() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}