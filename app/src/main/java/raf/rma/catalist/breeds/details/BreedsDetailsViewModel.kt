package raf.rma.catalist.breeds.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import raf.rma.catalist.breeds.api.model.BreedsApiModel
import raf.rma.catalist.breeds.api.model.ImageApiModel
import raf.rma.catalist.breeds.details.BreedsDetailsContract.BreedsDetailsState
import raf.rma.catalist.breeds.model.BreedsUiModel
import raf.rma.catalist.breeds.model.ImageUiModel
import raf.rma.catalist.breeds.repository.BreedRepository
import java.io.IOException

class BreedsDetailsViewModel (
    private val breedId: String,
    private val repository: BreedRepository = BreedRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BreedsDetailsState(breedId = breedId))
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedsDetailsState.() -> BreedsDetailsState) =
        _state.getAndUpdate(reducer)

    init {
        fetchBreedDetails()
    }

    private fun fetchBreedDetails(){
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val data = withContext(Dispatchers.IO) {
                    repository.getBreedDetails(breedId = breedId).asBreedsUiModel()
                }
                setState { copy(breedId = data.id) }
                setState { copy(data = data) }
                fetchImage(data.referenceImageId)
            } catch (error: IOException) {
                setState { copy(error = true) }
            } finally {
                setState { copy(loading = false) }
            }
        }
    }

    private fun fetchImage(imageId: String){
        viewModelScope.launch {
            try {
                val image = withContext(Dispatchers.IO) {
                    repository.getImage(imageId = imageId).asImageUiModel()
                }
                setState { copy( image = image) }
            } catch (error: IOException) {
                setState { copy(error = true) }
            }
        }
    }

    private fun BreedsApiModel.asBreedsUiModel() = BreedsUiModel(
        id = this.id,
        name = this.name,
        description = this.description,
        alternativeName = this.alternativeName,
        lifeSpan = this.lifeSpan,
        origin = this.origin,
        rare = this.rare,
        temperament = this.temperament,
        weight = this.weight.metric,
        wikipediaURL = this.wikipediaURL,

        referenceImageId = this.referenceImageId,

        adaptability = this.adaptability,
        affectionLevel = this.affectionLevel,
        intelligence = this.intelligence,
        socialNeeds = this.socialNeeds,
        childFriendly = this.childFriendly
    )

    private fun ImageApiModel.asImageUiModel() = ImageUiModel(
        id = this.id,
        url = this.url
    )
}