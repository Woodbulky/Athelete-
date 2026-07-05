package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AICoachScreen
import com.example.ui.components.DashboardScreen
import com.example.ui.components.LoggingScreen
import com.example.ui.theme.*
import com.example.ui.viewmodel.AthleteViewModel

enum class Screen {
    DASHBOARD, LOGGING, COACH
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: AthleteViewModel = viewModel()
                var currentScreen by remember { mutableStateOf(Screen.DASHBOARD) }
                val context = LocalContext.current

                // Collect flows with lifecycle safety
                val dailyLogs by viewModel.dailyLogs.collectAsStateWithLifecycle()
                val cricketLogs by viewModel.cricketLogs.collectAsStateWithLifecycle()
                val fitnessLogs by viewModel.fitnessLogs.collectAsStateWithLifecycle()
                val matchLogs by viewModel.matchLogs.collectAsStateWithLifecycle()
                val videoLogs by viewModel.videoLogs.collectAsStateWithLifecycle()
                val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
                val isAiLoading by viewModel.isAiLoading.collectAsStateWithLifecycle()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "ATHLETE OS",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.SansSerif,
                                        letterSpacing = 2.sp
                                    )
                                )
                            },
                            actions = {
                                // Simple active profile indicator
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(end = 16.dp)
                                ) {
                                    Text(
                                        text = "LIVE",
                                        color = OnAthleticBlack,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 8.dp,
                            modifier = Modifier.testTag("bottom_nav_bar")
                        ) {
                            NavigationBarItem(
                                selected = currentScreen == Screen.DASHBOARD,
                                onClick = { currentScreen = Screen.DASHBOARD },
                                icon = {
                                    Icon(
                                        imageVector = if (currentScreen == Screen.DASHBOARD) Icons.Filled.Dashboard else Icons.Outlined.Dashboard,
                                        contentDescription = "Dashboard"
                                    )
                                },
                                label = { Text("Dashboard") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = OnAthleticBlack,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = TextMutedGray,
                                    unselectedTextColor = TextMutedGray
                                ),
                                modifier = Modifier.testTag("nav_dashboard")
                            )

                            NavigationBarItem(
                                selected = currentScreen == Screen.LOGGING,
                                onClick = { currentScreen = Screen.LOGGING },
                                icon = {
                                    Icon(
                                        imageVector = if (currentScreen == Screen.LOGGING) Icons.Filled.EditNote else Icons.Outlined.EditNote,
                                        contentDescription = "Logs"
                                    )
                                },
                                label = { Text("Log Tracker") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = OnAthleticBlack,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = TextMutedGray,
                                    unselectedTextColor = TextMutedGray
                                ),
                                modifier = Modifier.testTag("nav_logs")
                            )

                            NavigationBarItem(
                                selected = currentScreen == Screen.COACH,
                                onClick = { currentScreen = Screen.COACH },
                                icon = {
                                    Icon(
                                        imageVector = if (currentScreen == Screen.COACH) Icons.Filled.SmartToy else Icons.Outlined.SmartToy,
                                        contentDescription = "AI Coach"
                                    )
                                },
                                label = { Text("AI Coach") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = OnAthleticBlack,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = TextMutedGray,
                                    unselectedTextColor = TextMutedGray
                                ),
                                modifier = Modifier.testTag("nav_coach")
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentScreen) {
                            Screen.DASHBOARD -> {
                                DashboardScreen(
                                    dailyLogs = dailyLogs,
                                    cricketLogs = cricketLogs,
                                    fitnessLogs = fitnessLogs,
                                    matchLogs = matchLogs,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Screen.LOGGING -> {
                                LoggingScreen(
                                    dailyLogs = dailyLogs,
                                    cricketLogs = cricketLogs,
                                    fitnessLogs = fitnessLogs,
                                    matchLogs = matchLogs,
                                    videoLogs = videoLogs,
                                    onSaveDaily = { wakeTime, sleepDur, quality, meals, water, supplements, mood, stress, energy, rec ->
                                        viewModel.saveDailyLog(wakeTime, sleepDur, quality, meals, water, supplements, mood, stress, energy, rec) {
                                            Toast.makeText(context, "Daily Health Log Saved!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    onSaveCricket = { batDur, batFocus, bowlOvers, bowlFocus, fieldDur, fieldFocus, feedback, strengths, weaknesses ->
                                        viewModel.saveCricketLog(batDur, batFocus, bowlOvers, bowlFocus, fieldDur, fieldFocus, feedback, strengths, weaknesses) {
                                            Toast.makeText(context, "Cricket Practice Log Saved!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    onSaveFitness = { session, dur, dist, runDur, mobDur, recAct, injuries ->
                                        viewModel.saveFitnessLog(session, dur, dist, runDur, mobDur, recAct, injuries) {
                                            Toast.makeText(context, "Workout Log Saved!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    onSaveMatch = { matchName, format, role, runs, balls, wkts, overs, runsCon, fielding, result ->
                                        viewModel.saveMatchLog(matchName, format, role, runs, balls, wkts, overs, runsCon, fielding, result) {
                                            Toast.makeText(context, "Match Stats Log Saved!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    onSaveVideo = { title, uri, notes ->
                                        viewModel.saveVideoLog(title, uri, notes) {
                                            Toast.makeText(context, "Technique Video Saved!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Screen.COACH -> {
                                AICoachScreen(
                                    chatMessages = chatMessages,
                                    isAiLoading = isAiLoading,
                                    onSendMessage = { question ->
                                        viewModel.askAiCoach(question)
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
