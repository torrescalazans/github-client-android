package com.torrescalazans.githubclient.presentation.home.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.torrescalazans.githubclient.data.model.RepositoryItem
import com.torrescalazans.githubclient.domain.usecase.GetSearchedRepositoriesUseCase
import com.torrescalazans.githubclient.presentation.home.viewmodel.GitHubViewModel.Companion.DEFAULT_SEARCH_QUERY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@kotlinx.coroutines.ExperimentalCoroutinesApi
@HiltViewModel
class GitHubViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSearchedRepositoriesUseCase: GetSearchedRepositoriesUseCase
) : ViewModel() {

    companion object {
        const val DEFAULT_SEARCH_QUERY = "android kotlin"
        const val LAST_SEARCH_QUERY: String = "last_search_query"
        const val LAST_SEARCH_QUERY_SCROLLED: String = "last_search_query_scrolled"
    }

    val pagingDataFlow: Flow<PagingData<RepositoryItem>>

    /**
     * Stream of immutable states representative of the UI.
     */
    val state: StateFlow<UiState>

    /**
     * Processor of side effects from the UI which in turn feedback into [state]
     */
    val accept: (UiAction) -> Unit

    init {
        val initialQuery: String = savedStateHandle.get(LAST_SEARCH_QUERY) ?: DEFAULT_SEARCH_QUERY
        val lastQueryScrolled: String = savedStateHandle.get(LAST_SEARCH_QUERY_SCROLLED) ?: DEFAULT_SEARCH_QUERY
        val actionStateFlow = MutableSharedFlow<UiAction>()

        val searches = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.Search(searchQuery = initialQuery)) }

        val queriesScrolled = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            // This is shared to keep the flow "hot" while caching the last query scrolled,
            // otherwise each flatMapLatest invocation would lose the last query scrolled,
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(UiAction.Scroll(currentSearchQuery = lastQueryScrolled)) }

        pagingDataFlow = searches
            .flatMapLatest { searchRepositories(searchQuery = it.searchQuery) }
            .cachedIn(viewModelScope)

        state = combine(
            searches,
            queriesScrolled,
            ::Pair
        ).map { (search, scroll) ->
            UiState(
                searchQuery = search.searchQuery,
                lastSearchQueryScrolled = scroll.currentSearchQuery,
                // If the search query matches the scroll query, the user has scrolled
                hasNotScrolledForCurrentSearchQuery = search.searchQuery != scroll.currentSearchQuery
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = UiState()
        )

        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
    }

    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY] = state.value.searchQuery
        savedStateHandle[LAST_SEARCH_QUERY_SCROLLED] = state.value.lastSearchQueryScrolled

        super.onCleared()
    }

    private fun searchRepositories(searchQuery: String): Flow<PagingData<RepositoryItem>> =
        getSearchedRepositoriesUseCase.execute(searchQuery)
}

data class UiState(
    val searchQuery: String = DEFAULT_SEARCH_QUERY,
    val lastSearchQueryScrolled: String = DEFAULT_SEARCH_QUERY,
    val hasNotScrolledForCurrentSearchQuery: Boolean = false
)

sealed class UiAction {
    data class Search(val searchQuery: String) : UiAction()
    data class Scroll(val currentSearchQuery: String) : UiAction()
}
