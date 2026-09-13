package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppItem

@Composable
fun AppDrawerSheet(
  apps: List<AppItem>,
  isOpen: Boolean,
  onClose: () -> Unit,
  onLaunchApp: (AppItem) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("All") }

  val categories = listOf("All", "Daily", "AI & AR", "Productivity", "Media", "Tools", "System")

  val filteredApps = apps.filter { app ->
    val matchesSearch = searchQuery.isBlank() ||
      app.name.contains(searchQuery, ignoreCase = true) ||
      app.category.contains(searchQuery, ignoreCase = true)

    val matchesCategory = selectedCategory == "All" ||
      app.category.equals(selectedCategory, ignoreCase = true) ||
      (selectedCategory == "AI & AR" && (app.category.contains("AI") || app.category.contains("AR")))

    matchesSearch && matchesCategory
  }

  // iOS 26 fluid spring entrance
  AnimatedVisibility(
    visible = isOpen,
    enter = slideInVertically(
      initialOffsetY = { it },
      animationSpec = spring(
        dampingRatio = 0.82f,
        stiffness = 360f
      )
    ) + fadeIn(
      animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    ),
    exit = slideOutVertically(
      targetOffsetY = { it },
      animationSpec = spring(
        dampingRatio = 0.86f,
        stiffness = 420f
      )
    ) + fadeOut()
  ) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .background(Color(0xCC050811))
        .clickable(enabled = false) {}
        .testTag("app_drawer_sheet")
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(top = 40.dp)
          .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // iOS 26 Glass Pill Close Handle
        Box(
          modifier = Modifier
            .size(width = 48.dp, height = 5.dp)
            .clip(CircleShape)
            .background(Color(0x80FFFFFF))
            .clickable { onClose() }
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Main Glass Body Container
        Box(
          modifier = Modifier
            .fillMaxSize()
            .shadow(24.dp, RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp), spotColor = Color(0x66000000))
            .clip(RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp))
            .background(
              Brush.verticalGradient(
                listOf(
                  Color(0xF0131B2E),
                  Color(0xF50B101D),
                  Color(0xFF070A14)
                )
              )
            )
            .border(
              1.dp,
              Brush.linearGradient(
                listOf(
                  Color(0x99FFFFFF),
                  Color(0x22FFFFFF),
                  Color(0x50FFFFFF)
                )
              ),
              RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)
            )
            .padding(top = 18.dp)
        ) {
          Column(modifier = Modifier.fillMaxSize()) {
            // Search Bar in App Drawer (iOS 26 Frosted Glass Capsule)
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                  .weight(1f)
                  .shadow(8.dp, RoundedCornerShape(26.dp), spotColor = Color(0x33000000))
                  .testTag("drawer_search_input"),
                placeholder = {
                  Text("Search apps, shortcuts & AR Assistant...", color = Color(0xFF64748B), fontSize = 14.sp)
                },
                leadingIcon = {
                  Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF38BDF8))
                },
                trailingIcon = {
                  if (searchQuery.isNotEmpty()) {
                    Icon(
                      Icons.Default.Close,
                      contentDescription = "Clear",
                      tint = Color(0xFF94A3B8),
                      modifier = Modifier.clickable { searchQuery = "" }
                    )
                  }
                },
                singleLine = true,
                shape = RoundedCornerShape(26.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedContainerColor = Color(0x401E293B),
                  unfocusedContainerColor = Color(0x2E1E293B),
                  focusedBorderColor = Color(0xFF38BDF8),
                  unfocusedBorderColor = Color(0x38FFFFFF),
                  focusedTextColor = Color.White,
                  unfocusedTextColor = Color.White
                )
              )

              Spacer(modifier = Modifier.width(10.dp))

              Box(
                modifier = Modifier
                  .size(46.dp)
                  .shadow(8.dp, CircleShape, spotColor = Color(0x33000000))
                  .clip(CircleShape)
                  .background(Color(0x33FFFFFF))
                  .border(1.dp, Color(0x44FFFFFF), CircleShape)
                  .clickable { onClose() },
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Close, contentDescription = "Close Drawer", tint = Color.White, modifier = Modifier.size(20.dp))
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // iOS 26 Glass Category Filter Chips
            LazyRow(
              modifier = Modifier.fillMaxWidth(),
              contentPadding = PaddingValues(horizontal = 20.dp),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              items(categories) { cat ->
                val isSelected = cat == selectedCategory
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                      if (isSelected) {
                        Brush.horizontalGradient(
                          listOf(Color(0xFF0284C7), Color(0xFF2563EB))
                        )
                      } else {
                        Brush.horizontalGradient(
                          listOf(Color(0x25FFFFFF), Color(0x15FFFFFF))
                        )
                      }
                    )
                    .border(
                      1.dp,
                      if (isSelected) Color(0x8038BDF8) else Color(0x22FFFFFF),
                      RoundedCornerShape(18.dp)
                    )
                    .clickable { selectedCategory = cat }
                    .testTag("drawer_cat_$cat")
                    .padding(horizontal = 15.dp, vertical = 7.dp)
                ) {
                  Text(
                    text = cat,
                    color = if (isSelected) Color.White else Color(0xFFE2E8F0),
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Categorized App Grid with smooth fluid item appearance
            LazyVerticalGrid(
              columns = GridCells.Fixed(4),
              modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
              contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
              verticalArrangement = Arrangement.spacedBy(22.dp),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              if (selectedCategory == "All" && searchQuery.isBlank()) {
                // Group by categorized sections for clarity
                val grouped = apps.groupBy { it.category }
                grouped.forEach { (categoryName, appList) ->
                  item(span = { GridItemSpan(4) }) {
                    Row(
                      modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Text(
                        text = categoryName.uppercase(),
                        color = Color(0xFF7DD3FC),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                      )
                      Spacer(modifier = Modifier.width(8.dp))
                      Box(
                        modifier = Modifier
                          .weight(1f)
                          .height(1.dp)
                          .background(Color(0x22FFFFFF))
                      )
                    }
                  }

                  items(appList) { app ->
                    AppIconItem(
                      app = app,
                      onClick = { onLaunchApp(app) }
                    )
                  }
                }
              } else {
                items(filteredApps) { app ->
                  AppIconItem(
                    app = app,
                    onClick = { onLaunchApp(app) }
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun AppIconItem(
  app: AppItem,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isPressed by remember { mutableStateOf(false) }
  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.88f else 1f,
    animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
    label = "icon_scale"
  )

  Column(
    modifier = modifier
      .graphicsLayer {
        scaleX = scale
        scaleY = scale
      }
      .pointerInput(Unit) {
        detectTapGestures(
          onPress = {
            isPressed = true
            tryAwaitRelease()
            isPressed = false
            onClick()
          }
        )
      }
      .testTag("app_icon_${app.id}")
      .padding(vertical = 4.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Pixel Squircle App Icon with iOS 26 glassy specular border
    Box(
      modifier = Modifier
        .size(58.dp)
        .shadow(8.dp, RoundedCornerShape(18.dp), spotColor = app.accentColor.copy(alpha = 0.45f))
        .clip(RoundedCornerShape(18.dp))
        .background(
          Brush.linearGradient(
            listOf(
              app.accentColor,
              app.accentColor.copy(alpha = 0.85f)
            )
          )
        )
        .border(
          1.dp,
          Brush.linearGradient(
            listOf(
              Color(0x80FFFFFF),
              Color(0x15FFFFFF),
              Color(0x35FFFFFF)
            )
          ),
          RoundedCornerShape(18.dp)
        ),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = getAppIconVector(app.iconSymbol),
        contentDescription = app.name,
        tint = Color.White,
        modifier = Modifier.size(28.dp)
      )
    }

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = app.name,
      color = Color(0xFFF1F5F9),
      fontSize = 12.sp,
      fontWeight = FontWeight.Medium,
      textAlign = TextAlign.Center,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier.width(72.dp)
    )
  }
}

fun getAppIconVector(symbol: String): ImageVector {
  return when (symbol) {
    "phone" -> Icons.Default.Phone
    "chat" -> Icons.Default.Chat
    "public" -> Icons.Default.Public
    "photo_camera" -> Icons.Default.PhotoCamera
    "auto_awesome" -> Icons.Default.AutoAwesome
    "collections" -> Icons.Default.Collections
    "settings" -> Icons.Default.Settings
    "music_note" -> Icons.Default.MusicNote
    "event" -> Icons.Default.Event
    "place" -> Icons.Default.Place
    "schedule" -> Icons.Default.Schedule
    "wb_sunny" -> Icons.Default.WbSunny
    "folder" -> Icons.Default.Folder
    "calculate" -> Icons.Default.Calculate
    "edit_note" -> Icons.Default.EditNote
    "favorite" -> Icons.Default.Favorite
    else -> Icons.Default.AutoAwesome
  }
}
