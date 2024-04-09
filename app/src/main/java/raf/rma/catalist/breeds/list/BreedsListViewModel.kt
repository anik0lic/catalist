package raf.rma.catalist.breeds.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import raf.rma.catalist.breeds.api.model.BreedsApiModel
import raf.rma.catalist.breeds.model.BreedsUiModel
import raf.rma.catalist.breeds.repository.BreedRepository
import java.io.IOException

class BreedsListViewModel (
    private val repository: BreedRepository = BreedRepository
): ViewModel() {

    private val _state = MutableStateFlow(BreedsListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedsListState.() -> BreedsListState) = _state.getAndUpdate(reducer)

    init {
//        observeBreeds()
        fetchAllBreeds()
    }

    private fun observeBreeds(){
        // We are launching a new coroutine
//        viewModelScope.launch {
            // Which will observe all changes to our passwords
//            repository.observeBreeds().collect {
//                setState { copy(breeds = it) }
//            }
//        }
    }

    private fun fetchAllBreeds(){
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                val breeds = withContext(Dispatchers.IO) {
                    repository.fetchAllBreeds().map { it.asBreedsUiModel() }
                }
                setState { copy(breeds = breeds) }
            } catch (error: IOException) {
                setState { copy(error = BreedsListState.ListError.ListUpdateFailed(cause = error)) }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }

    private fun BreedsApiModel.asBreedsUiModel() = BreedsUiModel(
        id = this.id,
        name = this.name,
        description = this.description,
        alternativeName = this.alternativeName,
        adaptability = this.adaptability,
        affectionLevel = this.affectionLevel,
        imageUrl = this.image.url,
        lifeSpan = this.lifeSpan,
        origin = this.origin,
        rare = this.rare,
        temperament = this.temperament,
        weight = this.weight.metric,
        wikipediaURL = this.wikipediaURL
    )
}