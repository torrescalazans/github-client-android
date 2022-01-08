package com.torrescalazans.githubclient.domain.repository

import androidx.paging.PagingData
import com.torrescalazans.githubclient.data.model.RepositoryItem
import kotlinx.coroutines.flow.Flow

interface GitHubRepository {

    fun getSearchRepositoriesStream(searchQuery: String): Flow<PagingData<RepositoryItem>>
}
