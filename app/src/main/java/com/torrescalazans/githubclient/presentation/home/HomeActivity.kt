package com.torrescalazans.githubclient.presentation.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.torrescalazans.githubclient.databinding.ActivityHomeBinding
import com.torrescalazans.githubclient.presentation.home.viewmodel.GitHubViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.torrescalazans.githubclient.R
import com.torrescalazans.githubclient.presentation.home.adapter.RepositoryAdapter
import javax.inject.Inject

@kotlinx.coroutines.ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    companion object {
        private const val TIME_DELAY_CONFIRM_EXIT_MILLIS = 2000L
    }

    private lateinit var binding: ActivityHomeBinding

    lateinit var gitHubViewModel: GitHubViewModel

    private lateinit var navController: NavController

    @Inject
    lateinit var repositoryAdapter: RepositoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        gitHubViewModel = ViewModelProvider(this)[GitHubViewModel::class.java]
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (!navController.navigateUp()) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, R.string.activity_home_confirm_exit, Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, TIME_DELAY_CONFIRM_EXIT_MILLIS)
        }
    }
}
