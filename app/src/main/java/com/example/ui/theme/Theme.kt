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

private val DarkColorScheme = darkColorScheme(
  primary = Color(0xFF8AB4F8),
  secondary = Color(0xFFC58AF9),
  tertiary = Color(0xFF81C995),
  background = Color(0xFF0C0E14),
  surface = Color(0xFF141824),
  onPrimary = Color(0xFF002B5B),
  onSecondary = Color(0xFF381E72),
  onTertiary = Color(0xFF00391A),
  onBackground = Color(0xFFE2E8F0),
  onSurface = Color(0xFFE2E8F0)
)

private val LightColorScheme = lightColorScheme(
  primary = Color(0xFF1A73E8),
  secondary = Color(0xFF7C3AED),
  tertiary = Color(0xFF1E8E3E),
  background = Color(0xFFF8FAFC),
  surface = Color(0xFFFFFFFF),
  onPrimary = Color.White,
  onSecondary = Color.White,
  onTertiary = Color.White,
  onBackground = Color(0xFF0F172A),
  onSurface = Color(0xFF0F172A)
)

@Composable
fun PilosLauncherTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit
) {
  PilosLauncherTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
