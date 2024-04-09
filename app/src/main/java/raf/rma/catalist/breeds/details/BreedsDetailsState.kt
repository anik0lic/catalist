package raf.rma.catalist.breeds.details

import android.telephony.DataFailCause
import raf.rma.catalist.breeds.domain.BreedsData
import raf.rma.catalist.breeds.domain.BreedsUiModel

data class BreedsDetailsState (
    val breedId: String,
    val fetching: Boolean = false,
    val data: BreedsUiModel? = null,
    val error: DetailsError? = null
){

    sealed class DetailsError {
        data class DataUpdateFailed(val cause: Throwable? = null) : DetailsError()
    }
}