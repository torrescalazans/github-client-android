package com.torrescalazans.githubclient.data.repository.remote.datasource

import androidx.paging.PagingData
import com.torrescalazans.githubclient.data.model.RepositoryItem
import kotlinx.coroutines.flow.Flow

interface GitHubRemoteDataSource {

    fun getSearchRepositoriesStream(searchQuery: String): Flow<PagingData<RepositoryItem>>
}
