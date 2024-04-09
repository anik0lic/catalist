package raf.rma.catalist.breeds.list

import raf.rma.catalist.breeds.domain.BreedsData
import raf.rma.catalist.breeds.domain.BreedsUiModel

data class BreedsListState (
    val breeds: List<BreedsUiModel> = emptyList(),
    val fetching: Boolean = false,
    val error: ListError? = null
){
    sealed class ListError {
        data class ListUpdateFailed(val cause: Throwable? = null) : ListError()
    }
}