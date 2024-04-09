package raf.rma.catalist.breeds.model

data class BreedsUiModel (
    val id: String,
    val name: String,
    val alternativeName: String,
    val temperament: String,
    val origin: String,
    val description: String,
    val lifeSpan: String,
    val imageUrl: String,
    val weight: String,
    val wikipediaURL: String,
    val rare: Int,

    val adaptability: Int,
    val affectionLevel: Int,
)