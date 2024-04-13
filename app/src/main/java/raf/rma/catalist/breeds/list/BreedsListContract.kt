package raf.rma.catalist.breeds.list

import raf.rma.catalist.breeds.model.BreedsUiModel

interface BreedsListContract {
    data class BreedsListState (
        val breeds: List<BreedsUiModel> = emptyList(),
        val loading: Boolean = false,
        val query: String = "",
        val error: Boolean = false,
        val isSearchMode: Boolean = false,
        val filteredBreeds: List<BreedsUiModel> = emptyList()
    )
    sealed class BreedsListUiEvent{
        data class SearchQueryChanged(val query: String) : BreedsListUiEvent()
        data object CloseSearchMode : BreedsListUiEvent()
    }
}