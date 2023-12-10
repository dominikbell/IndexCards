package com.example.indexcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.indexcards.data.AppDatabase
import com.example.indexcards.data.Box
import com.example.indexcards.ui.theme.IndexCardsTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        GlobalScope.launch {
//            AppDatabase.getDatabase(applicationContext)
//        }
        val dao = AppDatabase.getInstance(applicationContext).dao
        val box1 = Box(1, "Test2", "_ar", "Arabic")

//        lifecycleScope.launch {
//            dao.upsertBox(box1)
//        }

        setContent {
            IndexCardsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

