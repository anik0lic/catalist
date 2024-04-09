package raf.rma.catalist.breeds.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import raf.rma.catalist.breeds.api.BreedsApi
import raf.rma.catalist.breeds.domain.BreedsData
import raf.rma.catalist.networking.retrofit
import kotlin.time.Duration.Companion.seconds

object BreedRepository {

//    private val breeds = MutableStateFlow(listOf<BreedsData>())
    private val breedsApi: BreedsApi = retrofit.create(BreedsApi::class.java)

//    fun allBreeds(): List<BreedsData> = breeds.value

    suspend fun fetchAllBreeds() = breedsApi.getAllBreeds()
//        delay(2.seconds)
//        breeds.update { SampleData.toMutableList() }


    suspend fun fetchBreedDetails(breedId : String)=
//        delay(1.seconds)
        breedsApi.getBreed(breedId = breedId)
//    }

//    fun observeBreeds(): Flow<List<BreedsData>> = breeds.asStateFlow()

//    fun observeBreedDetails(breedId: String): Flow<BreedsData?> {
//        return observeBreeds()
//            .map { breeds -> breeds.find { it.id == breedId } }
//            .distinctUntilChanged()
//    }

//    fun getBreedById(id: String): BreedsData? {
//        return breeds.value.find { it.id == id }
//    }
}