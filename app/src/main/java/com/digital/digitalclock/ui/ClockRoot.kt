package com.digital.digitalclock.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.digital.digitalclock.core.ClockStateHolder
import com.digital.digitalclock.core.buildClockTime
import com.digital.digitalclock.theme.RenderContext
import kotlinx.coroutines.delay
import kotlin.math.abs

private const val SWIPE_THRESHOLD = 50f
private const val MIN_SCALE = 0.3f
private const val MAX_SCALE = 3.0f

/**
 * Top-level clock surface. Owns the ticking time, routes the global gestures to the
 * function-layer state holder, renders the active theme, and hosts the settings menu.
 *
 * Gestures:
 * - short press (tap): cycle theme
 * - swipe left/right: cycle the current theme's color scheme
 * - swipe up/down: cycle the current theme's layout
 * - long press: open the settings menu (and reveal system bars)
 */
@Composable
fun ClockRoot(
    onShowSystemBars: () -> Unit = {},
    onHideSystemBars: () -> Unit = {}
) {
    val context = LocalContext.current
    val state = remember { ClockStateHolder(context) }

    var time by remember { mutableStateOf(buildClockTime(state.zoneId)) }
    var settingsOpen by remember { mutableStateOf(false) }

    // Rebuild the time every second; restart when the selected zone changes.
    LaunchedEffect(state.zoneId) {
        while (true) {
            time = buildClockTime(state.zoneId)
            delay(1000)
        }
    }

    fun openSettings() {
        settingsOpen = true
        onShowSystemBars()
    }

    fun closeSettings() {
        settingsOpen = false
        onHideSystemBars()
    }

    var dragX by remember { mutableFloatStateOf(0f) }
    var dragY by remember { mutableFloatStateOf(0f) }

    // Zoom and pan state
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(state.currentColorScheme.background)
            // Tap / long-press
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { state.nextTheme() },
                    onLongPress = { openSettings() }
                )
            }
            // Swipes (separate detector so drags cancel the tap)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { dragX = 0f; dragY = 0f },
                    onDrag = { change, amount ->
                        change.consume()
                        dragX += amount.x
                        dragY += amount.y
                    },
                    onDragEnd = {
                        if (abs(dragX) > abs(dragY)) {
                            if (dragX > SWIPE_THRESHOLD) state.cycleColorScheme(1)
                            else if (dragX < -SWIPE_THRESHOLD) state.cycleColorScheme(-1)
                        } else {
                            if (dragY > SWIPE_THRESHOLD) state.cycleLayout(1)
                            else if (dragY < -SWIPE_THRESHOLD) state.cycleLayout(-1)
                        }
                    }
                )
            }
            // Pinch zoom and two-finger pan (only activates with 2+ fingers)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    var initialSpan = 0f
                    var initialCenter = Offset.Zero

                    while (true) {
                        val event = awaitPointerEvent()

                        // Only handle multi-touch (2+ fingers)
                        if (event.changes.size >= 2) {
                            when (event.type) {
                                PointerEventType.Press -> {
                                    // Calculate initial span and center
                                    val p1 = event.changes[0].position
                                    val p2 = event.changes[1].position
                                    initialSpan = (p1 - p2).getDistance()
                                    initialCenter = (p1 + p2) / 2f
                                    event.changes.forEach { it.consume() }
                                }
                                PointerEventType.Move -> {
                                    val p1 = event.changes[0].position
                                    val p2 = event.changes[1].position
                                    val currentSpan = (p1 - p2).getDistance()
                                    val currentCenter = (p1 + p2) / 2f

                                    // Calculate zoom
                                    val zoom = if (initialSpan > 0) currentSpan / initialSpan else 1f
                                    scale = (scale * zoom).coerceIn(MIN_SCALE, MAX_SCALE)

                                    // Calculate pan
                                    val pan = currentCenter - initialCenter
                                    val viewportWidth = size.width
                                    val viewportHeight = size.height
                                    val contentWidth = viewportWidth * scale
                                    val contentHeight = viewportHeight * scale

                                    val maxOffsetX = if (contentWidth > viewportWidth) (contentWidth - viewportWidth) / 2f else 0f
                                    val maxOffsetY = if (contentHeight > viewportHeight) (contentHeight - viewportHeight) / 2f else 0f

                                    offset = Offset(
                                        (offset.x + pan.x).coerceIn(-maxOffsetX, maxOffsetX),
                                        (offset.y + pan.y).coerceIn(-maxOffsetY, maxOffsetY)
                                    )

                                    // Reset for next calculation
                                    initialSpan = currentSpan
                                    initialCenter = currentCenter

                                    event.changes.forEach { it.consume() }
                                }
                                PointerEventType.Release -> {
                                    // Reset when fingers lifted
                                    initialSpan = 0f
                                    event.changes.forEach { it.consume() }
                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offset.x
                translationY = offset.y
            }
    ) {
        state.currentTheme.Render(
            RenderContext(
                time = time,
                is24Hour = state.is24Hour,
                layout = state.currentLayout,
                colorScheme = state.currentColorScheme,
                settings = state.resolvedSettings,
                constraintsWidth = maxWidth
            )
        )
    }

    if (settingsOpen) {
        SettingsPanel(state = state, onDismiss = { closeSettings() })
    }
}
