package com.torrescalazans.githubclient.presentation.home

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.torrescalazans.githubclient.R
import com.torrescalazans.githubclient.data.model.RepositoryItem
import com.torrescalazans.githubclient.databinding.FragmentRepositoriesBinding
import com.torrescalazans.githubclient.presentation.home.adapter.RepositoryAdapter
import com.torrescalazans.githubclient.presentation.home.adapter.RepositoryLoadStateAdapter
import com.torrescalazans.githubclient.presentation.home.viewmodel.GitHubViewModel
import com.torrescalazans.githubclient.presentation.home.viewmodel.UiAction
import com.torrescalazans.githubclient.presentation.home.viewmodel.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@kotlinx.coroutines.ExperimentalCoroutinesApi
class RepositoriesFragment : Fragment() {

    private lateinit var binding: FragmentRepositoriesBinding
    private lateinit var gitHubViewModel: GitHubViewModel
    private lateinit var repositoryAdapter: RepositoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_repositories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRepositoriesBinding.bind(view)

        gitHubViewModel = (activity as HomeActivity).gitHubViewModel

        repositoryAdapter = (activity as HomeActivity).repositoryAdapter
        repositoryAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("selected_repository", it)
            }

            findNavController().navigate(
                R.id.action_repositoriesFragment_to_repositoryDetailsFragment,
                bundle
            )
        }

        // bind the state
        binding.bindState(
            uiState = gitHubViewModel.state,
            pagingData = gitHubViewModel.pagingDataFlow,
            uiActions = gitHubViewModel.accept
        )
    }

    private fun FragmentRepositoriesBinding.bindState(
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<RepositoryItem>>,
        uiActions: (UiAction) -> Unit
    ) {
        recyclerviewRepositories.adapter = repositoryAdapter.withLoadStateHeaderAndFooter(
            header = RepositoryLoadStateAdapter { repositoryAdapter.retry() },
            footer = RepositoryLoadStateAdapter { repositoryAdapter.retry() }
        )

        bindSearch(
            uiState = uiState,
            onSearchQueryChanged = uiActions
        )

        bindList(
            repositoryAdapter = repositoryAdapter,
            uiState = uiState,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )
    }

    private fun FragmentRepositoriesBinding.bindSearch(
        uiState: StateFlow<UiState>,
        onSearchQueryChanged: (UiAction.Search) -> Unit
    ) {
        edittextSearchRepository.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepositoriesListFromInput(onSearchQueryChanged)
                true
            } else {
                false
            }
        }
        edittextSearchRepository.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepositoriesListFromInput(onSearchQueryChanged)
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            uiState
                .map { it.searchQuery }
                .distinctUntilChanged()
                .collect(edittextSearchRepository::setText)
        }
    }

    private fun FragmentRepositoriesBinding.updateRepositoriesListFromInput(onSearchQueryChanged: (UiAction.Search) -> Unit) {
        edittextSearchRepository.text.trim().let {
            if (it.isNotEmpty()) {
                recyclerviewRepositories.scrollToPosition(0)
                onSearchQueryChanged(UiAction.Search(searchQuery = it.toString()))
            }
        }
    }

    private fun FragmentRepositoriesBinding.bindList(
        repositoryAdapter: RepositoryAdapter,
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<RepositoryItem>>,
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {
        buttonRetryRequest.setOnClickListener { repositoryAdapter.retry() }

        recyclerviewRepositories.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(UiAction.Scroll(currentSearchQuery = uiState.value.searchQuery))
            }
        })

        val notLoading = repositoryAdapter.loadStateFlow
            // Only emit when REFRESH LoadState for the paging source changes.
            .distinctUntilChangedBy { it.source.refresh }
            // Only react to cases where REFRESH completes i.e., NotLoading.
            .map { it.source.refresh is LoadState.NotLoading }

        val hasNotScrolledForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentSearchQuery }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        ).distinctUntilChanged()

        lifecycleScope.launch {
            pagingData.collectLatest(repositoryAdapter::submitData)
        }

        lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) recyclerviewRepositories.scrollToPosition(0)
            }
        }

        lifecycleScope.launch {
            repositoryAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty =
                    loadState.source.refresh is LoadState.NotLoading && repositoryAdapter.itemCount == 0

                // show empty list
                textviewEmptyList.isVisible = isListEmpty

                // Only show the list if refresh succeeds.
                recyclerviewRepositories.isVisible = loadState.source.refresh is LoadState.NotLoading && !isListEmpty

                // Show loading spinner during initial load or refresh.
                progressbarRepositoryLoadState.isVisible =
                    loadState.source.refresh is LoadState.Loading

                // Show the retry state if initial load or refresh fails.
                buttonRetryRequest.isVisible =
                    loadState.source.refresh is LoadState.Error

                // Show error message if initial load or refresh fails.
                textviewRequestErrorMessage.isVisible = loadState.source.refresh is LoadState.Error

                val errorState =
                    loadState.source.refresh as? LoadState.Error
                        ?: loadState.source.append as? LoadState.Error
                        ?: loadState.source.prepend as? LoadState.Error
                        ?: loadState.refresh as? LoadState.Error
                        ?: loadState.append as? LoadState.Error
                        ?: loadState.prepend as? LoadState.Error

                errorState?.let {
                    textviewRequestErrorMessage.text = it.error.localizedMessage
                }
            }
        }
    }
}
