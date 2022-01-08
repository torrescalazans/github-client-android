package com.torrescalazans.githubclient.domain.usecase

import androidx.paging.PagingData
import com.torrescalazans.githubclient.data.model.RepositoryItem
import com.torrescalazans.githubclient.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow

class GetSearchedRepositoriesUseCase(private val gitHubRepository: GitHubRepository) {

    fun execute(searchQuery: String): Flow<PagingData<RepositoryItem>> {
        return gitHubRepository.getSearchRepositoriesStream(searchQuery)
    }
}
