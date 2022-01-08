package com.torrescalazans.githubclient.data.api

import com.google.common.truth.Truth.assertThat
import com.torrescalazans.githubclient.core.MockWebServerTest
import kotlinx.coroutines.test.runTest
import org.junit.Test

@kotlinx.coroutines.ExperimentalCoroutinesApi
class GitHubServiceTest: MockWebServerTest() {

    companion object {
        const val REQUEST_PATH = "/search/repositories?sort=stars&q=Androidin%3Aname%2Cdescription&page=1&per_page=30"
        const val REQUEST_SEARCH_QUERY = "Android" + GitHubService.IN_QUALIFIER
        const val REQUEST_INITIAL_PAGE = 1
        const val REQUEST_PAGE_SIZE = 30
    }

    @Test
    fun searchRepositories_sentRequest_responseExpected() {
        runTest {
            enqueueMockResponse(MOCK_GITHUB_PAGE_1_RESPONSE_FILE_PATH)

            val repositoryItems = gitHubService.searchRepositories(
                REQUEST_SEARCH_QUERY,
                REQUEST_INITIAL_PAGE,
                REQUEST_PAGE_SIZE
            ).repositoryItems

            val request = server.takeRequest()

            assertThat(repositoryItems).isNotNull()
            assertThat(request.path).isEqualTo(REQUEST_PATH)
        }
    }

    @Test
    fun searchRepositories_receivedResponse_correctPageSize() {
        runTest {
            enqueueMockResponse(MOCK_GITHUB_PAGE_1_RESPONSE_FILE_PATH)

            val repositoryItems = gitHubService.searchRepositories(
                REQUEST_SEARCH_QUERY,
                REQUEST_INITIAL_PAGE,
                REQUEST_PAGE_SIZE
            ).repositoryItems

            assertThat(repositoryItems.size).isEqualTo(REQUEST_PAGE_SIZE)
        }
    }

    @Test
    fun searchRepositories_receivedResponse_correctContent() {
        runTest {
            enqueueMockResponse(MOCK_GITHUB_PAGE_1_RESPONSE_FILE_PATH)

            val repositoryItems = gitHubService.searchRepositories(
                REQUEST_SEARCH_QUERY,
                REQUEST_INITIAL_PAGE,
                REQUEST_PAGE_SIZE
            ).repositoryItems

            val repositoryItem = repositoryItems[0]

            assertThat(repositoryItem.repositoryOwner.avatarUrl).isEqualTo("https://avatars.githubusercontent.com/u/3427627?v=4")
            assertThat(repositoryItem.repositoryOwner.login).isEqualTo("Genymobile")
            assertThat(repositoryItem.name).isEqualTo("scrcpy")
            assertThat(repositoryItem.description).isEqualTo("Display and control your Android device")
            assertThat(repositoryItem.stargazersCount.toString()).isEqualTo("59423")
            assertThat(repositoryItem.forksCount.toString()).isEqualTo("6241")
            assertThat(repositoryItem.language).isEqualTo("C")
        }
    }
}
