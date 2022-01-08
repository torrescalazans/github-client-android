package com.torrescalazans.githubclient.di

import com.torrescalazans.githubclient.data.repository.GitHubRepositoryImpl
import com.torrescalazans.githubclient.data.repository.remote.datasource.GitHubRemoteDataSource
import com.torrescalazans.githubclient.domain.repository.GitHubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideGitHubRepository(gitHubRemoteDataSource: GitHubRemoteDataSource): GitHubRepository {
        return GitHubRepositoryImpl(gitHubRemoteDataSource)
    }
}
