package com.nerazim.synonyms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class Route(
    val path: String
) {
    data object Main: Route("Main")
    data object Word: Route("Word")
}

@Composable
fun Navigator(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = Route.Main.path) {
        composable(Route.Main.path) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier.fillMaxSize()
            ) {
                StartScreen(modifier = Modifier
                    .align(Alignment.Center),
                    goToWord = {}
                )
            }
        }
    }
}