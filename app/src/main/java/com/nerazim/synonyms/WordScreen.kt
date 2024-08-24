package com.nerazim.synonyms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nerazim.synonyms.ui.theme.SynonymsTheme
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject

//экран для отображения списка значений слова
@Composable
fun WordScreen(
    word: String, //само слово
    info: String, //JSON-строка с данными
    goToTerm: (String) -> Unit //функция перехода к экрану значения
) {
    val wordJson = Json.decodeFromString<Result>(info) //преобразовываем в предварительный объект
    val termList = mutableListOf<Term>() //список значений
    //собираем значения в список, попутно разбивая строки примеров, синонимов и антонимов в списки
    for (term in wordJson.terms) {
        termList.add(Term(
            word = word,
            definition = term.definition,
            examples = term.example.split(";").filter { it != "" },
            partOfSpeech = term.partOfSpeech,
            synonyms = term.synonyms.split(", ").filter { it != "" },
            antonyms = term.antonyms.split(", ").filter { it != "" }
        ))
    }
    //готовый объект слова
    val wordData = Word(termList)
    Spacer(modifier = Modifier.height(128.dp))
    //столбец, в котором выводятся все значения
    LazyColumn {
        items(wordData.terms) {
            TermItem(
                word = it.word,
                definition = it.definition,
                onClick = {
                    val data = Json.encodeToString<Term>(it)
                    goToTerm(data)
                }
            )
        }
    }
}

//элемент списка
@Composable
fun TermItem(
    word: String, //слово
    definition: String, //определение
    onClick: () -> Unit //функция перехода при нажатии на элемент
) {
    //кликабельный весь элемент
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        //текст со словом
        Text(
            modifier = Modifier,
            text = word,
            style = MaterialTheme.typography.titleLarge
                .merge(TextStyle(fontWeight = FontWeight.Bold))
        )
        //текст с определением
        Text(
            modifier = Modifier,
            overflow = TextOverflow.Ellipsis,
            text = definition,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TermItemPreview() {
    SynonymsTheme {
        TermItem(word = "word", definition = "something something idk") {

        }
    }
}