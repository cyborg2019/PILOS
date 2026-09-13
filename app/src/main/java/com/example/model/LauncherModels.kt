package com.example.model

import androidx.compose.ui.graphics.Color

enum class WidgetType(val displayName: String, val description: String) {
  AT_A_GLANCE("Pixel At a Glance", "Live date, Celsius weather by location, and AR Assistant insights"),
  NOW_PLAYING("Now Playing Island", "Music player with animated waveform audio visualizer"),
  BATTERY_TELEMETRY("System & Battery", "Circular gauges for battery %, RAM, and device health"),
  CLOCK("Pixel Bold Clock", "Digital typography clock with seconds and world time"),
  AR_SMART_STACK("AR Assistant Stack", "Contextual action recommendations and proactive spatial prompts"),
  QUICK_TOGGLES("Control Center", "Fast toggles for Wi-Fi, Bluetooth, Torch, DND, and AR"),
  PHOTO_MEMORIES("Photo Memories", "Curated photography frames with mood tint")
}

enum class WidgetSize(val spanColumns: Int, val label: String) {
  COMPACT(1, "Compact (2x2)"),
  MEDIUM(2, "Wide (4x2)"),
  EXPANDED(2, "Large (4x4)")
}

enum class GlassStyle(val displayName: String) {
  FROSTED_GLASS("iOS 26 Frosted Glass"),
  OLED_DARK("OLED Deep Black"),
  PIXEL_TINT("Pixel Material Tint"),
  ULTRA_CLEAR("Minimal Border")
}

data class WidgetItem(
  val id: String,
  val type: WidgetType,
  val size: WidgetSize = WidgetSize.MEDIUM,
  val cornerRadiusDp: Int = 26,
  val glassStyle: GlassStyle = GlassStyle.FROSTED_GLASS,
  val opacity: Float = 0.88f
)

data class AppItem(
  val id: String,
  val name: String,
  val packageName: String,
  val category: String,
  val accentColor: Color,
  val iconSymbol: String,
  val isSystemApp: Boolean = true
)

enum class IslandMode {
  COLLAPSED,
  MUSIC_ACTIVE,
  AR_THINKING,
  SYSTEM_ALERT
}

data class WallpaperTheme(
  val id: String,
  val name: String,
  val primaryAccent: Color,
  val secondaryAccent: Color,
  val backgroundGradient: List<Color>,
  val isDark: Boolean = true
)

data class ArAssistantAdaptationResult(
  val modeTitle: String,
  val summaryGreeting: String,
  val recommendedThemeId: String,
  val recommendedWidgets: List<WidgetType>,
  val recommendedDockAppIds: List<String>,
  val aiThoughtReasoning: String
)

typealias GeminiAdaptationResult = ArAssistantAdaptationResult

data class WeatherLocation(
  val city: String,
  val tempCelsius: Int,
  val condition: String,
  val icon: String = "wb_sunny"
)
