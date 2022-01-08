package com.torrescalazans.githubclient.di

import com.torrescalazans.githubclient.data.api.GitHubService
import com.torrescalazans.githubclient.data.repository.remote.datasource.GitHubRemoteDataSource
import com.torrescalazans.githubclient.data.repository.remote.datasourceimpl.GitHubRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataSourceModule {

    @Singleton
    @Provides
    fun provideGitHubRemoteDataSource(
        gitHubService: GitHubService,
    ): GitHubRemoteDataSource {
        return GitHubRemoteDataSourceImpl(gitHubService, "")
    }
}
