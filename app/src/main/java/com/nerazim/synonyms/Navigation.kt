package com.nerazim.synonyms

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

//класс путей для навигации
sealed class Route(
    val path: String
) {
    data object Main: Route("Main")
    data object Word: Route("Word")
    data object Term: Route("Term")
}

@Composable
fun Navigator(modifier: Modifier = Modifier) {
    //нав контроллер
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Main.path //начинаем с главного экрана
    ) {
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

        //список значений
        composable(Route.Word.path + "/{word}/{wordData}") { backStackEntry ->
            //извлекаем аргументы
            val word = backStackEntry.arguments?.getString("word")
            val wordData = backStackEntry.arguments?.getString("wordData")
            //если что-то пошло не так, выводим тост и возвращаемся, откуда пришли
            if (word == null || wordData == null) {
                Toast.makeText(LocalContext.current, "Something went wrong!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
            //все ок, выводим экран
            else {
                WordScreen(
                    word = word,
                    info = wordData,
                    goToTerm = { term -> //переход к конкретному значению, передается значение в формате JSON
                        navController.navigate(Route.Term.path + "/" + term)
                    }
                )
            }
        }

        //отдельное значение
        composable(Route.Term.path + "/{data}") { backStackEntry ->
            //извлекаются аргументы
            val wordData = backStackEntry.arguments?.getString("data")
            //если что-то пошло не так, выводим тост и возвращаемся, откуда пришли
            if (wordData == null) {
                Toast.makeText(LocalContext.current, "Something went wrong!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
            //все ок, выводим экран
            else {
                TermScreen(
                    data = wordData
                )
            }
        }
    }
}