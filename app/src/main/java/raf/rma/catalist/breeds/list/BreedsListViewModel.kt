package raf.rma.catalist.breeds.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import raf.rma.catalist.breeds.repository.BreedRepository
import java.io.IOException

class BreedsListViewModel (
    private val repository: BreedRepository = BreedRepository
): ViewModel() {

    private val _state = MutableStateFlow(BreedsListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedsListState.() -> BreedsListState) = _state.getAndUpdate(reducer)

    init {
        observeBreeds()
        fetchBreeds()
    }

    private fun observeBreeds(){
        // We are launching a new coroutine
        viewModelScope.launch {
            // Which will observe all changes to our passwords
            repository.observeBreeds().collect {
                setState { copy(breeds = it) }
            }
        }
    }

    private fun fetchBreeds(){
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                withContext(Dispatchers.IO) {
                    repository.fetchBreeds()
                }
            } catch (error: IOException) {
                setState { copy(error = BreedsListState.ListError.ListUpdateFailed(cause = error)) }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }
}