package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GeminiService
import com.example.data.LauncherDefaults
import com.example.model.AppItem
import com.example.model.GeminiAdaptationResult
import com.example.model.GlassStyle
import com.example.model.IslandMode
import com.example.model.WallpaperTheme
import com.example.model.WeatherLocation
import com.example.model.WidgetItem
import com.example.model.WidgetSize
import com.example.model.WidgetType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class LauncherViewModel : ViewModel() {

  private val geminiService = GeminiService()

  // Widgets state
  private val _widgets = MutableStateFlow<List<WidgetItem>>(LauncherDefaults.initialWidgets)
  val widgets: StateFlow<List<WidgetItem>> = _widgets.asStateFlow()

  // Apps state
  private val _apps = MutableStateFlow<List<AppItem>>(LauncherDefaults.defaultApps)
  val apps: StateFlow<List<AppItem>> = _apps.asStateFlow()

  private val _dockAppIds = MutableStateFlow<List<String>>(LauncherDefaults.defaultDockAppIds)
  val dockAppIds: StateFlow<List<String>> = _dockAppIds.asStateFlow()

  // Theme & Wallpaper
  private val _currentTheme = MutableStateFlow(LauncherDefaults.defaultWallpapers.first())
  val currentTheme: StateFlow<WallpaperTheme> = _currentTheme.asStateFlow()

  // Dynamic Island
  private val _islandMode = MutableStateFlow(IslandMode.COLLAPSED)
  val islandMode: StateFlow<IslandMode> = _islandMode.asStateFlow()

  private val _isPlaying = MutableStateFlow(true)
  val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

  private val _currentTrack = MutableStateFlow("Midnight Echoes - Lofi Beats")
  val currentTrack: StateFlow<String> = _currentTrack.asStateFlow()

  // At a Glance live context (AR Assistant)
  private val _geminiGreeting = MutableStateFlow("✨ AR Assistant · Proactive scene perception ready")
  val geminiGreeting: StateFlow<String> = _geminiGreeting.asStateFlow()

  // Location & Weather State (Celsius)
  private val _currentLocation = MutableStateFlow(LauncherDefaults.defaultLocations.first())
  val currentLocation: StateFlow<WeatherLocation> = _currentLocation.asStateFlow()

  val availableLocations: List<WeatherLocation> = LauncherDefaults.defaultLocations

  private val _isLocationPickerOpen = MutableStateFlow(false)
  val isLocationPickerOpen: StateFlow<Boolean> = _isLocationPickerOpen.asStateFlow()

  // UI Navigation / Overlay states
  private val _activeApp = MutableStateFlow<AppItem?>(null)
  val activeApp: StateFlow<AppItem?> = _activeApp.asStateFlow()

  private val _isDrawerOpen = MutableStateFlow(false)
  val isDrawerOpen: StateFlow<Boolean> = _isDrawerOpen.asStateFlow()

  private val _isCustomizationOpen = MutableStateFlow(false)
  val isCustomizationOpen: StateFlow<Boolean> = _isCustomizationOpen.asStateFlow()

  private val _isGeminiSheetOpen = MutableStateFlow(false)
  val isGeminiSheetOpen: StateFlow<Boolean> = _isGeminiSheetOpen.asStateFlow()

  private val _isEditMode = MutableStateFlow(false)
  val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()

  // Gemini Adaptation state
  private val _isGeminiLoading = MutableStateFlow(false)
  val isGeminiLoading: StateFlow<Boolean> = _isGeminiLoading.asStateFlow()

  private val _geminiResult = MutableStateFlow<GeminiAdaptationResult?>(null)
  val geminiResult: StateFlow<GeminiAdaptationResult?> = _geminiResult.asStateFlow()

  // Playback controls
  fun togglePlay() {
    _isPlaying.value = !_isPlaying.value
  }

  fun nextTrack() {
    val tracks = listOf(
      "Celestial Resonance - High Hopes",
      "Kyoto Twilight - Ambient Dreams",
      "Pixel Groove - Electronic Waves",
      "Midnight Echoes - Lofi Beats"
    )
    val next = tracks[(tracks.indexOf(_currentTrack.value) + 1) % tracks.size]
    _currentTrack.value = next
    _isPlaying.value = true
  }

  fun toggleIsland() {
    _islandMode.value = if (_islandMode.value == IslandMode.COLLAPSED) {
      IslandMode.MUSIC_ACTIVE
    } else {
      IslandMode.COLLAPSED
    }
  }

  fun collapseIsland() {
    _islandMode.value = IslandMode.COLLAPSED
  }

  // App launch overlay
  fun launchApp(app: AppItem) {
    _activeApp.value = app
    _isDrawerOpen.value = false
    collapseIsland()
  }

  fun dismissActiveApp() {
    _activeApp.value = null
  }

  // Drawers
  fun openDrawer() {
    _isDrawerOpen.value = true
    _isEditMode.value = false
    collapseIsland()
  }

  fun closeDrawer() {
    _isDrawerOpen.value = false
  }

  fun openCustomization() {
    _isCustomizationOpen.value = true
    _isDrawerOpen.value = false
    _isGeminiSheetOpen.value = false
  }

  fun closeCustomization() {
    _isCustomizationOpen.value = false
  }

  fun openGeminiSheet() {
    _isGeminiSheetOpen.value = true
    _isDrawerOpen.value = false
    _isCustomizationOpen.value = false
    collapseIsland()
  }

  fun closeGeminiSheet() {
    _isGeminiSheetOpen.value = false
  }

  fun toggleEditMode() {
    _isEditMode.value = !_isEditMode.value
  }

  fun setEditMode(enabled: Boolean) {
    _isEditMode.value = enabled
  }

  // Widget management
  fun addWidget(type: WidgetType, size: WidgetSize) {
    val newWidget = WidgetItem(
      id = "w_${UUID.randomUUID().toString().take(8)}",
      type = type,
      size = size,
      cornerRadiusDp = 24,
      glassStyle = GlassStyle.FROSTED_GLASS,
      opacity = 0.90f
    )
    _widgets.value = _widgets.value + newWidget
  }

  fun removeWidget(id: String) {
    _widgets.value = _widgets.value.filterNot { it.id == id }
  }

  fun cycleWidgetSize(id: String) {
    _widgets.value = _widgets.value.map { w ->
      if (w.id == id) {
        val nextSize = when (w.size) {
          WidgetSize.COMPACT -> WidgetSize.MEDIUM
          WidgetSize.MEDIUM -> WidgetSize.EXPANDED
          WidgetSize.EXPANDED -> WidgetSize.COMPACT
        }
        w.copy(size = nextSize)
      } else {
        w
      }
    }
  }

  fun updateWidgetStyle(id: String, glassStyle: GlassStyle, cornerRadiusDp: Int, opacity: Float) {
    _widgets.value = _widgets.value.map { w ->
      if (w.id == id) {
        w.copy(glassStyle = glassStyle, cornerRadiusDp = cornerRadiusDp, opacity = opacity)
      } else {
        w
      }
    }
  }

  // Theme selection
  fun selectTheme(theme: WallpaperTheme) {
    _currentTheme.value = theme
  }

  // Location & Weather selection
  fun openLocationPicker() {
    _isLocationPickerOpen.value = true
  }

  fun closeLocationPicker() {
    _isLocationPickerOpen.value = false
  }

  fun selectLocation(loc: WeatherLocation) {
    _currentLocation.value = loc
    _isLocationPickerOpen.value = false
  }

  fun cycleNextLocation() {
    val list = LauncherDefaults.defaultLocations
    val currentIndex = list.indexOfFirst { it.city == _currentLocation.value.city }
    val nextIndex = if (currentIndex >= 0) (currentIndex + 1) % list.size else 0
    _currentLocation.value = list[nextIndex]
  }

  fun setCustomLocation(cityName: String) {
    val clean = cityName.trim()
    if (clean.isBlank()) return
    val existing = availableLocations.find { it.city.equals(clean, ignoreCase = true) }
    if (existing != null) {
      _currentLocation.value = existing
    } else {
      val hash = kotlin.math.abs(clean.hashCode())
      val temp = 14 + (hash % 18) // 14°C to 31°C
      val conditions = listOf("Clear Skies", "Partly Cloudy", "Mild Breeze", "Golden Sunlight", "Scattered Clouds")
      val condition = conditions[hash % conditions.size]
      val icon = if (condition.contains("Clear") || condition.contains("Sun")) "wb_sunny" else "wb_cloudy"
      _currentLocation.value = WeatherLocation(
        city = clean.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
        tempCelsius = temp,
        condition = condition,
        icon = icon
      )
    }
    _isLocationPickerOpen.value = false
  }

  // AR Assistant Adaptation
  fun requestGeminiAdaptation(prompt: String) {
    viewModelScope.launch {
      _isGeminiLoading.value = true
      try {
        val result = geminiService.adaptLauncher(prompt)
        _geminiResult.value = result
      } catch (_: Exception) {
        _geminiResult.value = geminiService.generateHeuristicAdaptation(prompt)
      } finally {
        _isGeminiLoading.value = false
      }
    }
  }

  fun applyGeminiAdaptation(result: GeminiAdaptationResult) {
    // 1. Update greeting
    _geminiGreeting.value = "✨ ${result.summaryGreeting}"

    // 2. Update theme
    val foundTheme = LauncherDefaults.defaultWallpapers.find { it.id == result.recommendedThemeId }
    if (foundTheme != null) {
      _currentTheme.value = foundTheme
    }

    // 3. Update widgets
    val newWidgetList = result.recommendedWidgets.mapIndexed { idx, type ->
      WidgetItem(
        id = "ar_w_$idx",
        type = type,
        size = if (idx == 0 || idx == 1) WidgetSize.MEDIUM else WidgetSize.COMPACT,
        cornerRadiusDp = 26,
        glassStyle = GlassStyle.PIXEL_TINT,
        opacity = 0.92f
      )
    }
    _widgets.value = newWidgetList

    // 4. Update dock apps
    if (result.recommendedDockAppIds.isNotEmpty()) {
      _dockAppIds.value = result.recommendedDockAppIds
    }

    // Close sheet
    _isGeminiSheetOpen.value = false
  }
}
