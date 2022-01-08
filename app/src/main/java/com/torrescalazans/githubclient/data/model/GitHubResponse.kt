package com.torrescalazans.githubclient.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GitHubResponse(
    @Json(name = "incomplete_results")
    val incompleteResults: Boolean,

    @Json(name = "items")
    val repositoryItems: List<RepositoryItem>,

    @Json(name = "total_count")
    val totalCount: Int
)
