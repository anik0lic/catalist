package raf.rma.catalist.breeds.repository

import raf.rma.catalist.breeds.api.BreedsApi
import raf.rma.catalist.networking.retrofit

object BreedRepository {

//    private val breeds = MutableStateFlow(listOf<BreedsData>())
    private val breedsApi: BreedsApi = retrofit.create(BreedsApi::class.java)

//    fun allBreeds(): List<BreedsData> = breeds.value

    suspend fun getAllBreeds() = breedsApi.getAllBreeds()
    suspend fun getBreedDetails(breedId : String) = breedsApi.getBreed(breedId = breedId)
    suspend fun getImage(imageId: String) = breedsApi.getImage(imageId = imageId)

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