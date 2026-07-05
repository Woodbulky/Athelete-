package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AthleteDatabase
import com.example.data.model.*
import com.example.data.repository.AthleteRepository
import com.example.data.api.GeminiApiManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class ChatMessage(
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class AthleteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AthleteRepository
    private val geminiApiManager = GeminiApiManager()

    // --- State Observables ---
    val dailyLogs: StateFlow<List<DailyLog>>
    val cricketLogs: StateFlow<List<CricketLog>>
    val fitnessLogs: StateFlow<List<FitnessLog>>
    val matchLogs: StateFlow<List<MatchLog>>
    val videoLogs: StateFlow<List<VideoLog>>

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    init {
        val database = AthleteDatabase.getDatabase(application)
        repository = AthleteRepository(database.athleteDao())

        dailyLogs = repository.allDailyLogs.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        cricketLogs = repository.allCricketLogs.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        fitnessLogs = repository.allFitnessLogs.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        matchLogs = repository.allMatchLogs.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        videoLogs = repository.allVideoLogs.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Prepopulate with mock data if everything is empty
        viewModelScope.launch {
            checkAndPrepopulateData()
        }

        // Welcome message from AI Coach
        _chatMessages.value = listOf(
            ChatMessage(
                content = "Welcome to **Athlete OS**, Champion! I am your **AI Performance Coach**.\n\nI have loaded your complete athletic database containing health metrics, batting sessions, fitness workouts, and match histories. Ask me questions like:\n- *What is my biggest weakness in batting right now?*\n- *Am I overtraining? Check my stress and recovery logs.*\n- *How did my sleep quality affect my match scores?*\n\nLet's analyze your data and find the evidence-backed adjustments to improve your game!",
                isUser = false
            )
        )
    }

    private suspend fun checkAndPrepopulateData() {
        // Simple check: if there are no daily logs, prepopulate everything
        val currentDaily = repository.getDailyLogByDate(getTodayDateString())
        val dbDailyLogs = repository.getDailyLogByDate(getDaysAgoDateString(1))
        
        // Let's inspect if database is empty by waiting or checking
        // Since we're in init, we'll wait for flows or query directly.
        // We will insert 5 days of realistic, Cricket-centric logs to make dashboard fully live!
        if (currentDaily == null && dbDailyLogs == null) {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val cal = Calendar.getInstance()

            // Prepopulate 5 days ago to today
            for (i in 5 downTo 0) {
                cal.time = Date()
                cal.add(Calendar.DAY_OF_YEAR, -i)
                val dateStr = sdf.format(cal.time)
                val ts = cal.timeInMillis

                // 1. Daily logs
                val sleepQuality = when(i) {
                    4 -> 2 // Bad sleep before a rough match day
                    else -> 4
                }
                val sleepDuration = when(i) {
                    4 -> 5.8f
                    2 -> 8.2f
                    else -> 7.4f
                }
                val mood = if (i == 4) 2 else 4
                val stress = if (i == 4) 5 else 2
                val energy = if (i == 4) 3 else 4
                val recovery = if (i == 4) 2 else 4

                repository.insertDailyLog(
                    DailyLog(
                        date = dateStr,
                        timestamp = ts,
                        wakeTime = "06:45",
                        sleepDuration = sleepDuration,
                        sleepQuality = sleepQuality,
                        meals = "Breakfast: Eggs & Oats. Lunch: Chicken & Rice, Greens. Dinner: Salmon & Quinoa.",
                        waterIntake = 3.5f,
                        supplements = "Creatine 5g, Whey Isolate, Vitamin D3",
                        mood = mood,
                        stress = stress,
                        energy = energy,
                        recovery = recovery
                    )
                )

                // 2. Cricket Practice (on some days)
                if (i in listOf(5, 3, 1)) {
                    val battingFocus = when(i) {
                        5 -> "Playing left-arm spin. Emphasized staying low and using wrists."
                        3 -> "Power-hitting drills. Focus on clearance and bat flow."
                        else -> "Facing leg-spin. Struggling with balance when lunging forward."
                    }
                    val bowlingWorkload = when(i) {
                        5 -> 5 // 5 overs
                        3 -> 4
                        else -> 0
                    }
                    val weaknesses = when(i) {
                        1 -> "Lunging forward to leg-spin, head falls off-balance to the off side."
                        else -> "Slight delay in backfoot transition."
                    }

                    repository.insertCricketLog(
                        CricketLog(
                            date = dateStr,
                            timestamp = ts,
                            battingDuration = 45,
                            battingFocus = battingFocus,
                            bowlingWorkload = bowlingWorkload,
                            bowlingFocus = "Consistent line & length, testing outswingers.",
                            fieldingDuration = 20,
                            fieldingFocus = "High catches & boundary relay throws.",
                            coachFeedback = "Head falling away slightly on front-foot drives against turning ball. Need to stay aligned.",
                            strengths = "Excellent bat speed and strong cover drives.",
                            weaknesses = weaknesses
                        )
                    )
                }

                // 3. Fitness Workouts
                if (i in listOf(5, 4, 2, 0)) {
                    val gym = when(i) {
                        5 -> "Upper Body Power: Bench Press, Pullups, Medicine ball throws."
                        4 -> "Lower Body Strength: Back squats 4x6, Romanian deadlifts 3x8, Core."
                        2 -> "Mobility & Conditioning: Sprint intervals, deep hip stretching."
                        else -> "Core & Cricket-specific rotational power exercises."
                    }
                    val gymDur = if (i == 2) 30 else 50
                    val runningDist = if (i == 2) 4.0f else 0f
                    val runningDur = if (i == 2) 18 else 0

                    repository.insertFitnessLog(
                        FitnessLog(
                            date = dateStr,
                            timestamp = ts,
                            gymSession = gym,
                            gymDuration = gymDur,
                            runningDistance = runningDist,
                            runningDuration = runningDur,
                            mobilityDuration = 15,
                            recoveryActivity = if (i == 4) "Ice bath & Foam roller" else "Standard stretching",
                            injuries = if (i == 4) "Mild tightness in left hamstring" else "No injuries, feeling fresh"
                        )
                    )
                }

                // 4. Match (recorded on Day 4)
                if (i == 4) {
                    repository.insertMatchLog(
                        MatchLog(
                            date = dateStr,
                            timestamp = ts,
                            matchName = "Elite Division T20 Quarterfinal",
                            format = "T20",
                            role = "All-rounder",
                            runsScored = 14,
                            ballsFaced = 11,
                            wicketsTaken = 2,
                            oversBowled = 4.0f,
                            runsConceded = 22,
                            fieldingPerformance = "1 direct hit runout from mid-off, 1 catch.",
                            matchResult = "Win (Won by 18 runs)"
                        )
                    )
                }

                // 5. Video analysis (recorded on Day 1)
                if (i == 1) {
                    repository.insertVideoLog(
                        VideoLog(
                            date = dateStr,
                            timestamp = ts,
                            title = "Net session vs Leg-Spinner",
                            videoUri = "practice_leg_spin_03_07.mp4",
                            analysisNotes = "Self-Analysis: Noticed on frame 120 that my head falls to the off side by about 4 inches, causing me to play around the front-foot defensive shot. Need to lock the front shoulder and keep head directly over the ball."
                        )
                    )
                }
            }
        }
    }

    // --- Helper Functions to insert data ---
    fun saveDailyLog(
        wakeTime: String,
        sleepDuration: Float,
        sleepQuality: Int,
        meals: String,
        waterIntake: Float,
        supplements: String,
        mood: Int,
        stress: Int,
        energy: Int,
        recovery: Int,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            val log = DailyLog(
                date = getTodayDateString(),
                wakeTime = wakeTime,
                sleepDuration = sleepDuration,
                sleepQuality = sleepQuality,
                meals = meals,
                waterIntake = waterIntake,
                supplements = supplements,
                mood = mood,
                stress = stress,
                energy = energy,
                recovery = recovery
            )
            repository.insertDailyLog(log)
            onComplete()
        }
    }

    fun saveCricketLog(
        battingDuration: Int,
        battingFocus: String,
        bowlingWorkload: Int,
        bowlingFocus: String,
        fieldingDuration: Int,
        fieldingFocus: String,
        coachFeedback: String,
        strengths: String,
        weaknesses: String,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            val log = CricketLog(
                date = getTodayDateString(),
                battingDuration = battingDuration,
                battingFocus = battingFocus,
                bowlingWorkload = bowlingWorkload,
                bowlingFocus = bowlingFocus,
                fieldingDuration = fieldingDuration,
                fieldingFocus = fieldingFocus,
                coachFeedback = coachFeedback,
                strengths = strengths,
                weaknesses = weaknesses
            )
            repository.insertCricketLog(log)
            onComplete()
        }
    }

    fun saveFitnessLog(
        gymSession: String,
        gymDuration: Int,
        runningDistance: Float,
        runningDuration: Int,
        mobilityDuration: Int,
        recoveryActivity: String,
        injuries: String,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            val log = FitnessLog(
                date = getTodayDateString(),
                gymSession = gymSession,
                gymDuration = gymDuration,
                runningDistance = runningDistance,
                runningDuration = runningDuration,
                mobilityDuration = mobilityDuration,
                recoveryActivity = recoveryActivity,
                injuries = injuries
            )
            repository.insertFitnessLog(log)
            onComplete()
        }
    }

    fun saveMatchLog(
        matchName: String,
        format: String,
        role: String,
        runsScored: Int,
        ballsFaced: Int,
        wicketsTaken: Int,
        oversBowled: Float,
        runsConceded: Int,
        fieldingPerformance: String,
        matchResult: String,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            val log = MatchLog(
                date = getTodayDateString(),
                matchName = matchName,
                format = format,
                role = role,
                runsScored = runsScored,
                ballsFaced = ballsFaced,
                wicketsTaken = wicketsTaken,
                oversBowled = oversBowled,
                runsConceded = runsConceded,
                fieldingPerformance = fieldingPerformance,
                matchResult = matchResult
            )
            repository.insertMatchLog(log)
            onComplete()
        }
    }

    fun saveVideoLog(
        title: String,
        videoUri: String,
        analysisNotes: String,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            val log = VideoLog(
                date = getTodayDateString(),
                title = title,
                videoUri = videoUri,
                analysisNotes = analysisNotes
            )
            repository.insertVideoLog(log)
            onComplete()
        }
    }

    // --- AI Coach Chat ---
    fun askAiCoach(userQuestion: String) {
        if (userQuestion.isBlank()) return

        // 1. Add User message to chat
        val currentList = _chatMessages.value.toMutableList()
        currentList.add(ChatMessage(content = userQuestion, isUser = true))
        _chatMessages.value = currentList

        _isAiLoading.value = true

        viewModelScope.launch {
            // 2. Fetch full context logs
            val dailyList = dailyLogs.value
            val cricketList = cricketLogs.value
            val fitnessList = fitnessLogs.value
            val matchList = matchLogs.value
            val videoList = videoLogs.value

            // 3. Compile context text
            val formattedContext = formatLogsForContext(
                dailyLogs = dailyList,
                cricketLogs = cricketList,
                fitnessLogs = fitnessList,
                matchLogs = matchList,
                videoLogs = videoList
            )

            // 4. Setup System Instruction
            val systemInstruction = """
                You are Athlete OS AI Performance Coach, an elite AI coaching assistant for a professional cricket athlete.
                Your core responsibility is to analyze ONLY the provided athlete data to make evidence-based insights, recommendations, and answers.
                
                RULES:
                1. NEVER give generic coaching advice. Every response should reference the athlete's actual logged metrics, workouts, matches, and video analysis.
                2. Point out specific dates, numbers, or feedback from their history (e.g. "On 2026-07-02, you noted that...").
                3. If there is not enough evidence or data in their logs to fully answer the question, state it honestly: "I don't have enough logged data to answer that definitively." Suggest precisely what parameters they should track in their logs next (e.g., track sleep quality for 7 consecutive days).
                4. Focus on cricket-specific concepts: batting technique, bowling workload (overs bowled and fatigue), physical conditioning, recovery activities (ice baths), and stress levels.
                5. Structure your output elegantly. Use markdown headings, bold terms, and bullet points. Example format:
                   ### COACHING ANALYSIS
                   - **The Issue**: [Short explanation]
                   - **Evidence Found**: [Reference specific logs, values, or dates]
                   - **Action Plan**: [Clear, bulleted instructions for tomorrow or next session]
                   
                Avoid clinical jargon, keep tone objective, encouraging, and highly professional.
            """.trimIndent()

            // 5. Generate prompt
            val prompt = """
                $formattedContext
                
                ATHLETE'S QUESTION:
                $userQuestion
            """.trimIndent()

            // 6. Call API
            val aiResponse = geminiApiManager.generateCoachingInsight(prompt, systemInstruction)

            // 7. Add AI response to chat
            val updatedList = _chatMessages.value.toMutableList()
            updatedList.add(ChatMessage(content = aiResponse, isUser = false))
            _chatMessages.value = updatedList

            _isAiLoading.value = false
        }
    }

    // --- Helpers to format context ---
    private fun formatLogsForContext(
        dailyLogs: List<DailyLog>,
        cricketLogs: List<CricketLog>,
        fitnessLogs: List<FitnessLog>,
        matchLogs: List<MatchLog>,
        videoLogs: List<VideoLog>
    ): String {
        val sb = StringBuilder()
        sb.append("ATHLETE OS DATABASE - COMPLETE PERFORMANCE RECORD:\n\n")

        sb.append("=== SECTION 1: DAILY HEALTH & METRICS ===\n")
        if (dailyLogs.isEmpty()) {
            sb.append("No entries recorded yet.\n")
        } else {
            dailyLogs.take(15).forEach { log ->
                sb.append("- [Date: ${log.date}] Wake: ${log.wakeTime}, Sleep: ${log.sleepDuration} hrs, Quality: ${log.sleepQuality}/5, Mood: ${log.mood}/5, Stress: ${log.stress}/5, Energy: ${log.energy}/5, Recovery: ${log.recovery}/5. Water: ${log.waterIntake}L, Meals: ${log.meals}, Supplements: ${log.supplements}\n")
            }
        }
        sb.append("\n")

        sb.append("=== SECTION 2: CRICKET SKILLS PRACTICE ===\n")
        if (cricketLogs.isEmpty()) {
            sb.append("No practice logs recorded yet.\n")
        } else {
            cricketLogs.take(15).forEach { log ->
                sb.append("- [Date: ${log.date}] Batting: ${log.battingDuration} mins (Focus: ${log.battingFocus}), Bowling Workload: ${log.bowlingWorkload} overs (Focus: ${log.bowlingFocus}), Fielding: ${log.fieldingDuration} mins (Focus: ${log.fieldingFocus}). Feedback: ${log.coachFeedback}. Strengths: ${log.strengths}, Weaknesses: ${log.weaknesses}\n")
            }
        }
        sb.append("\n")

        sb.append("=== SECTION 3: PHYSICAL FITNESS & WORKOUTS ===\n")
        if (fitnessLogs.isEmpty()) {
            sb.append("No workouts recorded yet.\n")
        } else {
            fitnessLogs.take(15).forEach { log ->
                sb.append("- [Date: ${log.date}] Gym Session: ${log.gymSession} (${log.gymDuration} mins), Cardio Running: ${log.runningDistance} km in ${log.runningDuration} mins, Mobility: ${log.mobilityDuration} mins, Recovery Activity: ${log.recoveryActivity}. Injury/Pain Notes: ${log.injuries}\n")
            }
        }
        sb.append("\n")

        sb.append("=== SECTION 4: MATCH PERFORMANCE RECORDS ===\n")
        if (matchLogs.isEmpty()) {
            sb.append("No matches recorded yet.\n")
        } else {
            matchLogs.take(15).forEach { log ->
                sb.append("- [Date: ${log.date}] Match: ${log.matchName} (${log.format}), Role: ${log.role}, Runs: ${log.runsScored} runs off ${log.ballsFaced} balls, Bowling: ${log.wicketsTaken} wickets (Overs: ${log.oversBowled}, Runs Conceded: ${log.runsConceded}), Fielding Highlights: ${log.fieldingPerformance}, Result: ${log.matchResult}\n")
            }
        }
        sb.append("\n")

        sb.append("=== SECTION 5: VIDEO TECHNICAL ANALYSIS ===\n")
        if (videoLogs.isEmpty()) {
            sb.append("No videos recorded yet.\n")
        } else {
            videoLogs.take(15).forEach { log ->
                sb.append("- [Date: ${log.date}] Clip Title: ${log.title}, Ref File: ${log.videoUri}, Technical Analysis: ${log.analysisNotes}\n")
            }
        }
        sb.append("\n")

        return sb.toString()
    }

    // --- Date Formatting Helpers ---
    fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    private fun getDaysAgoDateString(daysAgo: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
    }
}
