package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.IslandMode

@Composable
fun DynamicIslandCapsule(
  mode: IslandMode,
  isPlaying: Boolean,
  currentTrack: String,
  geminiStatus: String,
  onTogglePlay: () -> Unit,
  onCapsuleClick: () -> Unit,
  onOpenGemini: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isExpanded = mode != IslandMode.COLLAPSED

  // iOS 26 fluid spring dimensions animation
  val animatedWidth by animateFloatAsState(
    targetValue = if (isExpanded) 360f else 170f,
    animationSpec = spring(dampingRatio = 0.72f, stiffness = 380f),
    label = "island_width"
  )

  val animatedHeight by animateFloatAsState(
    targetValue = if (isExpanded) 140f else 38f,
    animationSpec = spring(dampingRatio = 0.72f, stiffness = 380f),
    label = "island_height"
  )

  val cornerRadius by animateFloatAsState(
    targetValue = if (isExpanded) 28f else 19f,
    animationSpec = spring(dampingRatio = 0.75f, stiffness = 400f),
    label = "island_corner"
  )

  Box(
    modifier = modifier
      .width(animatedWidth.dp)
      .height(animatedHeight.dp)
      .shadow(16.dp, RoundedCornerShape(cornerRadius.dp), spotColor = Color(0x80000000))
      .clip(RoundedCornerShape(cornerRadius.dp))
      .background(Color(0xE60A0C14))
      .border(
        1.dp,
        androidx.compose.ui.graphics.Brush.linearGradient(
          listOf(Color(0x60FFFFFF), Color(0x12FFFFFF), Color(0x30FFFFFF))
        ),
        RoundedCornerShape(cornerRadius.dp)
      )
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onCapsuleClick
      )
      .testTag("dynamic_island_capsule")
      .padding(horizontal = 14.dp, vertical = 6.dp),
    contentAlignment = Alignment.Center
  ) {
    if (!isExpanded) {
      // Collapsed Pill: Mini Waveform + Music or Gemini pill
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
              .background(Color(0xFF22293D)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.MusicNote,
              contentDescription = "Music Playing",
              tint = Color(0xFF8AB4F8),
              modifier = Modifier.size(14.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "PILOS Island",
            color = Color(0xFFF1F5F9),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
          )
        }

        // Mini animated jumping waveform
        MiniAnimatedWaveform(isPlaying = isPlaying)
      }
    } else {
      // Expanded iOS 26 Fluid Activity Card
      Column(
        modifier = Modifier.fillMaxWidth(),
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
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF1E293B)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.GraphicEq,
                contentDescription = "Active Media",
                tint = Color(0xFF8AB4F8),
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = currentTrack,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = "Spatial Audio · iOS 26 Flow",
                color = Color(0xFF94A3B8),
                fontSize = 11.sp
              )
            }
          }

          // Gemini quick shortcut pill
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(14.dp))
              .background(Color(0xFF2E1065))
              .clickable { onOpenGemini() }
              .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "Gemini",
                tint = Color(0xFFD8B4FE),
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "AR Assistant",
                color = Color(0xFFE9D5FF),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        // Progress bar
        LinearProgressIndicator(
          progress = { 0.45f },
          modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(CircleShape),
          color = Color(0xFF8AB4F8),
          trackColor = Color(0xFF334155),
        )

        // Playback Controls
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
            Icon(
              imageVector = Icons.Default.FastRewind,
              contentDescription = "Previous Track",
              tint = Color(0xFFCBD5E1),
              modifier = Modifier.size(18.dp)
            )
          }

          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(Color(0xFF8AB4F8))
              .clickable { onTogglePlay() },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = "Toggle Play",
              tint = Color(0xFF0F172A),
              modifier = Modifier.size(22.dp)
            )
          }

          IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
            Icon(
              imageVector = Icons.Default.FastForward,
              contentDescription = "Next Track",
              tint = Color(0xFFCBD5E1),
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
fun MiniAnimatedWaveform(isPlaying: Boolean) {
  val transition = rememberInfiniteTransition(label = "waveform")
  val bar1 by transition.animateFloat(
    initialValue = 4f,
    targetValue = if (isPlaying) 14f else 4f,
    animationSpec = infiniteRepeatable(tween(350, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "bar1"
  )
  val bar2 by transition.animateFloat(
    initialValue = 14f,
    targetValue = if (isPlaying) 6f else 6f,
    animationSpec = infiniteRepeatable(tween(420, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "bar2"
  )
  val bar3 by transition.animateFloat(
    initialValue = 8f,
    targetValue = if (isPlaying) 16f else 4f,
    animationSpec = infiniteRepeatable(tween(380, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "bar3"
  )

  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(2.5.dp)
  ) {
    Box(
      modifier = Modifier
        .width(2.5.dp)
        .height(bar1.dp)
        .clip(CircleShape)
        .background(Color(0xFF34D399))
    )
    Box(
      modifier = Modifier
        .width(2.5.dp)
        .height(bar2.dp)
        .clip(CircleShape)
        .background(Color(0xFF38BDF8))
    )
    Box(
      modifier = Modifier
        .width(2.5.dp)
        .height(bar3.dp)
        .clip(CircleShape)
        .background(Color(0xFF818CF8))
    )
  }
}
