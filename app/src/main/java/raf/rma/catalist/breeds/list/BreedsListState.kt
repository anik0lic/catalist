package raf.rma.catalist.breeds.list

import raf.rma.catalist.breeds.domain.BreedsData

data class BreedsListState (
    val items: List<BreedsData> = emptyList()
)