package com.example.flowerapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flowerapplication.ui.theme.FlowerApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlowerApplicationTheme {

                val scope = rememberCoroutineScope()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val navController = rememberNavController()
                val flowerViewModel = FlowerViewModel()

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = Color.LightGray
                            )
                            {
                                Column(modifier = Modifier.padding(16.dp)) {

                                    Text(
                                        text = "Flowers",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                            .clickable {
                                                navController.navigate("flowerNamesList") {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                    )


                                    // Jokes link
                                    ClickableText(
                                        text = AnnotatedString("Click here for jokes"),
                                        onClick = { _ ->
                                            startActivity(Intent(this@MainActivity, JokeListActivity::class.java))
                                        },
                                        modifier = Modifier.padding(16.dp)
                                    )

                                    // Close navigation drawer
                                    Button(
                                        modifier = Modifier.padding(top = 16.dp),
                                        onClick = {
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        },
                                    ) {
                                        Text("Close")
                                    }
                                }
                            }
                        }
                    },
                    content = {
                        Scaffold(
                            topBar =
                            {
                                MyAppBar(title = "Menu", onMenu = {
                                    if (drawerState.isOpen)
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    else
                                        scope.launch {
                                            drawerState.open()
                                        }
                                })
                            },
                            content =
                            { padding ->
                                Column(
                                    modifier = Modifier.padding(padding)

                                ) {
                                    NavHost(navController, startDestination = "flowerList") {
                                        composable("flowerList") {
                                            FlowerList(navController, flowerViewModel)
                                        }
                                        composable("flowerNamesList") {
                                            FlowersNamesList(navController, flowerViewModel)
                                        }
                                        composable("flowerItem/details/{flowerName}") { backStackEntry ->
                                            val flowerName = backStackEntry.arguments?.getString("flowerName") ?: ""
                                            FlowerDetailItem(flowerName, navController, flowerViewModel)
                                        }
                                    }
                                }
                            })
                    }
                )
            }
        }
    }
}

@Composable
fun FlowersNamesList(
    navController: NavHostController,
    flowerViewModel: FlowerViewModel,
) {
    val flowers: List<Flower> by flowerViewModel.getFlowers().observeAsState(emptyList())
    LazyColumn {
        items(flowers) { flower ->
            Text(
                text = flower.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        navController.navigate("flowerItem/details/${flower.name}") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
            )
            Divider(color = Color.Gray, thickness = 1.dp)
        }
    }
}

@Composable
fun FlowerList(navController: NavHostController, viewModel: FlowerViewModel) {
    val flowers: List<Flower> by viewModel.getFlowers().observeAsState(emptyList())

    LazyColumn {
        items(flowers) { flower ->
            FlowerItem(flower = flower, navController = navController)
        }
    }
}

@Composable
fun FlowerItem(flower: Flower, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                navController.navigate("flowerItem/details/${flower.name}") {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
    ) {
        Text(
            text = "${flower.name}",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold)
        )
        Text(text = "Place of Origin: ${flower.placeOfOrigin}")
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.Gray, thickness = 1.dp)
    }
}


@Composable
fun FlowerDetailItem(
    flowerName: String,
    navController: NavHostController,
    viewModel: FlowerViewModel
) {

    val flower: Flower? = viewModel.getFlowerByName(flowerName)

    if (flower == null) {
        Text("Flower not found!")
        // Back button
        Button(
            onClick = {
                // Navigate back to the previous screen
                navController.navigateUp()
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Back")
        }
    }
    else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Name: ${flower.name}",
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
            Text(text = "Place of Origin: ${flower.placeOfOrigin}")
            Spacer(modifier = Modifier.height(8.dp))

            // Display Colors
            Text(
                text = "Colors:",
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
            flower.colors.forEach { color ->
                Text(text = color)
            }

            // Display Description
            Text(
                text = "Description:",
                style = TextStyle(fontWeight = FontWeight.Bold)
            )

            Text(text = flower.description)

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.Gray, thickness = 1.dp)

            // Back button
            Button(
                onClick = {
                    // Navigate back to the previous screen
                    navController.navigateUp()
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Back")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppBar(title: String, onMenu: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = title)
        },colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
        ),
        navigationIcon = {
            IconButton(onClick = onMenu) {
                Icon(Icons.Filled.Menu, "menu")
            }
        }
    )
}
