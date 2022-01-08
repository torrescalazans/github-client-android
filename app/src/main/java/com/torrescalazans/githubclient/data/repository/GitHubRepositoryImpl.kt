package com.torrescalazans.githubclient.data.repository

import androidx.paging.PagingData
import com.torrescalazans.githubclient.data.model.RepositoryItem
import com.torrescalazans.githubclient.data.repository.remote.datasource.GitHubRemoteDataSource
import com.torrescalazans.githubclient.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow

class GitHubRepositoryImpl(
    private val  gitHubRemoteDataSource: GitHubRemoteDataSource,
) : GitHubRepository {

    override fun getSearchRepositoriesStream(searchQuery: String): Flow<PagingData<RepositoryItem>> {
        return gitHubRemoteDataSource.getSearchRepositoriesStream(searchQuery)
    }
}
