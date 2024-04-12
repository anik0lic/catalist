package raf.rma.catalist.breeds.details

import raf.rma.catalist.breeds.model.BreedsUiModel
import raf.rma.catalist.breeds.model.ImageUiModel

interface BreedsDetailsContract {
    data class BreedsDetailsState (
        val breedId: String,
        val loading: Boolean = false,
        val data: BreedsUiModel? = null,
        val image: ImageUiModel? = null,
        val error: Boolean = false
    ){

//        sealed class DetailsError {
//            data class DataUpdateFailed(val cause: Throwable? = null) : DetailsError()
//        }
    }
}