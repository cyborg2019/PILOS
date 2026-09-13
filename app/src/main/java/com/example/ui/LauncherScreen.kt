package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.WidgetSize
import com.example.model.WidgetType
import com.example.ui.components.AppDrawerSheet
import com.example.ui.components.AppIconItem
import com.example.ui.components.AppLaunchOverlay
import com.example.ui.components.ArSmartStackWidget
import com.example.ui.components.BatteryTelemetryWidget
import com.example.ui.components.ClockWidget
import com.example.ui.components.CustomizationSheet
import com.example.ui.components.DynamicIslandCapsule
import com.example.ui.components.GeminiAdaptationSheet
import com.example.ui.components.LocationPickerSheet
import com.example.ui.components.NowPlayingWidget
import com.example.ui.components.PhotoMemoriesWidget
import com.example.ui.components.PixelAtAGlance
import com.example.ui.components.PixelSearchBar
import com.example.ui.components.QuickTogglesWidget
import com.example.ui.components.WidgetContainer

@Composable
fun LauncherScreen(
  viewModel: LauncherViewModel,
  modifier: Modifier = Modifier
) {
  val widgets by viewModel.widgets.collectAsState()
  val apps by viewModel.apps.collectAsState()
  val dockAppIds by viewModel.dockAppIds.collectAsState()
  val currentTheme by viewModel.currentTheme.collectAsState()
  val islandMode by viewModel.islandMode.collectAsState()
  val isPlaying by viewModel.isPlaying.collectAsState()
  val currentTrack by viewModel.currentTrack.collectAsState()
  val geminiGreeting by viewModel.geminiGreeting.collectAsState()
  val currentLocation by viewModel.currentLocation.collectAsState()
  val availableLocations = viewModel.availableLocations

  val activeApp by viewModel.activeApp.collectAsState()
  val isDrawerOpen by viewModel.isDrawerOpen.collectAsState()
  val isCustomizationOpen by viewModel.isCustomizationOpen.collectAsState()
  val isGeminiSheetOpen by viewModel.isGeminiSheetOpen.collectAsState()
  val isLocationPickerOpen by viewModel.isLocationPickerOpen.collectAsState()
  val isEditMode by viewModel.isEditMode.collectAsState()
  val isGeminiLoading by viewModel.isGeminiLoading.collectAsState()
  val geminiResult by viewModel.geminiResult.collectAsState()

  val dockApps = remember(dockAppIds, apps) {
    dockAppIds.mapNotNull { id -> apps.find { it.id == id } }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Brush.verticalGradient(currentTheme.backgroundGradient))
      .pointerInput(Unit) {
        detectTapGestures(
          onLongPress = { viewModel.toggleEditMode() },
          onTap = {
            if (isEditMode) viewModel.setEditMode(false)
            viewModel.collapseIsland()
          }
        )
      }
      .testTag("pilos_launcher_root")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // 1. Dynamic Island Capsule at Top Notch Area
      DynamicIslandCapsule(
        mode = islandMode,
        isPlaying = isPlaying,
        currentTrack = currentTrack,
        geminiStatus = geminiGreeting,
        onTogglePlay = { viewModel.togglePlay() },
        onCapsuleClick = { viewModel.toggleIsland() },
        onOpenGemini = { viewModel.openGeminiSheet() },
        modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)
      )

      // Edit Mode Floating Top Bar (iOS 26 Frosted Glass)
      AnimatedVisibility(visible = isEditMode) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xD9141824))
            .border(
              1.dp,
              Brush.linearGradient(listOf(Color(0x80FFFFFF), Color(0x20FFFFFF))),
              RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 14.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "PILOS Customization",
              color = Color.White,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF38BDF8))
                .clickable { viewModel.openCustomization() }
                .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF0F172A), modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add", color = Color(0xFF0F172A), fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF9333EA))
                .clickable { viewModel.openGeminiSheet() }
                .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("AR Remix", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF34D399))
                .clickable { viewModel.setEditMode(false) }
                .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
              Text("Done", color = Color(0xFF0F172A), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      // 2. Scrollable Home Area (At a Glance + Deep Customizable Widgets Grid)
      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Pixel Signature At a Glance (Spans full 2 columns, with Celsius weather and location picker)
        item(span = { GridItemSpan(2) }) {
          PixelAtAGlance(
            weatherLocation = currentLocation,
            geminiGreeting = geminiGreeting,
            onGlanceClick = { viewModel.openDrawer() },
            onLocationClick = { viewModel.openLocationPicker() },
            onAiSparkClick = { viewModel.openGeminiSheet() }
          )
        }

        // Dynamic Customizable Widgets
        items(
          items = widgets,
          key = { it.id },
          span = { widget ->
            val span = if (widget.size == WidgetSize.COMPACT) 1 else 2
            GridItemSpan(span)
          }
        ) { widget ->
          WidgetContainer(
            widget = widget,
            isEditMode = isEditMode,
            primaryAccent = currentTheme.primaryAccent,
            onDelete = { viewModel.removeWidget(widget.id) },
            onCycleSize = { viewModel.cycleWidgetSize(widget.id) },
            onConfigureStyle = { viewModel.openCustomization() },
            onLongClick = { viewModel.setEditMode(true) }
          ) {
            when (widget.type) {
              WidgetType.AT_A_GLANCE -> {
                PixelAtAGlance(
                  weatherLocation = currentLocation,
                  geminiGreeting = geminiGreeting,
                  onGlanceClick = { viewModel.openDrawer() },
                  onLocationClick = { viewModel.openLocationPicker() },
                  onAiSparkClick = { viewModel.openGeminiSheet() }
                )
              }
              WidgetType.NOW_PLAYING -> {
                NowPlayingWidget(
                  isPlaying = isPlaying,
                  currentTrack = currentTrack,
                  onTogglePlay = { viewModel.togglePlay() },
                  onNextTrack = { viewModel.nextTrack() }
                )
              }
              WidgetType.BATTERY_TELEMETRY -> {
                BatteryTelemetryWidget(
                  batteryPct = 88,
                  ramPct = 52,
                  cpuTemp = 37
                )
              }
              WidgetType.CLOCK -> {
                ClockWidget()
              }
              WidgetType.AR_SMART_STACK -> {
                ArSmartStackWidget(
                  onActionClick = { prompt ->
                    viewModel.requestGeminiAdaptation(prompt)
                    viewModel.openGeminiSheet()
                  }
                )
              }
              WidgetType.QUICK_TOGGLES -> {
                QuickTogglesWidget()
              }
              WidgetType.PHOTO_MEMORIES -> {
                PhotoMemoriesWidget()
              }
            }
          }
        }
      }

      // 3. Pixel Bottom Dock Area (iOS 26 Frosted Glass)
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Pixel Search Bar
        PixelSearchBar(
          onSearchClick = { viewModel.openDrawer() },
          onGeminiSparkClick = { viewModel.openGeminiSheet() }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 5 Favorite Dock Apps Row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          dockApps.forEach { app ->
            AppIconItem(
              app = app,
              onClick = { viewModel.launchApp(app) }
            )
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Home Navigation Gesture Bar (Tap or swipe up to open App Drawer)
        Box(
          modifier = Modifier
            .size(width = 120.dp, height = 5.dp)
            .clip(CircleShape)
            .background(Color(0x80FFFFFF))
            .clickable { viewModel.openDrawer() }
        )
      }
    }

    // 4. Overlays & Sheets
    // App Drawer Sheet (Smooth iOS 26 Entrance & Categorized Grid)
    AppDrawerSheet(
      apps = apps,
      isOpen = isDrawerOpen,
      onClose = { viewModel.closeDrawer() },
      onLaunchApp = { app -> viewModel.launchApp(app) }
    )

    // iOS 26 Fluid App Launch Overlay
    AppLaunchOverlay(
      activeApp = activeApp,
      onDismiss = { viewModel.dismissActiveApp() },
      onOpenCustomization = {
        viewModel.dismissActiveApp()
        viewModel.openCustomization()
      }
    )

    // Deep Customization Dialog Sheet
    CustomizationSheet(
      isOpen = isCustomizationOpen,
      currentWidgets = widgets,
      currentTheme = currentTheme,
      onClose = { viewModel.closeCustomization() },
      onAddWidget = { type, size ->
        viewModel.addWidget(type, size)
      },
      onRemoveWidget = { id -> viewModel.removeWidget(id) },
      onUpdateWidgetStyle = { id, style, corner, opacity ->
        viewModel.updateWidgetStyle(id, style, corner, opacity)
      },
      onSelectTheme = { theme -> viewModel.selectTheme(theme) }
    )

    // AR Assistant Adaptation Sheet
    GeminiAdaptationSheet(
      isOpen = isGeminiSheetOpen,
      isLoading = isGeminiLoading,
      lastResult = geminiResult,
      onClose = { viewModel.closeGeminiSheet() },
      onRequestAdaptation = { prompt -> viewModel.requestGeminiAdaptation(prompt) },
      onApplyAdaptation = { result -> viewModel.applyGeminiAdaptation(result) }
    )

    // Location Picker Sheet for Celsius Weather
    LocationPickerSheet(
      isOpen = isLocationPickerOpen,
      currentLocation = currentLocation,
      availableLocations = availableLocations,
      onClose = { viewModel.closeLocationPicker() },
      onSelectLocation = { viewModel.selectLocation(it) },
      onCustomCitySubmit = { cityName ->
        viewModel.setCustomLocation(cityName)
      }
    )
  }
}
