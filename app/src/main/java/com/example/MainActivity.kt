package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ui.LauncherScreen
import com.example.ui.LauncherViewModel
import com.example.ui.theme.PilosLauncherTheme

class MainActivity : ComponentActivity() {

  private val launcherViewModel: LauncherViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      PilosLauncherTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
          LauncherScreen(viewModel = launcherViewModel)
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "PILOS: Pixel & iOS 26 Launcher for $name", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  PilosLauncherTheme { Greeting("User") }
}
