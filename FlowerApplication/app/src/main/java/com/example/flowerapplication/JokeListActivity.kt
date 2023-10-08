package com.example.flowerapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.example.flowerapplication.ui.theme.FlowerApplicationTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class JokeListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlowerApplicationTheme {
                JokeListScreen(
                    JokeViewModel(),
                    onActivityFinish = { finish() }
                )
            }
        }
    }
}

@Composable
fun JokeListScreen(
    jokeViewModel: JokeViewModel,
    onActivityFinish: () -> Unit = { }
) {
    var joke by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Joke of the Day",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.padding(8.dp))

        if (isLoading) {
            Text(text = "Loading...")
        } else {
            Text(
                text = joke,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Button(
            onClick = {
                // Fetch a new joke
                fetchJoke(jokeViewModel) { newJoke ->
                    joke = newJoke
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Update Joke")
        }

        // Add a button to finish the Activity
        Spacer(modifier = Modifier.padding(16.dp))
        Button(
            onClick = {
                onActivityFinish()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Finish")
        }
    }

    // Initialize with a joke when the Composable is first displayed
    DisposableEffect(Unit) {
        fetchJoke(jokeViewModel) { newJoke ->
            joke = newJoke
        }
        onDispose { }
    }
}

private fun fetchJoke(viewModel: JokeViewModel, onJokeFetched: (String) -> Unit) {
    viewModel.fetchJokeFromApi()
    viewModel.joke.observeForever { joke ->
        onJokeFetched(joke)
    }
}
