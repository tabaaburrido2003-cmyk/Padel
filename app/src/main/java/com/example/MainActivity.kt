package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.PadelViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.VoltGreen

class MainActivity : ComponentActivity() {
    private val viewModel: PadelViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
                val isLocked by viewModel.isLocked.collectAsStateWithLifecycle()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (currentUser == null || isLocked) {
                        UnlockScreen(
                            viewModel = viewModel,
                            currentUser = currentUser,
                            onUnlocked = {
                                // Pin unlock successful! Screen switches automatically
                            }
                        )
                    } else {
                        // Main Application View
                        MainDashboardLayout(viewModel, currentUser!!)
                    }
                }
            }
        }
    }
}

@Composable
fun MainDashboardLayout(
    viewModel: PadelViewModel,
    currentUser: com.example.data.User
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars
            ) {
                // Tab 0: Espacio Libre (Match finder)
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.SportsTennis,
                            contentDescription = "Espacio Libre"
                        )
                    },
                    label = { Text("Espacio Libre") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = VoltGreen,
                        indicatorColor = VoltGreen,
                        unselectedIconColor = MaterialTheme.colorScheme.outline,
                        unselectedTextColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.testTag("tab_match_feed")
                )

                // Tab 1: Ranking Leaderboard
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Ranking"
                        )
                    },
                    label = { Text("Ranking") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = VoltGreen,
                        indicatorColor = VoltGreen,
                        unselectedIconColor = MaterialTheme.colorScheme.outline,
                        unselectedTextColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.testTag("tab_ranking")
                )

                // Tab 2: Profile Settings
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Mi Perfil"
                        )
                    },
                    label = { Text("Mi Perfil") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = VoltGreen,
                        indicatorColor = VoltGreen,
                        unselectedIconColor = MaterialTheme.colorScheme.outline,
                        unselectedTextColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.testTag("tab_profile")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> MatchFeedScreen(viewModel = viewModel, currentUser = currentUser)
                1 -> RankingScreen(viewModel = viewModel, currentUser = currentUser)
                2 -> ProfileScreen(viewModel = viewModel, currentUser = currentUser)
            }
        }
    }
}
