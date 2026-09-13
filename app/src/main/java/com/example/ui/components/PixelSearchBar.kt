package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PixelSearchBar(
  onSearchClick: () -> Unit,
  onGeminiSparkClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(52.dp)
      .shadow(12.dp, RoundedCornerShape(26.dp), spotColor = Color(0x66000000))
      .clip(RoundedCornerShape(26.dp))
      .background(
        Brush.horizontalGradient(
          listOf(Color(0xCC1E293B), Color(0xB30F172A))
        )
      )
      .border(
        1.dp,
        Brush.linearGradient(
          listOf(Color(0x80FFFFFF), Color(0x20FFFFFF), Color(0x40FFFFFF))
        ),
        RoundedCornerShape(26.dp)
      )
      .clickable { onSearchClick() }
      .testTag("pixel_search_bar")
      .padding(horizontal = 14.dp),
    contentAlignment = Alignment.CenterStart
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Left: Pixel Google 'G' Mark
      Row(verticalAlignment = Alignment.CenterVertically) {
        GoogleGLogo(modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(
          text = "Search apps or ask AR Assistant...",
          color = Color(0xFF94A3B8),
          fontSize = 14.sp,
          fontWeight = FontWeight.Normal
        )
      }

      // Right: Mic, Lens & AR Assistant Pill
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Mic,
          contentDescription = "Voice Search",
          tint = Color(0xFF38BDF8),
          modifier = Modifier.size(20.dp)
        )

        Icon(
          imageVector = Icons.Default.CenterFocusStrong,
          contentDescription = "Google Lens",
          tint = Color(0xFF38BDF8),
          modifier = Modifier.size(20.dp)
        )

        // AR Assistant Sparkle Pill
        Box(
          modifier = Modifier
            .size(32.dp)
            .shadow(6.dp, CircleShape, spotColor = Color(0x669333EA))
            .clip(CircleShape)
            .background(
              Brush.linearGradient(
                listOf(Color(0xFF9333EA), Color(0xFF38BDF8))
              )
            )
            .clickable { onGeminiSparkClick() }
            .testTag("search_gemini_sparkle"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = "Ask AR Assistant",
            tint = Color(0xFFF3E8FF),
            modifier = Modifier.size(17.dp)
          )
        }
      }
    }
  }
}

@Composable
fun GoogleGLogo(modifier: Modifier = Modifier) {
  Canvas(modifier = modifier) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val radius = size.minDimension / 2f
    val strokeWidth = radius * 0.42f

    drawArc(
      color = Color(0xFFEA4335),
      startAngle = 180f,
      sweepAngle = 100f,
      useCenter = false,
      style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
    )
    drawArc(
      color = Color(0xFFFBBC05),
      startAngle = 100f,
      sweepAngle = 80f,
      useCenter = false,
      style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
    )
    drawArc(
      color = Color(0xFF34A853),
      startAngle = 0f,
      sweepAngle = 100f,
      useCenter = false,
      style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
    )
    drawArc(
      color = Color(0xFF4285F4),
      startAngle = 310f,
      sweepAngle = 60f,
      useCenter = false,
      style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
    )

    // Horizontal bar of G
    drawLine(
      color = Color(0xFF4285F4),
      start = Offset(center.x, center.y),
      end = Offset(size.width * 0.92f, center.y),
      strokeWidth = strokeWidth
    )
  }
}
