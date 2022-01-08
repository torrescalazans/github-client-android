package com.torrescalazans.githubclient.data.repository.remote.datasourceimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.torrescalazans.githubclient.data.api.GitHubService
import com.torrescalazans.githubclient.data.api.GitHubService.Companion.IN_QUALIFIER
import com.torrescalazans.githubclient.data.model.RepositoryItem
import com.torrescalazans.githubclient.data.repository.remote.datasource.GitHubRemoteDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

class GitHubRemoteDataSourceImpl(
    private val gitHubService: GitHubService,
    private val searchQuery: String
) : PagingSource<Int, RepositoryItem>(), GitHubRemoteDataSource {

    companion object {
        const val INITIAL_PAGE_INDEX = 1
        const val NETWORK_PAGE_SIZE = 30
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryItem> {
        val position = params.key ?: INITIAL_PAGE_INDEX

        try {
            val response = gitHubService.searchRepositories(
                searchQuery + IN_QUALIFIER,
                position,
                params.loadSize
            )
            val repositories = response.repositoryItems

            val nextKey = if (repositories.isEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }

            return LoadResult.Page(
                data = repositories,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, RepositoryItem>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override fun getSearchRepositoriesStream(searchQuery: String): Flow<PagingData<RepositoryItem>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = NETWORK_PAGE_SIZE,
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PAGE_SIZE / 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GitHubRemoteDataSourceImpl(gitHubService, searchQuery) }
        ).flow
    }
}
