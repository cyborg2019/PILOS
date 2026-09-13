package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GeminiService
import com.example.model.AppItem
import kotlinx.coroutines.launch

@Composable
fun AppLaunchOverlay(
  activeApp: AppItem?,
  onDismiss: () -> Unit,
  onOpenCustomization: () -> Unit,
  modifier: Modifier = Modifier
) {
  AnimatedVisibility(
    visible = activeApp != null,
    enter = scaleIn(
      initialScale = 0.4f,
      animationSpec = spring(dampingRatio = 0.74f, stiffness = 380f)
    ) + fadeIn(animationSpec = spring(stiffness = 500f)),
    exit = scaleOut(
      targetScale = 0.45f,
      animationSpec = spring(dampingRatio = 0.85f, stiffness = 420f)
    ) + fadeOut()
  ) {
    if (activeApp != null) {
      Box(
        modifier = modifier
          .fillMaxSize()
          .background(Color(0xFF0C0E14))
          .statusBarsPadding()
          .navigationBarsPadding()
          .testTag("app_launch_overlay")
      ) {
        Column(modifier = Modifier.fillMaxSize()) {
          // App Top Navigation Bar
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .height(56.dp)
              .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              IconButton(onClick = onDismiss, modifier = Modifier.testTag("app_close_button")) {
                Icon(
                  imageVector = Icons.Default.ArrowBack,
                  contentDescription = "Back to Home",
                  tint = Color.White
                )
              }
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = activeApp.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(activeApp.accentColor),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = getAppIconVector(activeApp.iconSymbol),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
              )
            }
          }

          // Main App Content Window
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .weight(1f)
              .padding(horizontal = 16.dp, vertical = 8.dp)
              .clip(RoundedCornerShape(24.dp))
              .background(Color(0xFF141824))
          ) {
            when (activeApp.id) {
              "gemini", "ar_assistant" -> ArAssistantAppView()
              "camera" -> CameraAppView()
              "music" -> MusicPlayerAppView()
              "settings" -> SettingsAppView(onOpenCustomization = onOpenCustomization)
              "weather" -> WeatherAppView()
              else -> GenericAppView(app = activeApp)
            }
          }

          // iOS 26 Fluid Home Gesture Indicator Bar (swipe/tap to dismiss)
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(38.dp)
              .clickable { onDismiss() },
            contentAlignment = Alignment.Center
          ) {
            Box(
              modifier = Modifier
                .size(width = 135.dp, height = 5.dp)
                .clip(CircleShape)
                .background(Color(0x80FFFFFF))
            )
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// Interactive AR Assistant App View
// -------------------------------------------------------------
@Composable
fun ArAssistantAppView() {
  val messages = remember {
    mutableStateListOf(
      "AR Assistant" to "Greetings! I am your PILOS AR Assistant. Ask me anything, explore spatial scene perception, or tell me how to adapt your home screen widgets and mood."
    )
  }
  var promptInput by remember { mutableStateOf("") }
  var isLoading by remember { mutableStateOf(false) }
  val scope = rememberCoroutineScope()
  val geminiService = remember { GeminiService() }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(bottom = 12.dp)
    ) {
      Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFFC084FC))
      Spacer(modifier = Modifier.width(8.dp))
      Text("PILOS AR Spatial Engine", color = Color(0xFFE9D5FF), fontWeight = FontWeight.Bold)
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(messages) { (sender, text) ->
        val isUser = sender == "You"
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(16.dp))
              .background(if (isUser) Color(0xFF4338CA) else Color(0xFF1E2436))
              .padding(12.dp)
              .fillMaxWidth(0.85f)
          ) {
            Column {
              Text(
                text = sender,
                color = if (isUser) Color(0xFFA5B4FC) else Color(0xFFC084FC),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(3.dp))
              Text(text = text, color = Color.White, fontSize = 13.sp)
            }
          }
        }
      }

      if (isLoading) {
        item {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
          ) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color(0xFFC084FC))
            Spacer(modifier = Modifier.width(8.dp))
            Text("AR Assistant is analyzing...", color = Color(0xFF94A3B8), fontSize = 12.sp)
          }
        }
      }
    }

    // Input Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = promptInput,
        onValueChange = { promptInput = it },
        placeholder = { Text("Ask AR Assistant...", color = Color(0xFF64748B), fontSize = 13.sp) },
        modifier = Modifier
          .weight(1f)
          .testTag("gemini_chat_input"),
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = Color(0xFF0F121C),
          unfocusedContainerColor = Color(0xFF0F121C),
          focusedBorderColor = Color(0xFFC084FC),
          unfocusedBorderColor = Color(0x33FFFFFF),
          focusedTextColor = Color.White,
          unfocusedTextColor = Color.White
        )
      )

      Spacer(modifier = Modifier.width(8.dp))

      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(Color(0xFF9333EA))
          .clickable {
            if (promptInput.isNotBlank()) {
              val currentPrompt = promptInput
              messages.add("You" to currentPrompt)
              promptInput = ""
              isLoading = true
              scope.launch {
                val adaptation = geminiService.adaptLauncher(currentPrompt)
                messages.add("AR Assistant" to "✨ Adapted for **${adaptation.modeTitle}**:\n${adaptation.summaryGreeting}\n\nReasoning: ${adaptation.aiThoughtReasoning}")
                isLoading = false
              }
            }
          }
          .testTag("gemini_send_button"),
        contentAlignment = Alignment.Center
      ) {
        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
      }
    }
  }
}

@Composable
fun GeminiAssistantAppView() {
  ArAssistantAppView()
}

// -------------------------------------------------------------
// Interactive Camera App View
// -------------------------------------------------------------
@Composable
fun CameraAppView() {
  var flashTaken by remember { mutableStateOf(false) }
  var zoomLevel by remember { mutableStateOf("1x") }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.Black)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.SpaceBetween,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Pixel Pro Camera · RAW HDR+",
        color = Color(0xCCFFFFFF),
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
      )

      // Center Viewfinder Simulation
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(380.dp)
          .clip(RoundedCornerShape(18.dp))
          .background(Color(0xFF1E2436)),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(64.dp))
          Spacer(modifier = Modifier.height(10.dp))
          Text("HDR+ Neural Processing Ready", color = Color(0xFF94A3B8), fontSize = 13.sp)
        }

        // Lens Zoom Selectors (0.5x, 1x, 2x, 5x)
        Row(
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          listOf("0.5x", "1x", "2x", "5x").forEach { z ->
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(if (zoomLevel == z) Color(0xFFFDE047) else Color(0x66000000))
                .clickable { zoomLevel = z }
                .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Text(
                text = z,
                color = if (zoomLevel == z) Color.Black else Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }

      // Shutter Controls
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color(0x33FFFFFF)),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.FlipCameraAndroid, contentDescription = "Flip", tint = Color.White)
        }

        // Shutter Button
        Box(
          modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable { flashTaken = !flashTaken }
            .testTag("camera_shutter_button"),
          contentAlignment = Alignment.Center
        ) {
          Box(
            modifier = Modifier
              .size(60.dp)
              .clip(CircleShape)
              .background(if (flashTaken) Color(0xFFE2E8F0) else Color.White)
          )
        }

        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color(0x33FFFFFF)),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.WbSunny, contentDescription = "Exposure", tint = Color.White)
        }
      }
    }
  }
}

// -------------------------------------------------------------
// Interactive Music App View
// -------------------------------------------------------------
@Composable
fun MusicPlayerAppView() {
  var isPlaying by remember { mutableStateOf(true) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(20.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Text("PILOS Hi-Fi Music", color = Color(0xFF94A3B8), fontSize = 13.sp)

    // Album Art
    Box(
      modifier = Modifier
        .size(220.dp)
        .shadow(16.dp, RoundedCornerShape(24.dp), spotColor = Color(0xFF8AB4F8))
        .clip(RoundedCornerShape(24.dp))
        .background(Color(0xFF1E293B)),
      contentAlignment = Alignment.Center
    ) {
      Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color(0xFF8AB4F8), modifier = Modifier.size(80.dp))
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text("Celestial Resonance", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
      Text("Ambient Electronica · Gemini Mix", color = Color(0xFF8AB4F8), fontSize = 14.sp)
    }

    Row(
      horizontalArrangement = Arrangement.spacedBy(24.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(64.dp)
          .clip(CircleShape)
          .background(Color(0xFF8AB4F8))
          .clickable { isPlaying = !isPlaying }
          .testTag("music_play_toggle"),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (isPlaying) Icons.Default.MusicNote else Icons.Default.PlayArrow,
          contentDescription = null,
          tint = Color(0xFF0F172A),
          modifier = Modifier.size(32.dp)
        )
      }
    }
  }
}

// -------------------------------------------------------------
// Interactive Settings App View
// -------------------------------------------------------------
@Composable
fun SettingsAppView(onOpenCustomization: () -> Unit) {
  var fluidAnimationsEnabled by remember { mutableStateOf(true) }
  var dynamicIslandEnabled by remember { mutableStateOf(true) }
  var hapticFeedbackEnabled by remember { mutableStateOf(true) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text("PILOS Launcher Settings", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)

    Button(
      onClick = onOpenCustomization,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("settings_open_customize_btn"),
      colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
    ) {
      Icon(Icons.Default.Widgets, contentDescription = null)
      Spacer(modifier = Modifier.width(8.dp))
      Text("Customize Widgets & Themes")
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text("iOS 26 Spring Physics", color = Color.White, fontWeight = FontWeight.Medium)
        Text("Ultra-fluid bouncy gesture curves", color = Color(0xFF94A3B8), fontSize = 11.sp)
      }
      Switch(
        checked = fluidAnimationsEnabled,
        onCheckedChange = { fluidAnimationsEnabled = it },
        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF3B82F6))
      )
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text("Dynamic Island Capsule", color = Color.White, fontWeight = FontWeight.Medium)
        Text("Morphing live media & alerts", color = Color(0xFF94A3B8), fontSize = 11.sp)
      }
      Switch(
        checked = dynamicIslandEnabled,
        onCheckedChange = { dynamicIslandEnabled = it },
        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF3B82F6))
      )
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text("Haptic Feedback", color = Color.White, fontWeight = FontWeight.Medium)
        Text("Subtle mechanical clicks on gestures", color = Color(0xFF94A3B8), fontSize = 11.sp)
      }
      Switch(
        checked = hapticFeedbackEnabled,
        onCheckedChange = { hapticFeedbackEnabled = it },
        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF3B82F6))
      )
    }
  }
}

// -------------------------------------------------------------
// Interactive Weather App View (Celsius & iOS 26 Glass Card)
// -------------------------------------------------------------
@Composable
fun WeatherAppView() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(20.dp),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column {
      Text("London", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
      Text("19°C · Partly Cloudy", color = Color(0xFF38BDF8), fontSize = 16.sp)
      Text("H: 22°C L: 14°C · UV Index: 3 · Humidity: 62%", color = Color(0xFF94A3B8), fontSize = 12.sp)
    }

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .background(Color(0x331E293B))
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        listOf("Mon" to "19°C", "Tue" to "21°C", "Wed" to "20°C", "Thu" to "18°C", "Fri" to "22°C").forEach { (d, t) ->
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(d, color = Color(0xFF94A3B8), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Icon(Icons.Default.WbSunny, contentDescription = null, tint = Color(0xFFFDE047), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(t, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// Generic System App View
// -------------------------------------------------------------
@Composable
fun GenericAppView(app: AppItem) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Box(
      modifier = Modifier
        .size(80.dp)
        .clip(RoundedCornerShape(24.dp))
        .background(app.accentColor),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = getAppIconVector(app.iconSymbol),
        contentDescription = app.name,
        tint = Color.White,
        modifier = Modifier.size(40.dp)
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = app.name,
      color = Color.White,
      fontSize = 22.sp,
      fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = "Running in PILOS Sandbox · iOS 26 Container",
      color = Color(0xFF94A3B8),
      fontSize = 13.sp
    )
  }
}
