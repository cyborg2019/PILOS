package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.WeatherLocation

@Composable
fun LocationPickerSheet(
  isOpen: Boolean,
  currentLocation: WeatherLocation,
  availableLocations: List<WeatherLocation>,
  onClose: () -> Unit,
  onSelectLocation: (WeatherLocation) -> Unit,
  onCustomCitySubmit: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }

  val filteredLocations = availableLocations.filter {
    searchQuery.isBlank() ||
      it.city.contains(searchQuery, ignoreCase = true) ||
      it.condition.contains(searchQuery, ignoreCase = true)
  }

  AnimatedVisibility(
    visible = isOpen,
    enter = slideInVertically(
      initialOffsetY = { it },
      animationSpec = spring(dampingRatio = 0.82f, stiffness = 380f)
    ),
    exit = slideOutVertically(
      targetOffsetY = { it },
      animationSpec = spring(dampingRatio = 0.85f, stiffness = 420f)
    )
  ) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .background(Color(0xCC080B14))
        .clickable(enabled = false) {}
        .testTag("location_picker_sheet")
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(top = 48.dp)
          .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Drag Pill Handle
        Box(
          modifier = Modifier
            .size(width = 44.dp, height = 5.dp)
            .clip(CircleShape)
            .background(Color(0x55FFFFFF))
            .clickable { onClose() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Card Container (iOS 26 Frosted Glass)
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(horizontal = 16.dp)
            .shadow(20.dp, RoundedCornerShape(28.dp), spotColor = Color(0x66000000))
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xEE161D2E))
            .border(
              1.dp,
              Brush.linearGradient(
                listOf(Color(0x80FFFFFF), Color(0x20FFFFFF), Color(0x40FFFFFF))
              ),
              RoundedCornerShape(28.dp)
            )
            .padding(20.dp)
        ) {
          Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0x3338BDF8)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF38BDF8),
                    modifier = Modifier.size(20.dp)
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = "Select Location",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "Weather & date temperature in Celsius (°C)",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                  )
                }
              }

              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(Color(0x25FFFFFF))
                  .clickable { onClose() },
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = "Close",
                  tint = Color.White,
                  modifier = Modifier.size(18.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Current Selected Card
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                  Brush.horizontalGradient(
                    listOf(Color(0x4D0284C7), Color(0x330369A1))
                  )
                )
                .border(1.dp, Color(0x5538BDF8), RoundedCornerShape(18.dp))
                .padding(14.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = "CURRENT LOCATION",
                    color = Color(0xFF7DD3FC),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                  )
                  Text(
                    text = currentLocation.city,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = currentLocation.condition,
                    color = Color(0xFFE2E8F0),
                    fontSize = 12.sp
                  )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = if (currentLocation.condition.contains("Sun") || currentLocation.condition.contains("Clear")) Icons.Default.WbSunny else Icons.Default.Cloud,
                    contentDescription = null,
                    tint = Color(0xFFFDE047),
                    modifier = Modifier.size(28.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "${currentLocation.tempCelsius}°C",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search / Custom Input Bar
            OutlinedTextField(
              value = searchQuery,
              onValueChange = { searchQuery = it },
              placeholder = {
                Text("Search or enter city (e.g. Rome, Tokyo)...", color = Color(0xFF64748B), fontSize = 13.sp)
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
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("location_search_input"),
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0x40111827),
                unfocusedContainerColor = Color(0x33111827),
                focusedBorderColor = Color(0xFF38BDF8),
                unfocusedBorderColor = Color(0x33FFFFFF),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
              )
            )

            if (searchQuery.isNotBlank() && filteredLocations.none { it.city.equals(searchQuery.trim(), ignoreCase = true) }) {
              Spacer(modifier = Modifier.height(8.dp))
              Button(
                onClick = {
                  onCustomCitySubmit(searchQuery.trim())
                  searchQuery = ""
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("submit_custom_city_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
              ) {
                Icon(Icons.Default.LocationCity, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Use \"${searchQuery.trim()}\" as Custom Location", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = "POPULAR CITIES",
              color = Color(0xFF94A3B8),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // List of Locations in Celsius
            LazyColumn(
              modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              items(filteredLocations) { loc ->
                val isSelected = loc.city == currentLocation.city
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) Color(0x400284C7) else Color(0x20FFFFFF))
                    .border(
                      1.dp,
                      if (isSelected) Color(0x8038BDF8) else Color(0x18FFFFFF),
                      RoundedCornerShape(16.dp)
                    )
                    .clickable { onSelectLocation(loc) }
                    .testTag("location_item_${loc.city.lowercase()}")
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = if (loc.condition.contains("Sun") || loc.condition.contains("Clear")) Icons.Default.WbSunny else Icons.Default.Cloud,
                      contentDescription = null,
                      tint = if (loc.condition.contains("Sun") || loc.condition.contains("Clear")) Color(0xFFFDE047) else Color(0xFF93C5FD),
                      modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                      Text(
                        text = loc.city,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                      )
                      Text(
                        text = loc.condition,
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp
                      )
                    }
                  }

                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = "${loc.tempCelsius}°C",
                      color = Color.White,
                      fontSize = 18.sp,
                      fontWeight = FontWeight.Bold
                    )
                    if (isSelected) {
                      Spacer(modifier = Modifier.width(8.dp))
                      Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(18.dp)
                      )
                    }
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}
