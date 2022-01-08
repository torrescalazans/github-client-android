package com.torrescalazans.githubclient.data.api

import com.torrescalazans.githubclient.data.model.GitHubResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubService {

    companion object {
        const val IN_QUALIFIER = "in:name,description"
    }

    /**
     * Retrofit calls that return the body type throw either IOException for network
     * failures, or HttpException for any non-2xx HTTP status codes. This code reports all
     * errors to the UI, but you can inspect/wrap the exceptions to provide more context.
     *
     * If needed, you should change from GitHubResponse to Response<GitHubResponse> and update
     * its usage throughout the code.
     */
    @GET("search/repositories?sort=stars")
    suspend fun searchRepositories(
        @Query("q") searchQuery: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): GitHubResponse
}
