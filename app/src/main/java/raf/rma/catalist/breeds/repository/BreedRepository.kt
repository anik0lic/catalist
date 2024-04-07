package raf.rma.catalist.breeds.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import raf.rma.catalist.breeds.domain.BreedsData
import kotlin.time.Duration.Companion.seconds

object BreedRepository {

    private val breeds = MutableStateFlow(listOf<BreedsData>())

    fun allBreeds(): List<BreedsData> = breeds.value

    suspend fun fetchBreeds(){
        delay(2.seconds)
    }

    suspend fun fetchBreedDetails(breedId : String){
        delay(1.seconds)
    }

    fun observeBreeds(): Flow<List<BreedsData>> = breeds.asStateFlow()

    fun observeBreedDetails(breedId: String): Flow<BreedsData?> {
        return observeBreeds()
            .map { breeds -> breeds.find { it.id == breedId } }
            .distinctUntilChanged()
    }

    fun getBreedById(id: String): BreedsData? {
        return breeds.value.find { it.id == id }
    }
}