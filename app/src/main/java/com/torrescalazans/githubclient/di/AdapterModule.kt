package com.torrescalazans.githubclient.di

import com.torrescalazans.githubclient.presentation.home.adapter.RepositoryAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdapterModule {

    @Singleton
    @Provides
    fun provideRepositoryAdapter(): RepositoryAdapter {
        return RepositoryAdapter()
    }
}
