package com.torrescalazans.githubclient.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.torrescalazans.githubclient.databinding.RepositoryListItemLoadStateBinding

class RepositoryLoadStateAdapter(private val retry: () -> Unit)
    : LoadStateAdapter<RepositoryLoadStateAdapter.RepositoryLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): RepositoryLoadStateViewHolder {
        val binding = RepositoryListItemLoadStateBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return RepositoryLoadStateViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: RepositoryLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class RepositoryLoadStateViewHolder(
        private val binding: RepositoryListItemLoadStateBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonRetryRequest.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.textviewErrorMessage.text =
                    loadState.error.localizedMessage
            }

            binding.progressbar.isVisible = loadState is LoadState.Loading
            binding.buttonRetryRequest.isVisible = loadState is LoadState.Error
            binding.textviewErrorMessage.isVisible = loadState is LoadState.Error
        }
    }
}
