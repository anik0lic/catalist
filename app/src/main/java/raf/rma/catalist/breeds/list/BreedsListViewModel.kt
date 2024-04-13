package raf.rma.catalist.breeds.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import raf.rma.catalist.breeds.api.model.BreedsApiModel
import raf.rma.catalist.breeds.list.BreedsListContract.BreedsListUiEvent
import raf.rma.catalist.breeds.model.BreedsUiModel
import raf.rma.catalist.breeds.repository.BreedRepository
import java.io.IOException
import kotlin.time.Duration.Companion.seconds

class BreedsListViewModel (
    private val repository: BreedRepository = BreedRepository
): ViewModel() {

    private val _state = MutableStateFlow(BreedsListContract.BreedsListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedsListContract.BreedsListState.() -> BreedsListContract.BreedsListState) = _state.getAndUpdate(reducer)

    private val events = MutableSharedFlow<BreedsListUiEvent>()
    fun setEvent(event: BreedsListUiEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        fetchAllBreeds()
        observeSearchQuery()
    }

    private fun fetchAllBreeds(){
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val breeds = withContext(Dispatchers.IO) {
                    repository.getAllBreeds().map { it.asBreedsUiModel() }
                }
                setState { copy(breeds = breeds) }
            } catch (error: IOException) {
                setState { copy(error = true) }
            } finally {
                setState { copy(loading = false) }
            }
        }
    }

    private fun observeEvents(){
        viewModelScope.launch {
            events.collect {
                when (it) {
                    BreedsListUiEvent.CloseSearchMode -> setState { copy(isSearchMode = false) }

                    is BreedsListUiEvent.SearchQueryChanged -> {
                        println("search query changed")
                        println(it.query)
                        setState { copy(query = it.query) }
                        setState { copy(isSearchMode = true) }
                        setState { copy(loading = true) }
                    }
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery(){
        viewModelScope.launch {
            events
                .filterIsInstance<BreedsListUiEvent.SearchQueryChanged>()
                .debounce(1.seconds)
                .collect {
                    setState { copy(filteredBreeds = state.value.breeds.filter { item ->
                        item.name.contains(state.value.query, ignoreCase = true)
                    }) }

                    setState { copy(loading = false) }
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
}