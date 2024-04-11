package raf.rma.catalist.breeds.list

import raf.rma.catalist.breeds.model.BreedsUiModel

interface BreedsListContract {
    data class BreedsListState (
        val breeds: List<BreedsUiModel> = emptyList(),
        val fetching: Boolean = false,
        val query: String = "",
//        val error: ListError? = null,
        val isSearchMode: Boolean = false,
        val filteredBreeds: List<BreedsUiModel> = emptyList()
    )
//    {
//        sealed class ListError {
//            data class ListUpdateFailed(val cause: Throwable? = null) : ListError()
//        }
//    }
    sealed class BreedsListUiEvent{
        data class SearchQueryChanged(val query: String) : BreedsListUiEvent()
        data object CloseSearchMode : BreedsListUiEvent()
    }
}