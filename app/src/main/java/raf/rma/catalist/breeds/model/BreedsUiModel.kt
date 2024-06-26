package raf.rma.catalist.breeds.model

data class BreedsUiModel (
    val id: String,
    val name: String,
    val alternativeName: String,
    val temperament: String,
    val origin: String,
    val description: String,
    val lifeSpan: String,
    val weight: String,
    val wikipediaURL: String,
    val rare: Int,
    val referenceImageId: String,

    val adaptability: Int,
    val affectionLevel: Int,
    val intelligence: Int,
    val socialNeeds: Int,
    val childFriendly: Int
)