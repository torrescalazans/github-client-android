package com.torrescalazans.githubclient.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.torrescalazans.githubclient.R
import com.torrescalazans.githubclient.data.model.RepositoryItem
import com.torrescalazans.githubclient.databinding.RepositoryListItemBinding

class RepositoryAdapter : PagingDataAdapter<RepositoryItem, RepositoryAdapter.RepositoryViewHolder>(REPOSITORY_COMPARATOR) {

    companion object {
        private val REPOSITORY_COMPARATOR = object : DiffUtil.ItemCallback<RepositoryItem>() {
            override fun areItemsTheSame(
                oldItem: RepositoryItem,
                newItem: RepositoryItem
            ): Boolean =
                oldItem.fullName == newItem.fullName

            override fun areContentsTheSame(
                oldItem: RepositoryItem,
                newItem: RepositoryItem
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val binding = RepositoryListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return RepositoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repositoryItem = getItem(position)

        if (repositoryItem != null) {
            holder.bind(repositoryItem)
        }
    }

    private var onItemClickListener: ((RepositoryItem)-> Unit)? = null
    fun setOnItemClickListener(listener: (RepositoryItem)->Unit) {
        onItemClickListener = listener
    }

    inner class RepositoryViewHolder(private val binding: RepositoryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(repositoryItem: RepositoryItem) {
            with(binding) {
                Glide.with(imageviewAvatar.context)
                    .load(repositoryItem.repositoryOwner.avatarUrl)
                    .into(imageviewAvatar)

                textviewRepositoryOwnerLoginName.text = repositoryItem.repositoryOwner.login

                textviewRepositoryName.text = repositoryItem.name

                var repositoryDescriptionVisibility = View.GONE
                if (!repositoryItem.description.isNullOrEmpty()) {
                    textviewRepositoryDescription.text = repositoryItem.description
                    repositoryDescriptionVisibility = View.VISIBLE
                }
                textviewRepositoryDescription.visibility = repositoryDescriptionVisibility

                textviewRepositoryStarsCount.text = repositoryItem.stargazersCount.toString()
                textviewRepositoryForksCount.text = repositoryItem.forksCount.toString()

                var repositoryLanguageVisibility = View.GONE
                if (!repositoryItem.language.isNullOrEmpty()) {
                    val resources = itemView.resources
                    textviewRepositoryLanguage.text =
                        resources.getString(R.string.repository_list_item_language, repositoryItem.language)
                    repositoryLanguageVisibility = View.VISIBLE
                }
                textviewRepositoryLanguage.visibility = repositoryLanguageVisibility

                binding.root.setOnClickListener {
                    onItemClickListener?.let {
                        it(repositoryItem)
                    }
                }
            }
        }
    }
}
