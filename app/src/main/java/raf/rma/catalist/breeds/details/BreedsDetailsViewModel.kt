package raf.rma.catalist.breeds.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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


//    private val events = MutableSharedFlow<PasswordDetailsUiEvent>()
//    fun setEvent(event: PasswordDetailsUiEvent) {
//        viewModelScope.launch {
//            events.emit(event)
//        }
//    }

    init {
        observeBreedDetails()
        fetchBreedDetails()
    }

    private fun observeBreedDetails(){
        viewModelScope.launch {
            repository.observeBreedDetails(breedId = breedId)
                .filterNotNull()
                .collect {
                    setState { copy(data = it) }
                }
        }
    }

    private fun fetchBreedDetails(){
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                withContext(Dispatchers.IO) {
                    repository.fetchBreedDetails(breedId = breedId)
                }
            } catch (error: IOException) {
                setState {
                    copy(error = BreedsDetailsState.DetailsError.DataUpdateFailed(cause = error))
                }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }
}