package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = AthleticNeon,
    onPrimary = OnAthleticBlack,
    secondary = ElectricTeal,
    onSecondary = OnAthleticBlack,
    tertiary = CoralAlert,
    background = CarbonBg,
    onBackground = TextCrispWhite,
    surface = CarbonSurface,
    onSurface = TextCrispWhite,
    surfaceVariant = CarbonSurfaceVariant,
    onSurfaceVariant = TextMutedGray,
    outline = BorderCarbon
  )

private val LightColorScheme =
  darkColorScheme( // Use the beautiful dark scheme as default to keep visual identity premium!
    primary = AthleticNeon,
    onPrimary = OnAthleticBlack,
    secondary = ElectricTeal,
    onSecondary = OnAthleticBlack,
    tertiary = CoralAlert,
    background = CarbonBg,
    onBackground = TextCrispWhite,
    surface = CarbonSurface,
    onSurface = TextCrispWhite,
    surfaceVariant = CarbonSurfaceVariant,
    onSurfaceVariant = TextMutedGray,
    outline = BorderCarbon
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force dark theme for sport performance look!
  dynamicColor: Boolean = false, // Disable dynamic colors to preserve our brand identity
  content: @Composable () -> Unit,
) {
  val colorScheme = DarkColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
