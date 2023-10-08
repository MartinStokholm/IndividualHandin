package com.example.flowerapplication

data class Flower(
    val name: String,
    val placeOfOrigin: String,
    val colors: List<String> = emptyList(),
    val description: String = "",
)

