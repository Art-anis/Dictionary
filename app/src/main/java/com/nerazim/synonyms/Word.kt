package com.nerazim.synonyms

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//результат, который приходит из API
@Serializable
data class Result(
    @SerialName("result")
    val terms: List<SerializedTerm>
)

//вложенный объект значения, который приходит мз API
@Serializable
data class SerializedTerm(
    @SerialName("term")
    val word: String = "",
    @SerialName("definition")
    val definition: String = "",
    @SerialName("example")
    val example: String = "",
    @SerialName("partofspeech")
    val partOfSpeech: String = "",
    @SerialName("synonyms")
    val synonyms: String = "",
    @SerialName("antonyms")
    val antonyms: String = ""
)

//готовый объект слова
data class Word(
    val terms: List<Term>
)

//преобразованный объект значения, сериализуется для передачи между экранами
@Serializable
data class Term(
    val word: String,
    val definition: String,
    val examples: List<String>,
    val partOfSpeech: String,
    val synonyms: List<String>,
    val antonyms: List<String>
)

