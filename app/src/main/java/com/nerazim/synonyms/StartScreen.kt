package com.nerazim.synonyms

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nerazim.synonyms.ui.theme.SynonymsTheme
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    goToWord: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 5.dp)
    ) {
        var word by remember {
            mutableStateOf("")
        }
        Text(
            text = "Hello! This is the Synonym App. Type the word in, click 'Search' and you'll get it's definition, part of speech, " +
                    "synonyms and antonyms! Enjoy!",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = word,
            onValueChange = { word = it },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            val apiUrl = "https://www.stands4.com/services/v2/syno.php?uid=12733&tokenid=bHWvpdNe99sckn9g" +
                    "&word=" + word + "&format=json"
            Thread {
                try {
                    val url: URL = URI.create(apiUrl).toURL()
                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

                    connection.requestMethod = "GET"

                    val responseCode: Int = connection.responseCode

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val reader = BufferedReader(InputStreamReader(connection.inputStream))
                        var line: String?
                        val response = StringBuilder()

                        while (reader.readLine().also { line = it } != null) {
                            line = line!!.replace("{}", "\"\"")
                            response.append(line)
                        }
                        reader.close()

                        val wordJson = Json.decodeFromString<Result>(response.toString())
                        val termList = mutableListOf<Term>()
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
                        val wordData = Word(termList)
                    }

                    connection.disconnect()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()
        }) {
            Text(text = "Search!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    SynonymsTheme {
        StartScreen {}
    }
}