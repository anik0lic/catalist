package raf.rma.catalist.breeds.details

import android.telephony.DataFailCause
import raf.rma.catalist.breeds.domain.BreedsData

data class BreedsDetailsState (
    val breedId: String,
    val fetching: Boolean = false,
    val data: BreedsData? = null,
    val error: DetailsError? = null
){

    sealed class DetailsError {
        data class DataUpdateFailed(val cause: Throwable? = null) : DetailsError()
    }
}