package com.example.flowerapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FlowerViewModel : ViewModel() {
    private val flowerList = MutableLiveData<List<Flower>>()

    init {
        val flowers = listOf(
            Flower("Rose", "Asia", listOf("Red", "White", "Pink", "Yellow"), "The rose is a woody perennial flowering plant of the genus Rosa, in the family Rosaceae, or the flower it bears."),
            Flower("Daisy", "Europe", listOf("Red", "White", "Pink", "Yellow"), "Bellis perennis is a common European species of daisy, of the Asteraceae family, often considered the archetypal species of that name."),
            Flower("Lily", "Asia", listOf("Red", "White", "Pink", "Yellow"), "Lilium is a genus of herbaceous flowering plants growing from bulbs, all with large prominent flowers."),
            Flower("Tulip", "Asia", listOf("Red", "White", "Pink", "Yellow"), "The tulip is a Eurasian and North African genus of herbaceous, perennial, bulbous plants in the lily family, with showy flowers."),
            Flower("Orchid", "Asia", listOf("Red", "White", "Pink", "Yellow"), "The Orchidaceae are a diverse and widespread family of flowering plants, with blooms that are often colourful and fragrant, commonly known as the orchid family."),
            Flower("Sunflower", "Asia", listOf("Red", "White", "Pink", "Yellow"), "Helianthus is a genus comprising about 70 species of annual and perennial flowering plants in the daisy family Asteraceae."),
            Flower("Daffodil", "Asia", listOf("Red", "White", "Pink", "Yellow"), "Narcissus is a genus of predominantly spring flowering perennial plants of the amaryllis family, Amaryllidaceae."),
            Flower("Dahlia", "Asia", listOf("Red", "White", "Pink", "Yellow"), "Dahlia is a genus of bushy, tuberous, herbaceous perennial plants native to Mexico and Central America."),
            Flower("Cherry Blossom", "Asia", listOf("Red", "White", "Pink", "Yellow"), "A cherry blossom is a flower of many trees of genus Prunus. The most well-known species is the Japanese cherry, Prunus serrulata, which is commonly called sakura."),
        )
        flowerList.value = flowers
    }

    fun getFlowers(): LiveData<List<Flower>> {
        return flowerList
    }

    fun getFlowerByName(name: String): Flower? {
        flowerList.value?.let { flowers ->
            return flowers.first { it.name == name }
        }
        return null
    }
}

