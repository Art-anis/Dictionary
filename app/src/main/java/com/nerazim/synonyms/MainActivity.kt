package com.nerazim.synonyms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nerazim.synonyms.ui.theme.SynonymsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SynonymsTheme {
                //скаффолд
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //нав хост внутри
                    Navigator(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}