package raf.rma.catalist.breeds.repository

import raf.rma.catalist.breeds.api.BreedsApi
import raf.rma.catalist.networking.retrofit

object BreedRepository {
    private val breedsApi: BreedsApi = retrofit.create(BreedsApi::class.java)

    suspend fun getAllBreeds() = breedsApi.getAllBreeds()
    suspend fun getBreedDetails(breedId : String) = breedsApi.getBreed(breedId = breedId)
    suspend fun getImage(imageId: String) = breedsApi.getImage(imageId = imageId)
}