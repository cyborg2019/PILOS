package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PixelAtAGlance(
  weatherLocation: WeatherLocation = WeatherLocation("London", 19, "Partly Cloudy", "wb_cloudy"),
  geminiGreeting: String,
  onGlanceClick: () -> Unit,
  onLocationClick: () -> Unit,
  onAiSparkClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val dayFormat = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())
  val currentDateStr = dayFormat.format(Date())

  Column(
    modifier = modifier
      .fillMaxWidth()
      .testTag("pixel_at_a_glance")
      .padding(horizontal = 4.dp, vertical = 2.dp)
  ) {
    // Top line: Date and Weather Pill (Pixel signature + iOS 26 Glass)
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Start
    ) {
      Text(
        text = currentDateStr,
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.3).sp,
        modifier = Modifier.clickable { onGlanceClick() }
      )

      Spacer(modifier = Modifier.width(10.dp))

      // iOS 26 Glass Weather & Location Pill (Celsius)
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .shadow(6.dp, RoundedCornerShape(16.dp), spotColor = Color(0x33000000))
          .clip(RoundedCornerShape(16.dp))
          .background(Color(0x38FFFFFF))
          .border(
            1.dp,
            Brush.linearGradient(
              listOf(
                Color(0x80FFFFFF),
                Color(0x20FFFFFF),
                Color(0x40FFFFFF)
              )
            ),
            RoundedCornerShape(16.dp)
          )
          .clickable { onLocationClick() }
          .testTag("weather_location_pill")
          .padding(horizontal = 10.dp, vertical = 5.dp)
      ) {
        Icon(
          imageVector = if (weatherLocation.icon.contains("cloud") || weatherLocation.condition.contains("Cloud")) Icons.Default.Cloud else Icons.Default.WbSunny,
          contentDescription = "Weather",
          tint = if (weatherLocation.condition.contains("Sunny") || weatherLocation.condition.contains("Clear")) Color(0xFFFDE047) else Color(0xFF93C5FD),
          modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
          text = "${weatherLocation.tempCelsius}°C",
          color = Color.White,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "•",
          color = Color(0x80FFFFFF),
          fontSize = 11.sp
        )
        Spacer(modifier = Modifier.width(6.dp))
        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = "Location",
          tint = Color(0xFFE2E8F0),
          modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
          text = weatherLocation.city,
          color = Color(0xFFF1F5F9),
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium
        )
      }
    }

    Spacer(modifier = Modifier.height(7.dp))

    // Second line: AR Assistant Live Context Pill (iOS 26 Liquid Glass)
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .shadow(6.dp, RoundedCornerShape(18.dp), spotColor = Color(0x447E22CE))
        .clip(RoundedCornerShape(18.dp))
        .background(Color(0x3D2E1065))
        .border(
          1.dp,
          Brush.linearGradient(
            listOf(
              Color(0x88C084FC),
              Color(0x22C084FC),
              Color(0x55C084FC)
            )
          ),
          RoundedCornerShape(18.dp)
        )
        .clickable { onAiSparkClick() }
        .testTag("ar_assistant_glance_pill")
        .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
      Box(
        modifier = Modifier
          .size(20.dp)
          .clip(CircleShape)
          .background(Color(0xFF9333EA)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.AutoAwesome,
          contentDescription = "AR Assistant",
          tint = Color(0xFFF5EEFD),
          modifier = Modifier.size(12.dp)
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = geminiGreeting,
        color = Color(0xFFEDE9FE),
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        maxLines = 1
      )
    }
  }
}
