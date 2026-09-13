package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoNotDisturbOn
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GlassStyle
import com.example.model.WidgetItem
import com.example.model.WidgetSize
import com.example.model.WidgetType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WidgetContainer(
  widget: WidgetItem,
  isEditMode: Boolean,
  primaryAccent: Color,
  onDelete: () -> Unit,
  onCycleSize: () -> Unit,
  onConfigureStyle: () -> Unit,
  onLongClick: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  // iOS 26 style subtle jiggle when in edit mode
  val jiggleTransition = rememberInfiniteTransition(label = "jiggle")
  val jiggleRotation by jiggleTransition.animateFloat(
    initialValue = if (isEditMode) -1.2f else 0f,
    targetValue = if (isEditMode) 1.2f else 0f,
    animationSpec = infiniteRepeatable(
      animation = tween(140, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "jiggle_rot"
  )

  val heightDp = when (widget.size) {
    WidgetSize.COMPACT -> 155.dp
    WidgetSize.MEDIUM -> 155.dp
    WidgetSize.EXPANDED -> 320.dp
  }

  val shape = RoundedCornerShape(widget.cornerRadiusDp.dp)

  val backgroundModifier = when (widget.glassStyle) {
    GlassStyle.FROSTED_GLASS -> Modifier
      .background(
        Brush.verticalGradient(
          listOf(
            Color(0x551E293B).copy(alpha = widget.opacity),
            Color(0x330F172A).copy(alpha = widget.opacity)
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
        shape
      )
    GlassStyle.OLED_DARK -> Modifier
      .background(Color(0xE605070D).copy(alpha = widget.opacity))
      .border(
        1.dp,
        Brush.linearGradient(listOf(Color(0x35FFFFFF), Color(0x10FFFFFF))),
        shape
      )
    GlassStyle.PIXEL_TINT -> Modifier
      .background(
        Brush.verticalGradient(
          listOf(
            primaryAccent.copy(alpha = 0.28f * widget.opacity),
            primaryAccent.copy(alpha = 0.12f * widget.opacity)
          )
        )
      )
      .border(
        1.5.dp,
        Brush.linearGradient(
          listOf(primaryAccent.copy(alpha = 0.70f), primaryAccent.copy(alpha = 0.30f))
        ),
        shape
      )
    GlassStyle.ULTRA_CLEAR -> Modifier
      .background(Color(0x20000000).copy(alpha = widget.opacity))
      .border(
        1.dp,
        Brush.linearGradient(listOf(Color(0x50FFFFFF), Color(0x15FFFFFF))),
        shape
      )
  }

  Box(
    modifier = modifier
      .rotate(if (isEditMode) jiggleRotation else 0f)
      .height(heightDp)
      .shadow(8.dp, shape, spotColor = Color(0x40000000))
      .clip(shape)
      .then(backgroundModifier)
      .pointerInput(isEditMode) {
        detectTapGestures(
          onLongPress = { onLongClick() }
        )
      }
      .testTag("widget_container_${widget.id}")
      .padding(14.dp)
  ) {
    content()

    // Edit Mode Badges (iOS 26 Style floating controls)
    if (isEditMode) {
      // Delete Badge (Top-Left)
      Box(
        modifier = Modifier
          .align(Alignment.TopStart)
          .size(26.dp)
          .clip(CircleShape)
          .background(Color(0xFFEF4444))
          .clickable { onDelete() }
          .testTag("delete_widget_${widget.id}"),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Close,
          contentDescription = "Delete Widget",
          tint = Color.White,
          modifier = Modifier.size(16.dp)
        )
      }

      // Action Controls (Top-Right: Size cycle + Configure style)
      Row(
        modifier = Modifier.align(Alignment.TopEnd),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Box(
          modifier = Modifier
            .size(26.dp)
            .clip(CircleShape)
            .background(Color(0xFF3B82F6))
            .clickable { onCycleSize() }
            .testTag("cycle_size_${widget.id}"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.AspectRatio,
            contentDescription = "Cycle Size",
            tint = Color.White,
            modifier = Modifier.size(15.dp)
          )
        }

        Box(
          modifier = Modifier
            .size(26.dp)
            .clip(CircleShape)
            .background(Color(0xFF8B5CF6))
            .clickable { onConfigureStyle() }
            .testTag("style_widget_${widget.id}"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Tune,
            contentDescription = "Configure Widget",
            tint = Color.White,
            modifier = Modifier.size(15.dp)
          )
        }
      }
    }
  }
}

// -------------------------------------------------------------
// 1. Now Playing Widget (with jumping audio visualizer)
// -------------------------------------------------------------
@Composable
fun NowPlayingWidget(
  isPlaying: Boolean,
  currentTrack: String,
  onTogglePlay: () -> Unit,
  onNextTrack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val transition = rememberInfiniteTransition(label = "now_playing_bars")
  val b1 by transition.animateFloat(
    initialValue = 6f, targetValue = if (isPlaying) 26f else 6f,
    animationSpec = infiniteRepeatable(tween(310), RepeatMode.Reverse), label = "b1"
  )
  val b2 by transition.animateFloat(
    initialValue = 24f, targetValue = if (isPlaying) 8f else 6f,
    animationSpec = infiniteRepeatable(tween(420), RepeatMode.Reverse), label = "b2"
  )
  val b3 by transition.animateFloat(
    initialValue = 10f, targetValue = if (isPlaying) 28f else 6f,
    animationSpec = infiniteRepeatable(tween(290), RepeatMode.Reverse), label = "b3"
  )
  val b4 by transition.animateFloat(
    initialValue = 18f, targetValue = if (isPlaying) 12f else 6f,
    animationSpec = infiniteRepeatable(tween(370), RepeatMode.Reverse), label = "b4"
  )

  Column(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(Color(0xFF8AB4F8).copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.MusicNote,
            contentDescription = null,
            tint = Color(0xFF8AB4F8),
            modifier = Modifier.size(16.dp)
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "NOW PLAYING",
          color = Color(0xFF94A3B8),
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
      }

      // Animated equalizer bars
      Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier.height(28.dp)
      ) {
        listOf(b1, b2, b3, b4).forEach { heightVal ->
          Box(
            modifier = Modifier
              .width(3.dp)
              .height(heightVal.dp)
              .clip(CircleShape)
              .background(Color(0xFF8AB4F8))
          )
        }
      }
    }

    // Title & Artist
    Column {
      Text(
        text = currentTrack,
        color = Color.White,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Text(
        text = "Spatial Audio · High Res Lossless",
        color = Color(0xFF94A3B8),
        fontSize = 12.sp
      )
    }

    // Controls
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = if (isPlaying) "Streaming 24-bit" else "Paused",
        color = Color(0xFF64748B),
        fontSize = 11.sp
      )

      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0xFF8AB4F8))
            .clickable { onTogglePlay() },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = "Play/Pause",
            tint = Color(0xFF0F172A),
            modifier = Modifier.size(20.dp)
          )
        }

        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0x2BFFFFFF))
            .clickable { onNextTrack() },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.SkipNext,
            contentDescription = "Next Track",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}

// -------------------------------------------------------------
// 2. Battery & System Telemetry Widget (Canvas Circular Rings)
// -------------------------------------------------------------
@Composable
fun BatteryTelemetryWidget(
  batteryPct: Int = 88,
  ramPct: Int = 54,
  cpuTemp: Int = 38,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Memory,
          contentDescription = null,
          tint = Color(0xFF34D399),
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "TELEMETRY",
          color = Color(0xFF94A3B8),
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
      }
      Text(
        text = "${cpuTemp}°C",
        color = Color(0xFF34D399),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
      )
    }

    // Dual Gauges
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Battery Ring
      TelemetryRingGauge(
        percent = batteryPct,
        label = "BATTERY",
        ringColor = Color(0xFF34D399),
        icon = {
          Icon(
            imageVector = Icons.Default.BatteryChargingFull,
            contentDescription = null,
            tint = Color(0xFF34D399),
            modifier = Modifier.size(14.dp)
          )
        }
      )

      // RAM Ring
      TelemetryRingGauge(
        percent = ramPct,
        label = "RAM LOAD",
        ringColor = Color(0xFF38BDF8),
        icon = null
      )
    }

    Text(
      text = "All systems nominal · PILOS Kernel",
      color = Color(0xFF64748B),
      fontSize = 10.sp
    )
  }
}

@Composable
fun TelemetryRingGauge(
  percent: Int,
  label: String,
  ringColor: Color,
  icon: (@Composable () -> Unit)?
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Box(modifier = Modifier.size(60.dp), contentAlignment = Alignment.Center) {
      Canvas(modifier = Modifier.fillMaxSize()) {
        val strokeWidth = 5.dp.toPx()
        // Background track
        drawArc(
          color = Color(0x26FFFFFF),
          startAngle = 135f,
          sweepAngle = 270f,
          useCenter = false,
          style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
        // Active progress arc
        drawArc(
          color = ringColor,
          startAngle = 135f,
          sweepAngle = 270f * (percent / 100f),
          useCenter = false,
          style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
      }

      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (icon != null) {
          icon()
        }
        Text(
          text = "$percent%",
          color = Color.White,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    Text(
      text = label,
      color = Color(0xFF94A3B8),
      fontSize = 9.sp,
      fontWeight = FontWeight.SemiBold
    )
  }
}

// -------------------------------------------------------------
// 3. Pixel Bold Stacked Clock Widget
// -------------------------------------------------------------
@Composable
fun ClockWidget(modifier: Modifier = Modifier) {
  val hourFormat = SimpleDateFormat("HH", Locale.getDefault())
  val minFormat = SimpleDateFormat("mm", Locale.getDefault())
  val now = Date()

  val hourStr = hourFormat.format(now)
  val minStr = minFormat.format(now)

  val dayFormat = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())
  val dateStr = dayFormat.format(now)

  Column(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = dateStr.uppercase(),
      color = Color(0xFF8AB4F8),
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.sp
    )

    // Stacked Pixel Numerals
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Text(
        text = hourStr,
        color = Color.White,
        fontSize = 44.sp,
        fontWeight = FontWeight.Black,
        lineHeight = 44.sp
      )
      Text(
        text = ":",
        color = Color(0xFF8AB4F8),
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = minStr,
        color = Color(0xFFE2E8F0),
        fontSize = 44.sp,
        fontWeight = FontWeight.Black,
        lineHeight = 44.sp
      )
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = "Cupertino · +0h",
        color = Color(0xFF64748B),
        fontSize = 11.sp
      )
      Text(
        text = "Alarm 7:30 AM",
        color = Color(0xFF94A3B8),
        fontSize = 11.sp
      )
    }
  }
}

// -------------------------------------------------------------
// 4. AR Assistant Smart Stack Widget
// -------------------------------------------------------------
@Composable
fun ArSmartStackWidget(
  onActionClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val suggestions = listOf(
    "✨ AR Spatial Perception",
    "🚀 Enter Deep Focus Mode",
    "🕶️ Scan Environment Lens",
    "🎵 Spatial Acoustic Mix"
  )

  Column(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(Color(0xFF9333EA)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = Color(0xFFF3E8FF),
            modifier = Modifier.size(12.dp)
          )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "AR ASSISTANT STACK",
          color = Color(0xFFE9D5FF),
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
      }
      Text(
        text = "Spatial Hub",
        color = Color(0xFFC084FC),
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold
      )
    }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
      suggestions.take(2).forEach { prompt ->
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
              Brush.horizontalGradient(
                listOf(Color(0x553B0764), Color(0x351E1B4B))
              )
            )
            .border(
              1.dp,
              Brush.linearGradient(
                listOf(Color(0x66C084FC), Color(0x22C084FC))
              ),
              RoundedCornerShape(14.dp)
            )
            .clickable { onActionClick(prompt) }
            .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
          Text(
            text = prompt,
            color = Color(0xFFF3E8FF),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }
    }

    Text(
      text = "Contextual recommendations powered by AR Assistant",
      color = Color(0xFF94A3B8),
      fontSize = 10.sp
    )
  }
}

@Composable
fun GeminiSmartStackWidget(
  onActionClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  ArSmartStackWidget(onActionClick = onActionClick, modifier = modifier)
}

// -------------------------------------------------------------
// 5. Quick Toggles / Control Center Widget
// -------------------------------------------------------------
@Composable
fun QuickTogglesWidget(modifier: Modifier = Modifier) {
  var wifiOn by remember { mutableStateOf(true) }
  var btOn by remember { mutableStateOf(true) }
  var dndOn by remember { mutableStateOf(false) }
  var torchOn by remember { mutableStateOf(false) }

  Column(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = "QUICK CONTROLS",
      color = Color(0xFF94A3B8),
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.sp
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceAround
    ) {
      QuickToggleItem(
        icon = Icons.Default.Wifi,
        label = "Wi-Fi",
        isActive = wifiOn,
        activeColor = Color(0xFF3B82F6),
        onClick = { wifiOn = !wifiOn }
      )

      QuickToggleItem(
        icon = Icons.Default.Bluetooth,
        label = "BT",
        isActive = btOn,
        activeColor = Color(0xFF6366F1),
        onClick = { btOn = !btOn }
      )

      QuickToggleItem(
        icon = Icons.Default.DoNotDisturbOn,
        label = "DND",
        isActive = dndOn,
        activeColor = Color(0xFFEF4444),
        onClick = { dndOn = !dndOn }
      )

      QuickToggleItem(
        icon = Icons.Default.FlashlightOn,
        label = "Torch",
        isActive = torchOn,
        activeColor = Color(0xFFEAB308),
        onClick = { torchOn = !torchOn }
      )
    }

    Text(
      text = "Pixel Control · iOS 26 Gestures",
      color = Color(0xFF64748B),
      fontSize = 10.sp
    )
  }
}

@Composable
fun QuickToggleItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  isActive: Boolean,
  activeColor: Color,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Box(
      modifier = Modifier
        .size(42.dp)
        .clip(CircleShape)
        .background(if (isActive) activeColor else Color(0x2BFFFFFF))
        .clickable { onClick() },
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = if (isActive) Color.White else Color(0xFF94A3B8),
        modifier = Modifier.size(20.dp)
      )
    }
    Text(
      text = label,
      color = if (isActive) Color.White else Color(0xFF94A3B8),
      fontSize = 10.sp,
      fontWeight = FontWeight.Medium
    )
  }
}

// -------------------------------------------------------------
// 6. Photo Memories Widget
// -------------------------------------------------------------
@Composable
fun PhotoMemoriesWidget(modifier: Modifier = Modifier) {
  var photoIndex by remember { mutableIntStateOf(0) }
  val photoCaptions = listOf(
    "Tokyo Horizon · Golden Hour" to listOf(Color(0xFFE11D48), Color(0xFF4F46E5)),
    "Nordic Fjords · Morning Mist" to listOf(Color(0xFF0D9488), Color(0xFF1E3A8A)),
    "Pacific Coastline · Twilight" to listOf(Color(0xFFEA580C), Color(0xFF7C2D12))
  )

  val current = photoCaptions[photoIndex % photoCaptions.size]

  Box(
    modifier = modifier
      .fillMaxSize()
      .clip(RoundedCornerShape(18.dp))
      .background(Brush.linearGradient(current.second))
      .clickable { photoIndex++ }
      .padding(12.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = "MEMORIES",
        color = Color(0xCCFFFFFF),
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )

      Column {
        Text(
          text = current.first,
          color = Color.White,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold
        )
        Text(
          text = "Tap to switch frame",
          color = Color(0xB3FFFFFF),
          fontSize = 10.sp
        )
      }
    }
  }
}
