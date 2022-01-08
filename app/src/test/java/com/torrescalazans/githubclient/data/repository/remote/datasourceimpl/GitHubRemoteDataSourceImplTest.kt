package com.torrescalazans.githubclient.data.repository.remote.datasourceimpl

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult
import com.google.common.truth.Truth.assertThat
import com.torrescalazans.githubclient.core.MockWebServerTest
import com.torrescalazans.githubclient.data.model.RepositoryItem
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.net.SocketTimeoutException

class GitHubRemoteDataSourceImplTest: MockWebServerTest() {

    @Test
    fun load_receivedSocketTimeoutExceptionResponse_correctPagingSourceLoadResult() = runTest {
        enqueueTimeoutMockResponse()

        val pagingSource = GitHubRemoteDataSourceImpl(gitHubService, "")

        val actualPagingSourceLoadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        val expectedPagingSourceLoadResult =
            LoadResult.Error<Int, RepositoryItem>(SocketTimeoutException("timeout"))

        assertThat(actualPagingSourceLoadResult.toString()).isEqualTo(expectedPagingSourceLoadResult.toString())
    }
}
