package com.torrescalazans.githubclient.core

import com.torrescalazans.githubclient.data.api.GitHubService
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

open class MockWebServerTest {

    companion object {
        const val MOCK_GITHUB_PAGE_1_RESPONSE_FILE_PATH = "mock/api/github_page_1_response.json"
        const val MOCK_GITHUB_PAGE_2_RESPONSE_FILE_PATH = "mock/api/github_page_2_response.json"

        const val CONNECTION_TIMEOUT = 1L
        const val CONNECTION_READ_TIMEOUT = 1L
        const val CONNECTION_WRITE_TIMEOUT = 1L
    }

    protected lateinit var server: MockWebServer
    protected lateinit var gitHubService: GitHubService

    @Before
    fun setUp() {
        server = MockWebServer()

        val httpClient = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(CONNECTION_READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(CONNECTION_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()

        gitHubService = Retrofit.Builder()
            .baseUrl(server.url(""))
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GitHubService::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    protected fun enqueueMockResponse(fileName: String) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockResponse.setBody(source.readString(Charsets.UTF_8))

        server.enqueue(mockResponse)
    }

    protected fun enqueueTimeoutMockResponse() {
        val mockResponse = MockResponse()

        mockResponse.socketPolicy = SocketPolicy.NO_RESPONSE

        server.enqueue(mockResponse)
    }
}
