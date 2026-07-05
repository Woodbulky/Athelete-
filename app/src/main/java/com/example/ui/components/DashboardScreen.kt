package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    dailyLogs: List<DailyLog>,
    cricketLogs: List<CricketLog>,
    fitnessLogs: List<FitnessLog>,
    matchLogs: List<MatchLog>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
    ) {
        // --- 1. ATHLETE PROFILE CARD ---
        item {
            ProfileCard()
        }

        // --- 2. WEEKLY READINESS METRICS ---
        item {
            Text(
                text = "Athlete Readiness Index",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            ReadinessMetricsGrid(dailyLogs)
        }

        // --- 3. TRAINING & WORKLOAD VOLUME ---
        item {
            Text(
                text = "Weekly Workload Volume",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            WorkloadCard(cricketLogs, fitnessLogs)
        }

        // --- 4. RECENT MATCH SCORES ---
        item {
            Text(
                text = "Recent Cricket Match Logs",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (matchLogs.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SportsCricket,
                            contentDescription = "No matches",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No matches played or logged yet.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        } else {
            items(matchLogs.take(3)) { match ->
                MatchLogItem(match)
            }
        }
    }
}

@Composable
fun ProfileCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("athlete_profile_card"),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Avatar placeholder with sporty neon circle
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(32.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.SportsCricket,
                        contentDescription = "Avatar",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Column {
                    Text(
                        text = "HARSH KASLIWAL",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.SansSerif,
                            letterSpacing = 1.5.sp
                        )
                    )
                    Text(
                        text = "All-Rounder • Right-Hand Bat & Right-Arm Fast",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProfileStatItem(label = "Primary Sport", value = "Cricket")
                ProfileStatItem(label = "Level", value = "Elite Club")
                ProfileStatItem(label = "Focus Area", value = "Rotational Power")
            }
        }
    }
}

@Composable
fun ProfileStatItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = TextCrispWhite
            )
        )
    }
}

@Composable
fun ReadinessMetricsGrid(dailyLogs: List<DailyLog>) {
    val averageSleep = if (dailyLogs.isNotEmpty()) dailyLogs.map { it.sleepDuration }.average().toFloat() else 0f
    val averageSleepQuality = if (dailyLogs.isNotEmpty()) dailyLogs.map { it.sleepQuality }.average().toFloat() else 0f
    val averageEnergy = if (dailyLogs.isNotEmpty()) dailyLogs.map { it.energy }.average().toFloat() else 0f
    val averageRecovery = if (dailyLogs.isNotEmpty()) dailyLogs.map { it.recovery }.average().toFloat() else 0f

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ReadinessCard(
            title = "Sleep Vol",
            value = String.format("%.1f hrs", averageSleep),
            icon = Icons.Outlined.Bedtime,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
        ReadinessCard(
            title = "Sleep Quality",
            value = String.format("%.1f/5", averageSleepQuality),
            icon = Icons.Outlined.CheckCircle,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ReadinessCard(
            title = "Energy State",
            value = String.format("%.1f/5", averageEnergy),
            icon = Icons.Outlined.Bolt,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
        ReadinessCard(
            title = "Recovery Index",
            value = String.format("%.1f/5", averageRecovery),
            icon = Icons.Outlined.FavoriteBorder,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ReadinessCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextCrispWhite
                    )
                )
            }
        }
    }
}

@Composable
fun WorkloadCard(cricketLogs: List<CricketLog>, fitnessLogs: List<FitnessLog>) {
    val totalBattingMins = cricketLogs.sumOf { it.battingDuration }
    val totalBowlingOvers = cricketLogs.sumOf { it.bowlingWorkload }
    val totalFieldingMins = cricketLogs.sumOf { it.fieldingDuration }
    val totalGymMins = fitnessLogs.sumOf { it.gymDuration }
    val totalRunningKm = fitnessLogs.sumOf { it.runningDistance.toDouble() }.toFloat()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.QueryStats,
                        contentDescription = "Workload Stats",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Practice & Conditioning Volume",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            // Stat bars for visual density and polished feel
            WorkloadStatBar(
                label = "Batting Practice",
                value = "$totalBattingMins mins",
                percentage = (totalBattingMins / 300f).coerceIn(0f, 1f),
                color = AthleticNeon
            )

            WorkloadStatBar(
                label = "Bowling Workload",
                value = "$totalBowlingOvers overs",
                percentage = (totalBowlingOvers / 30f).coerceIn(0f, 1f),
                color = ElectricTeal
            )

            WorkloadStatBar(
                label = "Gym Strength & Rotational Work",
                value = "$totalGymMins mins",
                percentage = (totalGymMins / 300f).coerceIn(0f, 1f),
                color = AthleticNeon
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Fielding Practice",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = "$totalFieldingMins mins",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Cardio Running",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = String.format("%.2f km", totalRunningKm),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun WorkloadStatBar(
    label: String,
    value: String,
    percentage: Float,
    color: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextCrispWhite
                )
            )
        }

        LinearProgressIndicator(
            progress = percentage,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    }
}

@Composable
fun MatchLogItem(match: MatchLog) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = match.matchName,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${match.date} • ${match.format}",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }

                // Match outcome badge
                val isWin = match.matchResult.lowercase().contains("win")
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isWin) ElectricTeal.copy(alpha = 0.15f)
                            else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isWin) "W" else "L",
                        color = if (isWin) ElectricTeal else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Batting
                Column {
                    Text(
                        text = "BATTING",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "${match.runsScored} runs (${match.ballsFaced}b)",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // Bowling
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "BOWLING",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "${match.wicketsTaken} wkts (${match.oversBowled} ov, ${match.runsConceded}r)",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            if (match.fieldingPerformance.isNotBlank() || match.matchResult.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(8.dp)
                ) {
                    Column {
                        if (match.fieldingPerformance.isNotBlank()) {
                            Text(
                                text = "Fielding: ${match.fieldingPerformance}",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextMutedGray)
                            )
                        }
                        Text(
                            text = "Result: ${match.matchResult}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}
