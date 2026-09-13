package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GeminiAdaptationResult

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GeminiAdaptationSheet(
  isOpen: Boolean,
  isLoading: Boolean,
  lastResult: GeminiAdaptationResult?,
  onClose: () -> Unit,
  onRequestAdaptation: (String) -> Unit,
  onApplyAdaptation: (GeminiAdaptationResult) -> Unit,
  modifier: Modifier = Modifier
) {
  var customInput by remember { mutableStateOf("") }

  val presets = listOf(
    "Deep Focus & Coding",
    "Evening Chill & Music",
    "Travel & Navigation",
    "Active Fitness Pulse",
    "Porcelain Minimalist"
  )

  // Glow sparkle rotation animation
  val infiniteTransition = rememberInfiniteTransition(label = "ar_glow")
  val rotation by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(tween(8000, easing = FastOutSlowInEasing)),
    label = "glow_rot"
  )

  AnimatedVisibility(
    visible = isOpen,
    enter = slideInVertically(initialOffsetY = { it }, animationSpec = spring(0.78f, 360f)),
    exit = slideOutVertically(targetOffsetY = { it }, animationSpec = spring(0.85f, 420f))
  ) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .background(Color(0xD9080511))
        .clickable(enabled = false) {}
        .testTag("gemini_adaptation_sheet")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .fillMaxHeight(0.88f)
          .align(Alignment.BottomCenter)
          .shadow(24.dp, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp), spotColor = Color(0x667E22CE))
          .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
          .background(
            Brush.verticalGradient(
              listOf(
                Color(0xF01A1230),
                Color(0xF5100922),
                Color(0xFF090514)
              )
            )
          )
          .border(
            1.dp,
            Brush.linearGradient(
              listOf(
                Color(0x99C084FC),
                Color(0x22C084FC),
                Color(0x55C084FC)
              )
            ),
            RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
          )
          .navigationBarsPadding()
          .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 12.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Drag Pill (iOS 26 Frosted)
        Box(
          modifier = Modifier
            .size(width = 44.dp, height = 5.dp)
            .clip(CircleShape)
            .background(Color(0x66FFFFFF))
            .align(Alignment.CenterHorizontally)
            .clickable { onClose() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Header with glowing AR Assistant badge
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(Color(0xFF9333EA), Color(0xFF38BDF8)))),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                  .size(20.dp)
                  .rotate(rotation)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "AR Assistant Adaptation",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Spatial intelligence tailored to your routine",
                color = Color(0xFFC084FC),
                fontSize = 12.sp
              )
            }
          }

          IconButton(onClick = onClose, modifier = Modifier.testTag("close_gemini_sheet")) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF94A3B8))
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "Select a lifestyle preset or describe your current context:",
          color = Color(0xFFE2E8F0),
          fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Preset Chips
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          presets.forEach { preset ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x353B0764))
                .border(1.dp, Color(0x55C084FC), RoundedCornerShape(16.dp))
                .clickable { onRequestAdaptation(preset) }
                .padding(horizontal = 13.dp, vertical = 8.dp)
            ) {
              Text(
                text = preset,
                color = Color(0xFFF3E8FF),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Custom Prompt Input
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = customInput,
            onValueChange = { customInput = it },
            placeholder = { Text("e.g. Late night study session, dark theme...", color = Color(0xFF64748B), fontSize = 13.sp) },
            modifier = Modifier
              .weight(1f)
              .testTag("gemini_prompt_input"),
            shape = RoundedCornerShape(22.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = Color(0x301E1736),
              unfocusedContainerColor = Color(0x201A1330),
              focusedBorderColor = Color(0xFFC084FC),
              unfocusedBorderColor = Color(0x33FFFFFF),
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White
            )
          )

          Spacer(modifier = Modifier.width(8.dp))

          Box(
            modifier = Modifier
              .size(48.dp)
              .shadow(6.dp, CircleShape, spotColor = Color(0x559333EA))
              .clip(CircleShape)
              .background(Color(0xFF9333EA))
              .clickable {
                if (customInput.isNotBlank()) {
                  onRequestAdaptation(customInput)
                  customInput = ""
                }
              }
              .testTag("gemini_prompt_submit"),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Loading indicator
        if (isLoading) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              CircularProgressIndicator(color = Color(0xFFC084FC))
              Spacer(modifier = Modifier.height(10.dp))
              Text("AR Assistant is tailoring your spatial launcher...", color = Color(0xFFE9D5FF), fontSize = 13.sp)
            }
          }
        }

        // Adaptation Result Preview
        if (lastResult != null && !isLoading) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(22.dp))
              .background(Color(0x552E1065))
              .border(
                1.5.dp,
                Brush.linearGradient(listOf(Color(0xFFA855F7), Color(0xFF38BDF8))),
                RoundedCornerShape(22.dp)
              )
              .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = lastResult.modeTitle,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
              )

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(Color(0xFF6B21A8))
                  .padding(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text("AR Assistant", color = Color(0xFFF3E8FF), fontSize = 10.sp, fontWeight = FontWeight.Bold)
              }
            }

            Text(
              text = "At-a-Glance: \"${lastResult.summaryGreeting}\"",
              color = Color(0xFFC084FC),
              fontSize = 13.sp,
              fontWeight = FontWeight.Medium
            )

            Text(
              text = lastResult.aiThoughtReasoning,
              color = Color(0xFFCBD5E1),
              fontSize = 12.sp,
              lineHeight = 16.sp
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Widgets, contentDescription = null, tint = Color(0xFF8AB4F8), modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Widgets: ${lastResult.recommendedWidgets.joinToString { it.displayName }}",
                color = Color(0xFF94A3B8),
                fontSize = 11.sp
              )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
              onClick = { onApplyAdaptation(lastResult) },
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("apply_gemini_layout_btn"),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9333EA)),
              shape = RoundedCornerShape(14.dp)
            ) {
              Icon(Icons.Default.Check, contentDescription = null)
              Spacer(modifier = Modifier.width(8.dp))
              Text("Apply AR Assistant Layout to PILOS", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}
