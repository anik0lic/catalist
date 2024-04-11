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
            setState { copy(fetching = true) }
            try {
                val breeds = withContext(Dispatchers.IO) {
                    repository.getAllBreeds().map { it.asBreedsUiModel() }
                }
                setState { copy(breeds = breeds) }
            } catch (error: IOException) {
//                setState { copy(error = BreedsListContract.BreedsListState.ListError.ListUpdateFailed(cause = error)) }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }

    private fun observeEvents(){
        viewModelScope.launch {
            events.collect {
                when (it) {
//                    BreedsListUiEvent.ClearSearch -> Unit
                    BreedsListUiEvent.CloseSearchMode -> setState { copy(isSearchMode = false) }

                    is BreedsListUiEvent.SearchQueryChanged -> {
                        println("search query changed")
                        println(it.query)
                        setState { copy(query = it.query) }
                        setState { copy(isSearchMode = true) }
                        // onValueChange from OutlinedTextField is called for every character

                        // We should handle the query text state update here, but make the api call
                        // or any other expensive call somewhere else where we debounce the text changes
                        it.query // this should be added to state
                    }
//
//                    BreedsListUiEvent.Dummy -> Unit
//                    is BreedsListUiEvent.RemoveUser -> {
//                        BreedsListUiEvent(breedId = it.breedId)
//                    }
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery(){
        viewModelScope.launch {
            events
                .filterIsInstance<BreedsListUiEvent.SearchQueryChanged>()
                .debounce(2.seconds)
                .collect {
                    println("observe search query")
                    // Called only when search query was changed
                    // and once 2 seconds have passed after the last char
//                    val filtered = state.value.breeds.filter { item ->
//                        item.name.contains(query, ignoreCase = true)
//                    }

                    setState { copy(filteredBreeds = state.value.breeds.filter { item ->
                        item.name.contains(state.value.query, ignoreCase = true)
                    }) }
                    // This is helpful to avoid trigger expensive calls
                    // on every character change
                    //s
                    //si
                    //sib
                    //sibi
                    //sibir
                    //sibirs
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
        lifeSpan = this.lifeSpan,
        origin = this.origin,
        rare = this.rare,
        temperament = this.temperament,
        weight = this.weight.metric,
        wikipediaURL = this.wikipediaURL,
        referenceImageId = this.referenceImageId
    )
}