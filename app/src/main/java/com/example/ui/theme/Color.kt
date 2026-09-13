package com.example.ui.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Pixel Signature Material You Accents
val PixelBlue = Color(0xFF4285F4)
val PixelRed = Color(0xFFEA4335)
val PixelYellow = Color(0xFFFBBC04)
val PixelGreen = Color(0xFF34A853)

// iOS 26 Fluid Dark & Glass Tints
val ObsidianDark = Color(0xFF0C0E14)
val ObsidianCard = Color(0xCC181C26)
val FrostedGlassBackground = Color(0x33FFFFFF)
val FrostedGlassBorder = Color(0x3DFFFFFF)
val GlassSurfaceDark = Color(0xB812151F)
val GlassHighlight = Color(0x2BFFFFFF)

// Dynamic Island
val DynamicIslandBlack = Color(0xF506070B)
val DynamicIslandAccent = Color(0xFF8AB4F8)

// Physics spring animation presets for iOS 26 fluidity
fun <T> iosSpring(damping: Float = 0.74f, stiffness: Float = 400f) =
  spring<T>(dampingRatio = damping, stiffness = stiffness)

val iOSFluidSpring = spring<Float>(dampingRatio = 0.75f, stiffness = 380f)
val iOSBouncySpring = spring<Float>(dampingRatio = 0.62f, stiffness = 320f)

// Ambient Gradients
val GlassmorphismBrush = Brush.verticalGradient(
  listOf(Color(0x38FFFFFF), Color(0x14FFFFFF))
)

val DarkGlassmorphismBrush = Brush.verticalGradient(
  listOf(Color(0xD91E2230), Color(0xBF11131C))
)
