package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

enum class LogCategory {
    DAILY, CRICKET, FITNESS, MATCH, VIDEO
}

@Composable
fun LoggingScreen(
    dailyLogs: List<DailyLog>,
    cricketLogs: List<CricketLog>,
    fitnessLogs: List<FitnessLog>,
    matchLogs: List<MatchLog>,
    videoLogs: List<VideoLog>,
    onSaveDaily: (String, Float, Int, String, Float, String, Int, Int, Int, Int) -> Unit,
    onSaveCricket: (Int, String, Int, String, Int, String, String, String, String) -> Unit,
    onSaveFitness: (String, Int, Float, Int, Int, String, String) -> Unit,
    onSaveMatch: (String, String, String, Int, Int, Int, Float, Int, String, String) -> Unit,
    onSaveVideo: (String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(LogCategory.DAILY) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- Category Selection Selector Chips ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 12.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            LogCategory.values().forEach { cat ->
                val isSelected = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { selectedCategory = cat }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (cat) {
                            LogCategory.DAILY -> "Daily"
                            LogCategory.CRICKET -> "Practice"
                            LogCategory.FITNESS -> "Gym"
                            LogCategory.MATCH -> "Match"
                            LogCategory.VIDEO -> "Video"
                        },
                        color = if (isSelected) OnAthleticBlack else TextMutedGray,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // --- Logging Form ---
            item {
                Text(
                    text = when (selectedCategory) {
                        LogCategory.DAILY -> "Log Daily Readiness & Health"
                        LogCategory.CRICKET -> "Log Cricket Practice Session"
                        LogCategory.FITNESS -> "Log Gym, Running & Recovery"
                        LogCategory.MATCH -> "Log Cricket Match Statistics"
                        LogCategory.VIDEO -> "Log Technical Video Analysis"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Logs are cataloged under today's date for precise AI analytics.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMutedGray),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            item {
                when (selectedCategory) {
                    LogCategory.DAILY -> DailyForm(onSaveDaily)
                    LogCategory.CRICKET -> CricketForm(onSaveCricket)
                    LogCategory.FITNESS -> FitnessForm(onSaveFitness)
                    LogCategory.MATCH -> MatchForm(onSaveMatch)
                    LogCategory.VIDEO -> VideoForm(onSaveVideo)
                }
            }

            // --- Historical Logs List Section ---
            item {
                Text(
                    text = "Logged History (${when (selectedCategory) {
                        LogCategory.DAILY -> "Daily logs"
                        LogCategory.CRICKET -> "Cricket sessions"
                        LogCategory.FITNESS -> "Workouts"
                        LogCategory.MATCH -> "Match stats"
                        LogCategory.VIDEO -> "Video files"
                    }})",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            when (selectedCategory) {
                LogCategory.DAILY -> {
                    if (dailyLogs.isEmpty()) {
                        item { EmptyHistoryLabel() }
                    } else {
                        items(dailyLogs) { log ->
                            HistoryCard(title = "Health Log: ${log.date}") {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("• Wake Time: ${log.wakeTime} | Sleep: ${log.sleepDuration} hrs (Quality: ${log.sleepQuality}/5)", color = TextCrispWhite, fontSize = 13.sp)
                                    Text("• Energy: ${log.energy}/5 | Stress: ${log.stress}/5 | Recovery: ${log.recovery}/5", color = TextCrispWhite, fontSize = 13.sp)
                                    Text("• Hydration: ${log.waterIntake} Liters | Supplements: ${log.supplements}", color = TextCrispWhite, fontSize = 13.sp)
                                    if (log.meals.isNotBlank()) {
                                        Text("• Nutrition: ${log.meals}", color = TextMutedGray, fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }
                }
                LogCategory.CRICKET -> {
                    if (cricketLogs.isEmpty()) {
                        item { EmptyHistoryLabel() }
                    } else {
                        items(cricketLogs) { log ->
                            HistoryCard(title = "Skills Log: ${log.date}") {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("• Batting Practice: ${log.battingDuration} mins (Focus: ${log.battingFocus})", color = TextCrispWhite, fontSize = 13.sp)
                                    Text("• Bowling Workload: ${log.bowlingWorkload} overs (Focus: ${log.bowlingFocus})", color = TextCrispWhite, fontSize = 13.sp)
                                    Text("• Fielding Practice: ${log.fieldingDuration} mins (Focus: ${log.fieldingFocus})", color = TextCrispWhite, fontSize = 13.sp)
                                    if (log.coachFeedback.isNotBlank()) {
                                        Text("• Coach Feedback: ${log.coachFeedback}", color = MaterialTheme.colorScheme.secondary, fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }
                }
                LogCategory.FITNESS -> {
                    if (fitnessLogs.isEmpty()) {
                        item { EmptyHistoryLabel() }
                    } else {
                        items(fitnessLogs) { log ->
                            HistoryCard(title = "Gym Log: ${log.date}") {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    if (log.gymSession.isNotBlank()) {
                                        Text("• Gym: ${log.gymSession} (${log.gymDuration} mins)", color = TextCrispWhite, fontSize = 13.sp)
                                    }
                                    if (log.runningDistance > 0) {
                                        Text("• Running Cardio: ${log.runningDistance} km in ${log.runningDuration} mins", color = TextCrispWhite, fontSize = 13.sp)
                                    }
                                    Text("• Mobility Focus: ${log.mobilityDuration} mins | Active Recovery: ${log.recoveryActivity}", color = TextCrispWhite, fontSize = 13.sp)
                                    if (log.injuries.isNotBlank()) {
                                        Text("• Strain/Injury Alert: ${log.injuries}", color = CoralAlert, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }
                        }
                    }
                }
                LogCategory.MATCH -> {
                    if (matchLogs.isEmpty()) {
                        item { EmptyHistoryLabel() }
                    } else {
                        items(matchLogs) { log ->
                            HistoryCard(title = "${log.matchName} (${log.date})") {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("• Format: ${log.format} | Played as: ${log.role} | Result: ${log.matchResult}", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text("• Batting stats: ${log.runsScored} runs off ${log.ballsFaced} balls", color = TextCrispWhite, fontSize = 13.sp)
                                    Text("• Bowling stats: ${log.wicketsTaken} wkts, conceding ${log.runsConceded} runs in ${log.oversBowled} overs", color = TextCrispWhite, fontSize = 13.sp)
                                    if (log.fieldingPerformance.isNotBlank()) {
                                        Text("• Fielding actions: ${log.fieldingPerformance}", color = TextMutedGray, fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }
                }
                LogCategory.VIDEO -> {
                    if (videoLogs.isEmpty()) {
                        item { EmptyHistoryLabel() }
                    } else {
                        items(videoLogs) { log ->
                            HistoryCard(title = "Video Clip: ${log.title} (${log.date})") {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("• Video Filename: ${log.videoUri}", color = MaterialTheme.colorScheme.secondary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                    Text("• Frame-by-Frame Technique Notes:", color = TextCrispWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(log.analysisNotes, color = TextCrispWhite, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyHistoryLabel() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("No logs recorded for this category.", color = TextMutedGray, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun HistoryCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextCrispWhite))
            content()
        }
    }
}

// ==========================================
// FORM COMPOSABLES (DAILY, CRICKET, FITNESS...)
// ==========================================

@Composable
fun DailyForm(onSave: (String, Float, Int, String, Float, String, Int, Int, Int, Int) -> Unit) {
    var wakeTime by remember { mutableStateOf("06:30") }
    var sleepDuration by remember { mutableStateOf("7.5") }
    var sleepQuality by remember { mutableStateOf(4f) }
    var meals by remember { mutableStateOf("") }
    var waterIntake by remember { mutableStateOf("3.0") }
    var supplements by remember { mutableStateOf("") }
    var mood by remember { mutableStateOf(4f) }
    var stress by remember { mutableStateOf(2f) }
    var energy by remember { mutableStateOf(4f) }
    var recovery by remember { mutableStateOf(4f) }

    Card(
        modifier = Modifier.fillMaxWidth().testTag("daily_log_form"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = wakeTime,
                    onValueChange = { wakeTime = it },
                    label = { Text("Wake Time") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                OutlinedTextField(
                    value = sleepDuration,
                    onValueChange = { sleepDuration = it },
                    label = { Text("Sleep Hrs") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }

            Text("Sleep Quality: ${sleepQuality.toInt()}/5", style = MaterialTheme.typography.bodySmall)
            Slider(value = sleepQuality, onValueChange = { sleepQuality = it }, valueRange = 1f..5f, steps = 3)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = waterIntake,
                    onValueChange = { waterIntake = it },
                    label = { Text("Water (Liters)") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                OutlinedTextField(
                    value = supplements,
                    onValueChange = { supplements = it },
                    label = { Text("Supplements Taken") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            OutlinedTextField(
                value = meals,
                onValueChange = { meals = it },
                label = { Text("Nutrition (Meals description)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Text("Energy Level: ${energy.toInt()}/5", style = MaterialTheme.typography.bodySmall)
            Slider(value = energy, onValueChange = { energy = it }, valueRange = 1f..5f, steps = 3)

            Text("Stress Level: ${stress.toInt()}/5", style = MaterialTheme.typography.bodySmall)
            Slider(value = stress, onValueChange = { stress = it }, valueRange = 1f..5f, steps = 3)

            Text("Recovery Level: ${recovery.toInt()}/5", style = MaterialTheme.typography.bodySmall)
            Slider(value = recovery, onValueChange = { recovery = it }, valueRange = 1f..5f, steps = 3)

            Button(
                onClick = {
                    val sleepDur = sleepDuration.toFloatOrNull() ?: 8f
                    val water = waterIntake.toFloatOrNull() ?: 3f
                    onSave(
                        wakeTime, sleepDur, sleepQuality.toInt(), meals, water, supplements,
                        mood.toInt(), stress.toInt(), energy.toInt(), recovery.toInt()
                    )
                },
                modifier = Modifier.fillMaxWidth().testTag("save_daily_log_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save Daily Log", fontWeight = FontWeight.Bold, color = OnAthleticBlack)
            }
        }
    }
}

@Composable
fun CricketForm(onSave: (Int, String, Int, String, Int, String, String, String, String) -> Unit) {
    var battingDuration by remember { mutableStateOf("45") }
    var battingFocus by remember { mutableStateOf("") }
    var bowlingWorkload by remember { mutableStateOf("6") }
    var bowlingFocus by remember { mutableStateOf("") }
    var fieldingDuration by remember { mutableStateOf("15") }
    var fieldingFocus by remember { mutableStateOf("") }
    var coachFeedback by remember { mutableStateOf("") }
    var strengths by remember { mutableStateOf("") }
    var weaknesses by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = battingDuration,
                    onValueChange = { battingDuration = it },
                    label = { Text("Batting Mins") },
                    modifier = Modifier.weight(1.0f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = bowlingWorkload,
                    onValueChange = { bowlingWorkload = it },
                    label = { Text("Bowling Overs") },
                    modifier = Modifier.weight(1.0f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            OutlinedTextField(
                value = battingFocus,
                onValueChange = { battingFocus = it },
                label = { Text("Batting Drills / Technical Focus") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = bowlingFocus,
                onValueChange = { bowlingFocus = it },
                label = { Text("Bowling Targets (e.g. outswing yorkers)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = fieldingDuration,
                    onValueChange = { fieldingDuration = it },
                    label = { Text("Fielding Mins") },
                    modifier = Modifier.weight(1.0f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = fieldingFocus,
                    onValueChange = { fieldingFocus = it },
                    label = { Text("Fielding Drills") },
                    modifier = Modifier.weight(1.5f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            OutlinedTextField(
                value = coachFeedback,
                onValueChange = { coachFeedback = it },
                label = { Text("Coach Feedback") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = strengths,
                onValueChange = { strengths = it },
                label = { Text("Key Strengths Today") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = weaknesses,
                onValueChange = { weaknesses = it },
                label = { Text("Identified Technique Errors") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = {
                    val batDur = battingDuration.toIntOrNull() ?: 0
                    val bowlOvers = bowlingWorkload.toIntOrNull() ?: 0
                    val fieldDur = fieldingDuration.toIntOrNull() ?: 0
                    onSave(batDur, battingFocus, bowlOvers, bowlingFocus, fieldDur, fieldingFocus, coachFeedback, strengths, weaknesses)
                },
                modifier = Modifier.fillMaxWidth().testTag("save_cricket_log_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save Practice Log", fontWeight = FontWeight.Bold, color = OnAthleticBlack)
            }
        }
    }
}

@Composable
fun FitnessForm(onSave: (String, Int, Float, Int, Int, String, String) -> Unit) {
    var gymSession by remember { mutableStateOf("") }
    var gymDuration by remember { mutableStateOf("60") }
    var runningDistance by remember { mutableStateOf("0") }
    var runningDuration by remember { mutableStateOf("0") }
    var mobilityDuration by remember { mutableStateOf("15") }
    var recoveryActivity by remember { mutableStateOf("") }
    var injuries by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = gymSession,
                onValueChange = { gymSession = it },
                label = { Text("Gym Focus (e.g. Leg Day Squats)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = gymDuration,
                    onValueChange = { gymDuration = it },
                    label = { Text("Gym Mins") },
                    modifier = Modifier.weight(1.0f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = mobilityDuration,
                    onValueChange = { mobilityDuration = it },
                    label = { Text("Mobility Mins") },
                    modifier = Modifier.weight(1.0f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = runningDistance,
                    onValueChange = { runningDistance = it },
                    label = { Text("Running (Km)") },
                    modifier = Modifier.weight(1.0f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                OutlinedTextField(
                    value = runningDuration,
                    onValueChange = { runningDuration = it },
                    label = { Text("Run Mins") },
                    modifier = Modifier.weight(1.0f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            OutlinedTextField(
                value = recoveryActivity,
                onValueChange = { recoveryActivity = it },
                label = { Text("Recovery Focus (e.g. Ice bath, stretch)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = injuries,
                onValueChange = { injuries = it },
                label = { Text("Hamstring/Pain points/Injury status") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CoralAlert,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Button(
                onClick = {
                    val gymDur = gymDuration.toIntOrNull() ?: 0
                    val runDist = runningDistance.toFloatOrNull() ?: 0f
                    val runDur = runningDuration.toIntOrNull() ?: 0
                    val mobDur = mobilityDuration.toIntOrNull() ?: 0
                    onSave(gymSession, gymDur, runDist, runDur, mobDur, recoveryActivity, injuries)
                },
                modifier = Modifier.fillMaxWidth().testTag("save_fitness_log_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save Workout Log", fontWeight = FontWeight.Bold, color = OnAthleticBlack)
            }
        }
    }
}

@Composable
fun MatchForm(onSave: (String, String, String, Int, Int, Int, Float, Int, String, String) -> Unit) {
    var matchName by remember { mutableStateOf("") }
    var format by remember { mutableStateOf("T20") }
    var role by remember { mutableStateOf("All-rounder") }
    var runsScored by remember { mutableStateOf("0") }
    var ballsFaced by remember { mutableStateOf("0") }
    var wicketsTaken by remember { mutableStateOf("0") }
    var oversBowled by remember { mutableStateOf("0.0") }
    var runsConceded by remember { mutableStateOf("0") }
    var fieldingPerformance by remember { mutableStateOf("") }
    var matchResult by remember { mutableStateOf("Win") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = matchName,
                onValueChange = { matchName = it },
                label = { Text("Match Title (e.g. Semifinal Cup)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = format,
                    onValueChange = { format = it },
                    label = { Text("Match Format (T20/ODI)") },
                    modifier = Modifier.weight(1.0f),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("Your Role (All-Rounder)") },
                    modifier = Modifier.weight(1.0f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = runsScored,
                    onValueChange = { runsScored = it },
                    label = { Text("Runs Scored") },
                    modifier = Modifier.weight(1.0f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = ballsFaced,
                    onValueChange = { ballsFaced = it },
                    label = { Text("Balls Faced") },
                    modifier = Modifier.weight(1.0f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = wicketsTaken,
                    onValueChange = { wicketsTaken = it },
                    label = { Text("Wickets Taken") },
                    modifier = Modifier.weight(1.0f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = oversBowled,
                    onValueChange = { oversBowled = it },
                    label = { Text("Overs Bowled") },
                    modifier = Modifier.weight(1.0f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                OutlinedTextField(
                    value = runsConceded,
                    onValueChange = { runsConceded = it },
                    label = { Text("Runs Conceded") },
                    modifier = Modifier.weight(1.0f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            OutlinedTextField(
                value = fieldingPerformance,
                onValueChange = { fieldingPerformance = it },
                label = { Text("Fielding Highlights (catches, direct hit runout)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = matchResult,
                onValueChange = { matchResult = it },
                label = { Text("Match Result (e.g., Win by 12 runs)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = {
                    val runs = runsScored.toIntOrNull() ?: 0
                    val balls = ballsFaced.toIntOrNull() ?: 0
                    val wkts = wicketsTaken.toIntOrNull() ?: 0
                    val overs = oversBowled.toFloatOrNull() ?: 0f
                    val runsCon = runsConceded.toIntOrNull() ?: 0
                    onSave(matchName, format, role, runs, balls, wkts, overs, runsCon, fieldingPerformance, matchResult)
                },
                modifier = Modifier.fillMaxWidth().testTag("save_match_log_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save Match Stats", fontWeight = FontWeight.Bold, color = OnAthleticBlack)
            }
        }
    }
}

@Composable
fun VideoForm(onSave: (String, String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var videoUri by remember { mutableStateOf("") }
    var analysisNotes by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Session Clip Title (e.g. Defense vs Spin)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = videoUri,
                onValueChange = { videoUri = it },
                label = { Text("Video File Name / Reference (e.g. net_swing.mp4)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = analysisNotes,
                onValueChange = { analysisNotes = it },
                label = { Text("Frame-by-Frame Technique Notes (e.g., front leg falls away)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 3
            )

            Button(
                onClick = {
                    onSave(title, videoUri, analysisNotes)
                },
                modifier = Modifier.fillMaxWidth().testTag("save_video_log_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save Video Analysis", fontWeight = FontWeight.Bold, color = OnAthleticBlack)
            }
        }
    }
}
