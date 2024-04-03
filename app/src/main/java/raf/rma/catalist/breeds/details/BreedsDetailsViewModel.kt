package raf.rma.catalist.breeds.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import raf.rma.catalist.breeds.repository.BreedRepository

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
    }
}