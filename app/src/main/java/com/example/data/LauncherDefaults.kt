package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.model.AppItem
import com.example.model.GlassStyle
import com.example.model.WallpaperTheme
import com.example.model.WeatherLocation
import com.example.model.WidgetItem
import com.example.model.WidgetSize
import com.example.model.WidgetType

object LauncherDefaults {

  val defaultWallpapers = listOf(
    WallpaperTheme(
      id = "pixel_obsidian",
      name = "Pixel Obsidian",
      primaryAccent = Color(0xFF8AB4F8),
      secondaryAccent = Color(0xFFC58AF9),
      backgroundGradient = listOf(
        Color(0xFF0D0F18),
        Color(0xFF141726),
        Color(0xFF090A10)
      ),
      isDark = true
    ),
    WallpaperTheme(
      id = "aurora_borealis",
      name = "Nordic Aurora",
      primaryAccent = Color(0xFF34D399),
      secondaryAccent = Color(0xFF60A5FA),
      backgroundGradient = listOf(
        Color(0xFF06202A),
        Color(0xFF0A343D),
        Color(0xFF04151B)
      ),
      isDark = true
    ),
    WallpaperTheme(
      id = "sunset_dunes",
      name = "Pixel Sunset",
      primaryAccent = Color(0xFFFF8A65),
      secondaryAccent = Color(0xFFFFD54F),
      backgroundGradient = listOf(
        Color(0xFF2E151B),
        Color(0xFF3F1D24),
        Color(0xFF190C10)
      ),
      isDark = true
    ),
    WallpaperTheme(
      id = "botanical_sage",
      name = "Material Sage",
      primaryAccent = Color(0xFFA8DAB5),
      secondaryAccent = Color(0xFFD4E157),
      backgroundGradient = listOf(
        Color(0xFF111E16),
        Color(0xFF1A2D22),
        Color(0xFF0A130E)
      ),
      isDark = true
    ),
    WallpaperTheme(
      id = "cyber_neon",
      name = "Cyber Violet",
      primaryAccent = Color(0xFFE879F9),
      secondaryAccent = Color(0xFF38BDF8),
      backgroundGradient = listOf(
        Color(0xFF160B24),
        Color(0xFF24103A),
        Color(0xFF0B0513)
      ),
      isDark = true
    ),
    WallpaperTheme(
      id = "arctic_silver",
      name = "Clean Porcelain",
      primaryAccent = Color(0xFF2563EB),
      secondaryAccent = Color(0xFF7C3AED),
      backgroundGradient = listOf(
        Color(0xFFE2E8F0),
        Color(0xFFCBD5E1),
        Color(0xFFF1F5F9)
      ),
      isDark = false
    )
  )

  val defaultApps = listOf(
    AppItem("phone", "Phone", "com.google.android.dialer", "Daily", Color(0xFF34A853), "phone"),
    AppItem("messages", "Messages", "com.google.android.apps.messaging", "Daily", Color(0xFF4285F4), "chat"),
    AppItem("chrome", "Chrome", "com.android.chrome", "Daily", Color(0xFFEA4335), "public"),
    AppItem("camera", "Camera", "com.google.android.GoogleCamera", "Media", Color(0xFF202124), "photo_camera"),
    AppItem("ar_assistant", "AR Assistant", "com.google.android.apps.ar", "AI & AR", Color(0xFF9333EA), "auto_awesome"),
    AppItem("photos", "Photos", "com.google.android.apps.photos", "Media", Color(0xFFFBBC04), "collections"),
    AppItem("settings", "Settings", "com.android.settings", "System", Color(0xFF5F6368), "settings"),
    AppItem("music", "Music", "com.google.android.apps.youtube.music", "Media", Color(0xFFFF0033), "music_note"),
    AppItem("calendar", "Calendar", "com.google.android.calendar", "Productivity", Color(0xFF4285F4), "event"),
    AppItem("maps", "Maps", "com.google.android.apps.maps", "Daily", Color(0xFF34A853), "place"),
    AppItem("clock", "Clock", "com.google.android.deskclock", "Tools", Color(0xFF3B82F6), "schedule"),
    AppItem("weather", "Weather", "com.google.android.apps.weather", "Daily", Color(0xFF0284C7), "wb_sunny"),
    AppItem("files", "Files", "com.google.android.apps.nbu.files", "Tools", Color(0xFF009688), "folder"),
    AppItem("calculator", "Calculator", "com.google.android.calculator", "Tools", Color(0xFF6366F1), "calculate"),
    AppItem("notes", "Keep Notes", "com.google.android.keep", "Productivity", Color(0xFFF59E0B), "edit_note"),
    AppItem("health", "Fit & Health", "com.google.android.apps.fitness", "Daily", Color(0xFFEF4444), "favorite")
  )

  val defaultDockAppIds = listOf("phone", "messages", "chrome", "camera", "ar_assistant")

  val initialWidgets = listOf(
    WidgetItem(
      id = "w_at_a_glance",
      type = WidgetType.AT_A_GLANCE,
      size = WidgetSize.MEDIUM,
      cornerRadiusDp = 26,
      glassStyle = GlassStyle.FROSTED_GLASS,
      opacity = 0.90f
    ),
    WidgetItem(
      id = "w_now_playing",
      type = WidgetType.NOW_PLAYING,
      size = WidgetSize.MEDIUM,
      cornerRadiusDp = 26,
      glassStyle = GlassStyle.FROSTED_GLASS,
      opacity = 0.92f
    ),
    WidgetItem(
      id = "w_battery",
      type = WidgetType.BATTERY_TELEMETRY,
      size = WidgetSize.COMPACT,
      cornerRadiusDp = 26,
      glassStyle = GlassStyle.FROSTED_GLASS,
      opacity = 0.88f
    ),
    WidgetItem(
      id = "w_quick_toggles",
      type = WidgetType.QUICK_TOGGLES,
      size = WidgetSize.COMPACT,
      cornerRadiusDp = 26,
      glassStyle = GlassStyle.FROSTED_GLASS,
      opacity = 0.88f
    ),
    WidgetItem(
      id = "w_ar_stack",
      type = WidgetType.AR_SMART_STACK,
      size = WidgetSize.MEDIUM,
      cornerRadiusDp = 26,
      glassStyle = GlassStyle.PIXEL_TINT,
      opacity = 0.92f
    )
  )

  val defaultLocations = listOf(
    WeatherLocation("London", 19, "Partly Cloudy", "wb_cloudy"),
    WeatherLocation("Tokyo", 24, "Clear Skies", "wb_sunny"),
    WeatherLocation("New York", 22, "Sunny", "wb_sunny"),
    WeatherLocation("Paris", 20, "Mild Breeze", "wb_cloudy"),
    WeatherLocation("San Francisco", 17, "Cool Fog", "wb_cloudy"),
    WeatherLocation("Berlin", 18, "Crisp Air", "wb_cloudy"),
    WeatherLocation("Singapore", 31, "Tropical Warm", "wb_sunny"),
    WeatherLocation("Sydney", 23, "Sunny & Warm", "wb_sunny"),
    WeatherLocation("Dubai", 35, "Clear Sun", "wb_sunny"),
    WeatherLocation("Toronto", 16, "Fresh Autumn", "wb_sunny")
  )
}
