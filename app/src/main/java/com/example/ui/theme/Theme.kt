package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = EditorialCobaltLight,
    onPrimary = EditorialNavyDeep,
    primaryContainer = EditorialNavyMid,
    onPrimaryContainer = EditorialCobaltLight,
    secondary = EditorialCobalt,
    onSecondary = Color.White,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = TextPrimaryDark,
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = DarkBorder
  )

private val LightColorScheme =
  lightColorScheme(
    primary = EditorialCobalt,
    onPrimary = Color.White,
    primaryContainer = EditorialCobaltLight,
    onPrimaryContainer = EditorialNavyDeep,
    secondary = EditorialNavyMid,
    onSecondary = Color.White,
    secondaryContainer = EditorialSurfaceVariant,
    onSecondaryContainer = EditorialInk,
    background = EditorialPaper,
    onBackground = EditorialInk,
    surface = EditorialSurface,
    onSurface = EditorialInk,
    surfaceVariant = EditorialSurfaceVariant,
    onSurfaceVariant = EditorialInkMuted,
    outline = EditorialBorder,
    outlineVariant = EditorialBorderHairline
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false,
  // Keep false by default to ensure Editorial Aesthetic colors are preserved
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
