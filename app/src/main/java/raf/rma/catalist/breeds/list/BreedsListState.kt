package raf.rma.catalist.breeds.list

import raf.rma.catalist.breeds.domain.BreedsData

data class BreedsListState (
    val breeds: List<BreedsData> = emptyList(),
    val fetching: Boolean = false,
    val error: ListError? = null
){
    sealed class ListError {
        data class ListUpdateFailed(val cause: Throwable? = null) : ListError()
    }
}