package com.nerazim.synonyms

import android.util.Log
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nerazim.synonyms.ui.theme.SynonymsTheme
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.concurrent.LinkedBlockingQueue

//гланый экран
@Composable
fun StartScreen(
    modifier: Modifier = Modifier, //модификатор
    goToWord: (String, String) -> Unit //функция переходв к списку значений
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 5.dp)
    ) {
        //переменная состояния - слово, которое будем искать
        var word by remember {
            mutableStateOf("")
        }
        //текст-обращение
        Text(
            text = "Hello! This is the Synonym App. Type the word in, click 'Search' and you'll get it's definition, part of speech, " +
                    "synonyms and antonyms! Enjoy!",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        //поле ввода
        TextField(
            value = word,
            onValueChange = { word = it },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        //кнопка поиска
        Button(onClick = {
            //URL, по которому отправляем запрос
            val apiUrl = "https://www.stands4.com/services/v2/syno.php?uid=12733&tokenid=bHWvpdNe99sckn9g" +
                    "&word=" + word + "&format=json"
            //очередь для получения данных из потока
            val queue = LinkedBlockingQueue<String>()
            //запускаем поток
            Thread {
               try {
                   //устанавливаем соединение и отправляем GET-запрос
                   val response = StringBuilder()
                   val url: URL = URI.create(apiUrl).toURL()
                   val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                   connection.requestMethod = "GET"

                   val responseCode: Int = connection.responseCode

                   //если ответ ОК, то собираем ответ
                   if (responseCode == HttpURLConnection.HTTP_OK) {
                       val reader = BufferedReader(InputStreamReader(connection.inputStream))
                       var line: String?

                       while (reader.readLine().also { line = it } != null) {
                           //заменяем пустой объект на пустую строку (особенности API)
                           line = line!!.replace("{}", "\"\"")
                           response.append(line)
                       }
                       reader.close()
                   }
                   var result = response.toString()
                   //если значение всего одно, JSON не содержит массив, поэтому надо добавить скобки вручную
                   if (result.find { it == '[' } == null) {
                       result = result.replace("result\":{", "result\":[{").replace("}}", "}]}")
                   }
                   //добавляем результат в очередь
                   queue.add(result)
                   //отключаем соединение
                   connection.disconnect()
                }
               catch (e: Exception) {
                   e.printStackTrace()
               }
            }.start()

            //перехоим к списку после того, как заберем строку из очереди
            goToWord(word, queue.take())
        }) {
            Text(text = "Search!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    SynonymsTheme {
        StartScreen {a, b -> }
    }
}