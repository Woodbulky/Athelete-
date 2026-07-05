package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.ChatMessage
import com.example.ui.theme.*

@Composable
fun AICoachScreen(
    chatMessages: List<ChatMessage>,
    isAiLoading: Boolean,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var userText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current

    // Automatically scroll to the bottom when new messages arrive
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- 1. CHAT LIST ---
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ) {
            items(chatMessages) { message ->
                ChatBubble(message)
            }

            if (isAiLoading) {
                item {
                    TypingIndicatorBubble()
                }
            }
        }

        // --- 2. QUICK SUGGESTION PILLS ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "Tap to ask AI Performance Coach:",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SuggestionPill(
                    label = "🔍 Batting Weakness?",
                    modifier = Modifier.weight(1f)
                ) {
                    onSendMessage("What is my biggest weakness in batting right now according to my practice and match logs? Point out the evidence.")
                }
                
                SuggestionPill(
                    label = "📊 Overtraining Check?",
                    modifier = Modifier.weight(1f)
                ) {
                    onSendMessage("Analyze my recent sleep quality, stress levels, and workout workload logs. Am I overtraining? Show me the trends.")
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SuggestionPill(
                    label = "🏏 Tomorrow's Drills?",
                    modifier = Modifier.weight(1f)
                ) {
                    onSendMessage("Based on my latest net session coach feedback and self-analysis logs, what specific technical drills should I practice tomorrow?")
                }

                SuggestionPill(
                    label = "📉 Habits & Scores?",
                    modifier = Modifier.weight(1f)
                ) {
                    onSendMessage("Is there a connection in my logs between my sleep duration/quality and my match scores or training performance? Analyze the correlation.")
                }
            }
        }

        // --- 3. INPUT BAR ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .navigationBarsPadding()
                .imePadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = userText,
                onValueChange = { userText = it },
                placeholder = { Text("Ask Coach about your metrics...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_coach_input"),
                shape = RoundedCornerShape(24.dp),
                maxLines = 3,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (userText.isNotBlank()) {
                            onSendMessage(userText)
                            userText = ""
                            focusManager.clearFocus()
                        }
                    }
                )
            )

            FloatingActionButton(
                onClick = {
                    if (userText.isNotBlank()) {
                        onSendMessage(userText)
                        userText = ""
                        focusManager.clearFocus()
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .testTag("ai_coach_send_button"),
                shape = RoundedCornerShape(24.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = OnAthleticBlack
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun SuggestionPill(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = TextCrispWhite
            ),
            fontSize = 11.sp,
            maxLines = 1
        )
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val containerColor = if (message.isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (message.isUser) OnAthleticBlack else TextCrispWhite
    val bubbleShape = if (message.isUser) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp)
    } else {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        // Coach badge
        if (!message.isUser) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SportsCricket,
                    contentDescription = "Coach icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "AI PERFORMANCE COACH",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    fontSize = 9.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(bubbleShape)
                .background(containerColor)
                .padding(14.dp)
        ) {
            // Simple helper to parse basic markdown from responses (*bold*, ###, etc.)
            FormattedChatMessageText(text = message.content, textColor = textColor)
        }
    }
}

@Composable
fun FormattedChatMessageText(text: String, textColor: Color) {
    // Process markdown headers and list markers
    val lines = text.split("\n")
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        lines.forEach { line ->
            val cleanLine = line.trim()
            if (cleanLine.startsWith("###")) {
                Text(
                    text = cleanLine.substring(3).trim(),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                )
            } else if (cleanLine.startsWith("-") || cleanLine.startsWith("•")) {
                val bulletText = cleanLine.substring(1).trim()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("•", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Text(
                        text = parseBoldText(bulletText),
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                Text(
                    text = parseBoldText(line),
                    color = textColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// Simple parsing for **bold text** to annotated or bold format
private fun parseBoldText(text: String): String {
    // Return direct text but clean up the double stars for simplicity of display in text,
    // or let's keep it as is, or remove asterisks.
    return text.replace("**", "")
}

@Composable
fun TypingIndicatorBubble() {
    val infiniteTransition = rememberInfiniteTransition(label = "typing")
    val dotAlpha1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteTransitionSpec(0),
        label = "dot1"
    )
    val dotAlpha2 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteTransitionSpec(150),
        label = "dot2"
    )
    val dotAlpha3 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteTransitionSpec(300),
        label = "dot3"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SportsCricket,
                contentDescription = "Coach icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "COACH IS ANALYZING...",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                fontSize = 9.sp
            )
        }

        Box(
            modifier = Modifier
                .width(80.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(14.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(MaterialTheme.colorScheme.primary.copy(alpha = dotAlpha1)))
                Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(MaterialTheme.colorScheme.primary.copy(alpha = dotAlpha2)))
                Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(MaterialTheme.colorScheme.primary.copy(alpha = dotAlpha3)))
            }
        }
    }
}

private fun infiniteTransitionSpec(delayMs: Int) = infiniteRepeatable<Float>(
    animation = keyframes {
        durationMillis = 1000
        0.2f at delayMs with LinearEasing
        1.0f at delayMs + 300 with LinearEasing
        0.2f at delayMs + 600 with LinearEasing
    },
    repeatMode = RepeatMode.Restart
)
