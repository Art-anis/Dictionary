package com.nerazim.synonyms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json

//экран, где выводится отдельное значение слова
@Composable
fun TermScreen(
    data: String //JSON-строка с данными
) {
    //преобразование JSON в объект значения
    val term = Json.decodeFromString<Term>(data)

    //переменные состояния - флаги для раскрытия соответствующих столбцов
    var examplesExpanded by remember {
        mutableStateOf(false)
    }
    var synonymsExpanded by remember {
        mutableStateOf(false)
    }
    var antonymsExpanded by remember {
        mutableStateOf(false)
    }

    Spacer(modifier = Modifier.height(128.dp))
    Column {
        //слово
        Text(term.word,
            style = MaterialTheme.typography.titleLarge)
        //определение
        Text(term.definition)
        //часть речи
        Text(term.partOfSpeech)
        //примеры, столбец при нажатии раскрывает/убирает список примеров
        Column(
            modifier = Modifier
                .clickable { examplesExpanded = !examplesExpanded }
                .border(BorderStroke(1.dp, Color.Black))
        ) {
            Text("Examples",
                style = MaterialTheme.typography.titleMedium)
            //если список не пуст, выводим его
            if (examplesExpanded && term.examples.isNotEmpty()) {
                LazyColumn {
                    items(term.examples) {
                        Text(it)
                    }
                }
            }
            //иначе выводим сообщение, что ничего нет
            else if (examplesExpanded) {
                Text("None")
            }
        }
        //синонимы, при нажатии раскрывает/убирает список синонимов
        Column(
            modifier = Modifier
                .clickable { synonymsExpanded = !synonymsExpanded }
                .border(BorderStroke(1.dp, Color.Black))
        ) {
            Text("Synonyms",
                style = MaterialTheme.typography.titleMedium)
            //если список не пуст, выводим его
            if (synonymsExpanded && term.synonyms.isNotEmpty()) {
                LazyColumn {
                    items(term.synonyms) {
                        Text(it)
                    }
                }
            }
            //иначе выводим сообщение, что ничего нет
            else if (synonymsExpanded) {
                Text("None")
            }
        }
        //антонимы, при нажатии раскрывает/убирает список антонимов
        Column(
            modifier = Modifier
                .clickable { antonymsExpanded = !antonymsExpanded }
                .border(BorderStroke(1.dp, Color.Black))
        ) {
            Text("Antonyms",
                style = MaterialTheme.typography.titleMedium)
            //если список не пуст, выводим его
            if (antonymsExpanded && term.antonyms.isNotEmpty()) {
                LazyColumn {
                    items(term.antonyms) {
                        Text(it)
                    }
                }
            }
            //иначе выводим сообщение, что ничего нет
            else if (antonymsExpanded) {
                Text("None")
            }
        }
    }
}