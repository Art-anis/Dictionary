Приложение-словарь, использует Synonyms API для получения данных. 
Пользователь на главном экране вводит слово:
```kotlin
...
//переменная состояния - слово, которое будем искать
var word by remember {
    mutableStateOf("")
}
...
//поле ввода
TextField(
    value = word,
    onValueChange = { word = it },
    leadingIcon = {
        Icon(imageVector = Icons.Default.Search, contentDescription = null)
    }
)
...
```
Затем нажимает на кнопку поиска, которая отправляет запрос к API:
```kotlin
...
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
          ...
```
...и перенаправляет пользователя на экран со всеми полученными значениями этого слова:
Файл StartScreen.kt:
```kotlin
//переходим к списку после того, как заберем строку из очереди
goToWord(word, queue.take())
```
Файл Navigation.kt:
```kotlin
...
//главный экран
composable(Route.Main.path) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        StartScreen(
            modifier = Modifier
                .align(Alignment.Center),
            goToWord = { word, wordData -> //переход к списку значений, передается само слово и все данные по нему в формате JSON
                navController.navigate(Route.Word.path + "/" + word + "/" + wordData)
            }
        )
    }
}
...
```
Файл WordScreen.kt:
```
...
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
...
```
При нажатии на какое-то значение переходим на экран с этим значением:
```
...
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
...
```

Приложение было создано для ознакомления работы со сторонним API.
