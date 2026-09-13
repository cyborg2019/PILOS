package com.example.data

import android.util.Log
import com.example.BuildConfig
import com.example.model.GeminiAdaptationResult
import com.example.model.WidgetType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiService {

  private val client = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .build()

  suspend fun adaptLauncher(userPrompt: String): GeminiAdaptationResult = withContext(Dispatchers.IO) {
    val apiKey = try {
      BuildConfig.GEMINI_API_KEY
    } catch (e: Throwable) {
      ""
    }

    if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
      Log.d("GeminiService", "No valid API key present, using intelligent local heuristic engine.")
      return@withContext generateHeuristicAdaptation(userPrompt)
    }

    try {
      val systemPrompt = """
        You are PILOS AR Assistant, an intelligent launcher orchestrator for an Android launcher inspired by Google Pixel and iOS 26 fluid mechanics.
        Analyze the user's preference prompt, context, or mood, and recommend a personalized launcher layout.
        
        Available widget types:
        - AT_A_GLANCE
        - NOW_PLAYING
        - BATTERY_TELEMETRY
        - CLOCK
        - AR_SMART_STACK
        - QUICK_TOGGLES
        - PHOTO_MEMORIES
        
        Available themes:
        - pixel_obsidian (Dark, sleek Pixel black/blue)
        - aurora_borealis (Nordic cyan/emerald night)
        - sunset_dunes (Warm sunset orange/gold)
        - botanical_sage (Material You forest sage green)
        - cyber_neon (High contrast violet/cyan)
        - arctic_silver (Clean modern light theme)

        Available dock apps:
        phone, messages, chrome, camera, ar_assistant, photos, settings, music, calendar, maps, clock, weather, files, calculator, notes, health.

        You MUST reply in STRICT JSON format with no markdown blocks:
        {
          "modeTitle": "Short Title (e.g. Deep Focus Mode)",
          "summaryGreeting": "At-a-Glance greeting context (e.g. AR Spatial Tracking active · 22°C Clear)",
          "recommendedThemeId": "theme_id",
          "recommendedWidgets": ["AT_A_GLANCE", "CLOCK", "BATTERY_TELEMETRY"],
          "recommendedDockAppIds": ["notes", "calendar", "ar_assistant", "chrome", "clock"],
          "aiThoughtReasoning": "1 sentence explanation of why this layout fits the user's intent."
        }
      """.trimIndent()

      val jsonBody = JSONObject().apply {
        val contentsArray = JSONArray().apply {
          put(JSONObject().apply {
            put("parts", JSONArray().apply {
              put(JSONObject().apply { put("text", "System instruction: $systemPrompt\n\nUser request: $userPrompt") })
            })
          })
        }
        put("contents", contentsArray)
        put("generationConfig", JSONObject().apply {
          put("temperature", 0.7)
          put("topP", 0.95)
          put("responseMimeType", "application/json")
        })
      }

      val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
      val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

      val request = Request.Builder()
        .url(endpoint)
        .post(requestBody)
        .build()

      val response = client.newCall(request).execute()
      val responseString = response.body?.string()

      if (!response.isSuccessful || responseString == null) {
        Log.w("GeminiService", "Gemini API HTTP ${response.code}: $responseString")
        return@withContext generateHeuristicAdaptation(userPrompt)
      }

      val responseJson = JSONObject(responseString)
      val candidates = responseJson.optJSONArray("candidates")
      val firstPart = candidates?.optJSONObject(0)
        ?.optJSONObject("content")
        ?.optJSONArray("parts")
        ?.optJSONObject(0)
        ?.optString("text") ?: ""

      parseJsonResponse(firstPart, userPrompt)
    } catch (e: Exception) {
      Log.e("GeminiService", "Failed to call Gemini API: ${e.message}", e)
      generateHeuristicAdaptation(userPrompt)
    }
  }

  private fun parseJsonResponse(rawJson: String, originalPrompt: String): GeminiAdaptationResult {
    return try {
      val cleanJson = rawJson.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
      val obj = JSONObject(cleanJson)
      val title = obj.optString("modeTitle", "Custom AI Setup")
      val greeting = obj.optString("summaryGreeting", "PILOS AI Adaptive Engine ready")
      val theme = obj.optString("recommendedThemeId", "pixel_obsidian")
      val widgetsArray = obj.optJSONArray("recommendedWidgets")
      val widgets = mutableListOf<WidgetType>()
      if (widgetsArray != null) {
        for (i in 0 until widgetsArray.length()) {
          val name = widgetsArray.getString(i)
          try {
            widgets.add(WidgetType.valueOf(name))
          } catch (_: Exception) {}
        }
      }
      if (widgets.isEmpty()) {
        widgets.addAll(listOf(WidgetType.AT_A_GLANCE, WidgetType.AR_SMART_STACK, WidgetType.NOW_PLAYING))
      }

      val appsArray = obj.optJSONArray("recommendedDockAppIds")
      val dockApps = mutableListOf<String>()
      if (appsArray != null) {
        for (i in 0 until appsArray.length()) {
          dockApps.add(appsArray.getString(i))
        }
      }
      if (dockApps.isEmpty()) {
        dockApps.addAll(listOf("phone", "messages", "chrome", "camera", "ar_assistant"))
      }

      val reasoning = obj.optString("aiThoughtReasoning", "Adapted launcher layout according to your preferences.")

      GeminiAdaptationResult(
        modeTitle = title,
        summaryGreeting = greeting,
        recommendedThemeId = theme,
        recommendedWidgets = widgets,
        recommendedDockAppIds = dockApps,
        aiThoughtReasoning = reasoning
      )
    } catch (e: Exception) {
      Log.w("GeminiService", "Failed to parse Gemini JSON: ${e.message}")
      generateHeuristicAdaptation(originalPrompt)
    }
  }

  fun generateHeuristicAdaptation(prompt: String): GeminiAdaptationResult {
    val lower = prompt.lowercase()
    return when {
      lower.contains("work") || lower.contains("code") || lower.contains("study") || lower.contains("focus") -> {
        GeminiAdaptationResult(
          modeTitle = "Deep Focus Mode",
          summaryGreeting = "AR Focus session active · Spatial anchors locked · 21°C",
          recommendedThemeId = "pixel_obsidian",
          recommendedWidgets = listOf(
            WidgetType.AT_A_GLANCE,
            WidgetType.CLOCK,
            WidgetType.BATTERY_TELEMETRY,
            WidgetType.AR_SMART_STACK
          ),
          recommendedDockAppIds = listOf("notes", "calendar", "ar_assistant", "chrome", "calculator"),
          aiThoughtReasoning = "Prioritized clean digital clock, device telemetry, and fast note-taking to minimize distractions."
        )
      }
      lower.contains("relax") || lower.contains("chill") || lower.contains("evening") || lower.contains("night") || lower.contains("music") -> {
        GeminiAdaptationResult(
          modeTitle = "Evening Ambient Chill",
          summaryGreeting = "Unwinding · Lo-fi playlist playing · 20°C clear skies",
          recommendedThemeId = "cyber_neon",
          recommendedWidgets = listOf(
            WidgetType.NOW_PLAYING,
            WidgetType.PHOTO_MEMORIES,
            WidgetType.QUICK_TOGGLES,
            WidgetType.CLOCK
          ),
          recommendedDockAppIds = listOf("music", "photos", "messages", "ar_assistant", "phone"),
          aiThoughtReasoning = "Front-loaded audio waveforms, memory frames, and cozy ambient cyber aesthetic."
        )
      }
      lower.contains("travel") || lower.contains("trip") || lower.contains("navigate") || lower.contains("explore") -> {
        GeminiAdaptationResult(
          modeTitle = "Traveler & Commute",
          summaryGreeting = "AR Wayfinding active · Transit in 14m · 19°C London",
          recommendedThemeId = "sunset_dunes",
          recommendedWidgets = listOf(
            WidgetType.AT_A_GLANCE,
            WidgetType.CLOCK,
            WidgetType.BATTERY_TELEMETRY,
            WidgetType.QUICK_TOGGLES
          ),
          recommendedDockAppIds = listOf("maps", "camera", "weather", "ar_assistant", "phone"),
          aiThoughtReasoning = "Arranged dual world clock, GPS navigation, and camera shortcuts for swift traveling."
        )
      }
      lower.contains("fitness") || lower.contains("workout") || lower.contains("health") || lower.contains("run") -> {
        GeminiAdaptationResult(
          modeTitle = "Active Fitness Pulse",
          summaryGreeting = "6,420 steps · 450 kcal · 23°C Sunny outdoor route",
          recommendedThemeId = "aurora_borealis",
          recommendedWidgets = listOf(
            WidgetType.BATTERY_TELEMETRY,
            WidgetType.NOW_PLAYING,
            WidgetType.AT_A_GLANCE,
            WidgetType.QUICK_TOGGLES
          ),
          recommendedDockAppIds = listOf("health", "music", "clock", "maps", "ar_assistant"),
          aiThoughtReasoning = "Set up vital health telemetry, rhythm music controls, and stopwatch triggers."
        )
      }
      lower.contains("light") || lower.contains("clean") || lower.contains("minimal") || lower.contains("porcelain") -> {
        GeminiAdaptationResult(
          modeTitle = "Porcelain Minimalist",
          summaryGreeting = "Clean slate · Notifications silenced · 22°C Sunny",
          recommendedThemeId = "arctic_silver",
          recommendedWidgets = listOf(
            WidgetType.CLOCK,
            WidgetType.AT_A_GLANCE,
            WidgetType.PHOTO_MEMORIES
          ),
          recommendedDockAppIds = listOf("phone", "messages", "chrome", "camera", "ar_assistant"),
          aiThoughtReasoning = "Stripped away visual clutter for high-clarity Pixel porcelain aesthetics."
        )
      }
      else -> {
        GeminiAdaptationResult(
          modeTitle = "Balanced PILOS Dynamic",
          summaryGreeting = "Adapted to daily routine · AR Assistant spatial hub active",
          recommendedThemeId = "pixel_obsidian",
          recommendedWidgets = listOf(
            WidgetType.AT_A_GLANCE,
            WidgetType.NOW_PLAYING,
            WidgetType.BATTERY_TELEMETRY,
            WidgetType.QUICK_TOGGLES,
            WidgetType.AR_SMART_STACK
          ),
          recommendedDockAppIds = listOf("phone", "messages", "chrome", "camera", "ar_assistant"),
          aiThoughtReasoning = "Balanced signature Pixel widgets with iOS 26 fluid media island and telemetry."
        )
      }
    }
  }
}
