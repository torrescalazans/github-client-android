package com.torrescalazans.githubclient.di

import com.torrescalazans.githubclient.domain.repository.GitHubRepository
import com.torrescalazans.githubclient.domain.usecase.GetSearchedRepositoriesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun provideGetSearchedRepositoriesUseCase(gitHubRepository: GitHubRepository): GetSearchedRepositoriesUseCase {
        return GetSearchedRepositoriesUseCase(gitHubRepository)
    }
}
