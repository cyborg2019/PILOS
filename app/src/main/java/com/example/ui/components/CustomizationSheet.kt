package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LauncherDefaults
import com.example.model.GlassStyle
import com.example.model.WallpaperTheme
import com.example.model.WidgetItem
import com.example.model.WidgetSize
import com.example.model.WidgetType

@Composable
fun CustomizationSheet(
  isOpen: Boolean,
  currentWidgets: List<WidgetItem>,
  currentTheme: WallpaperTheme,
  onClose: () -> Unit,
  onAddWidget: (WidgetType, WidgetSize) -> Unit,
  onRemoveWidget: (String) -> Unit,
  onUpdateWidgetStyle: (String, GlassStyle, Int, Float) -> Unit,
  onSelectTheme: (WallpaperTheme) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("Widgets", "Appearance", "Wallpapers")

  // Global styling state
  var globalCornerRadius by remember { mutableFloatStateOf(24f) }
  var globalOpacity by remember { mutableFloatStateOf(0.90f) }
  var globalGlassStyle by remember { mutableStateOf(GlassStyle.FROSTED_GLASS) }

  AnimatedVisibility(
    visible = isOpen,
    enter = slideInVertically(initialOffsetY = { it }),
    exit = slideOutVertically(targetOffsetY = { it })
  ) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .background(Color(0xD9090B12))
        .clickable(enabled = false) {}
        .testTag("customization_sheet")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .fillMaxHeight(0.88f)
          .align(Alignment.BottomCenter)
          .shadow(16.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
          .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
          .background(Color(0xFF141826))
          .navigationBarsPadding()
          .padding(top = 16.dp)
      ) {
        // Drag Handle
        Box(
          modifier = Modifier
            .size(width = 40.dp, height = 4.dp)
            .clip(CircleShape)
            .background(Color(0x4DFFFFFF))
            .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Header
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Tune, contentDescription = null, tint = Color(0xFF8AB4F8))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "PILOS Customization",
              color = Color.White,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold
            )
          }

          IconButton(onClick = onClose, modifier = Modifier.testTag("close_customize_btn")) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF94A3B8))
          }
        }

        // Tabs
        TabRow(
          selectedTabIndex = selectedTab,
          containerColor = Color(0xFF141826),
          contentColor = Color(0xFF8AB4F8),
          indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
              Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
              color = Color(0xFF8AB4F8)
            )
          },
          modifier = Modifier.padding(horizontal = 16.dp)
        ) {
          tabs.forEachIndexed { index, title ->
            Tab(
              selected = selectedTab == index,
              onClick = { selectedTab = index },
              text = {
                Text(
                  text = title,
                  color = if (selectedTab == index) Color.White else Color(0xFF94A3B8),
                  fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                )
              }
            )
          }
        }

        // Tab Content
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(16.dp)
        ) {
          when (selectedTab) {
            0 -> WidgetsTabContent(
              currentWidgets = currentWidgets,
              onAddWidget = onAddWidget,
              onRemoveWidget = onRemoveWidget
            )
            1 -> AppearanceTabContent(
              cornerRadius = globalCornerRadius,
              opacity = globalOpacity,
              glassStyle = globalGlassStyle,
              onCornerRadiusChange = {
                globalCornerRadius = it
                currentWidgets.forEach { w ->
                  onUpdateWidgetStyle(w.id, globalGlassStyle, it.toInt(), globalOpacity)
                }
              },
              onOpacityChange = {
                globalOpacity = it
                currentWidgets.forEach { w ->
                  onUpdateWidgetStyle(w.id, globalGlassStyle, globalCornerRadius.toInt(), it)
                }
              },
              onGlassStyleChange = {
                globalGlassStyle = it
                currentWidgets.forEach { w ->
                  onUpdateWidgetStyle(w.id, it, globalCornerRadius.toInt(), globalOpacity)
                }
              }
            )
            2 -> WallpapersTabContent(
              currentTheme = currentTheme,
              onSelectTheme = onSelectTheme
            )
          }
        }
      }
    }
  }
}

@Composable
fun WidgetsTabContent(
  currentWidgets: List<WidgetItem>,
  onAddWidget: (WidgetType, WidgetSize) -> Unit,
  onRemoveWidget: (String) -> Unit
) {
  var selectedNewType by remember { mutableStateOf(WidgetType.NOW_PLAYING) }
  var selectedNewSize by remember { mutableStateOf(WidgetSize.MEDIUM) }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Text(
        text = "ACTIVE HOME WIDGETS (${currentWidgets.size})",
        color = Color(0xFF94A3B8),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )
    }

    items(currentWidgets) { widget ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(Color(0xFF1E2436))
          .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = widget.type.displayName,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
          )
          Text(
            text = "${widget.size.label} · ${widget.glassStyle.displayName}",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp
          )
        }

        IconButton(
          onClick = { onRemoveWidget(widget.id) },
          modifier = Modifier.size(32.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Remove",
            tint = Color(0xFFEF4444),
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = "ADD A NEW WIDGET",
        color = Color(0xFF8AB4F8),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )
    }

    // Grid of all available Widget Types
    items(WidgetType.values().toList()) { type ->
      val isSelected = type == selectedNewType
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(if (isSelected) Color(0xFF253352) else Color(0xFF1A1F30))
          .border(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) Color(0xFF8AB4F8) else Color(0x22FFFFFF),
            shape = RoundedCornerShape(14.dp)
          )
          .clickable { selectedNewType = type }
          .padding(12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = type.displayName,
              color = Color.White,
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = type.description,
              color = Color(0xFF94A3B8),
              fontSize = 11.sp
            )
          }

          if (isSelected) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              tint = Color(0xFF8AB4F8)
            )
          }
        }
      }
    }

    item {
      // Size Selector
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        WidgetSize.values().forEach { size ->
          val isSizeSelected = size == selectedNewSize
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSizeSelected) Color(0xFF8AB4F8) else Color(0xFF1E2436))
              .clickable { selectedNewSize = size }
              .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = size.name,
              color = if (isSizeSelected) Color(0xFF0F172A) else Color.White,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Button(
        onClick = { onAddWidget(selectedNewType, selectedNewSize) },
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("add_widget_confirm_btn"),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
        shape = RoundedCornerShape(14.dp)
      ) {
        Icon(Icons.Default.Add, contentDescription = null)
        Spacer(modifier = Modifier.width(6.dp))
        Text("Add to PILOS Home", fontWeight = FontWeight.Bold)
      }
    }
  }
}

@Composable
fun AppearanceTabContent(
  cornerRadius: Float,
  opacity: Float,
  glassStyle: GlassStyle,
  onCornerRadiusChange: (Float) -> Unit,
  onOpacityChange: (Float) -> Unit,
  onGlassStyleChange: (GlassStyle) -> Unit
) {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // Glass Style
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Text(
        text = "WIDGET GLASSMORPHISM STYLE",
        color = Color(0xFF94A3B8),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )

      GlassStyle.values().forEach { style ->
        val isSelected = style == glassStyle
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color(0xFF253352) else Color(0xFF1E2436))
            .clickable { onGlassStyleChange(style) }
            .padding(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = style.displayName, color = Color.White, fontWeight = FontWeight.Medium)
            if (isSelected) {
              Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF8AB4F8))
            }
          }
        }
      }
    }

    // Corner Radius Slider
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text("Corner Radius", color = Color.White, fontWeight = FontWeight.Medium)
        Text("${cornerRadius.toInt()} dp", color = Color(0xFF8AB4F8), fontWeight = FontWeight.Bold)
      }
      Slider(
        value = cornerRadius,
        onValueChange = onCornerRadiusChange,
        valueRange = 12f..36f,
        colors = SliderDefaults.colors(
          thumbColor = Color(0xFF8AB4F8),
          activeTrackColor = Color(0xFF8AB4F8),
          inactiveTrackColor = Color(0xFF334155)
        )
      )
    }

    // Opacity Slider
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text("Glass Transparency", color = Color.White, fontWeight = FontWeight.Medium)
        Text("${(opacity * 100).toInt()}%", color = Color(0xFF8AB4F8), fontWeight = FontWeight.Bold)
      }
      Slider(
        value = opacity,
        onValueChange = onOpacityChange,
        valueRange = 0.4f..1.0f,
        colors = SliderDefaults.colors(
          thumbColor = Color(0xFF8AB4F8),
          activeTrackColor = Color(0xFF8AB4F8),
          inactiveTrackColor = Color(0xFF334155)
        )
      )
    }
  }
}

@Composable
fun WallpapersTabContent(
  currentTheme: WallpaperTheme,
  onSelectTheme: (WallpaperTheme) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    items(LauncherDefaults.defaultWallpapers) { theme ->
      val isSelected = theme.id == currentTheme.id
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(84.dp)
          .clip(RoundedCornerShape(18.dp))
          .background(Brush.horizontalGradient(theme.backgroundGradient))
          .border(
            width = if (isSelected) 2.5.dp else 1.dp,
            color = if (isSelected) theme.primaryAccent else Color(0x33FFFFFF),
            shape = RoundedCornerShape(18.dp)
          )
          .clickable { onSelectTheme(theme) }
          .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(theme.primaryAccent)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = theme.name,
                color = if (theme.isDark) Color.White else Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = if (theme.isDark) "Pixel Dark Palette" else "Clean Light Palette",
                color = if (theme.isDark) Color(0xCCFFFFFF) else Color(0x99000000),
                fontSize = 12.sp
              )
            }
          }

          if (isSelected) {
            Box(
              modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(theme.primaryAccent),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
            }
          }
        }
      }
    }
  }
}
