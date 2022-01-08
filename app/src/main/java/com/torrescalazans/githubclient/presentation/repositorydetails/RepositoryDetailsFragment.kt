package com.torrescalazans.githubclient.presentation.repositorydetails

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.torrescalazans.githubclient.R
import com.torrescalazans.githubclient.data.model.RepositoryItem
import com.torrescalazans.githubclient.databinding.FragmentRepositoryDetailsBinding

class RepositoryDetailsFragment : Fragment() {

    private lateinit var binding: FragmentRepositoryDetailsBinding

    private lateinit var repository: RepositoryItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_repository_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRepositoryDetailsBinding.bind(view)

        binding.buttonRetryRequest.setOnClickListener {
            binding.progressbar.isVisible = true
            binding.textviewRequestErrorMessage.isVisible = false
            binding.buttonRetryRequest.isVisible = false

            loadRepositoryPage()
        }

        val args: RepositoryDetailsFragmentArgs by navArgs()
        repository = args.selectedRepository

        binding.webview.apply {
            webViewClient = object : WebViewClient() {
                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
                ) {
                    binding.textviewRequestErrorMessage.isVisible = true
                    binding.textviewRequestErrorMessage.text = description

                    binding.buttonRetryRequest.isVisible = true
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)

                    binding.progressbar.isVisible = true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)

                    binding.progressbar.isVisible = false
                }
            }

            loadRepositoryPage()
        }
    }

    private fun loadRepositoryPage() {
        if (repository.htmlUrl.isNotEmpty()) {
            binding.webview.loadUrl(repository.htmlUrl)
        }
    }
}
