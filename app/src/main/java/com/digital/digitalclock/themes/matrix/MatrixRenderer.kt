package com.digital.digitalclock.themes.matrix

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digital.digitalclock.theme.RenderContext
import com.digital.digitalclock.ui.components.DotMatrixDigit
import com.digital.digitalclock.ui.components.SimpleColon

private val CornerShape = RoundedCornerShape(68.dp)

/**
 * Code-rendered (dot-matrix) renderer for the Matrix theme. Supports all three layouts,
 * thick rounded border, and theme-specific settings.
 */
@Composable
fun MatrixRender(ctx: RenderContext) {
    val scheme = ctx.colorScheme
    val width = ctx.constraintsWidth

    // Theme-specific settings.
    val transparency = (ctx.settings[MatrixTheme.SETTING_INACTIVE_ALPHA] ?: 0f).coerceIn(0f, 100f)
    val inactiveAlphaFactor = 1f - transparency / 100f
    val inactive = scheme.inactive.copy(alpha = scheme.inactive.alpha * inactiveAlphaFactor)
    val active = scheme.active
    val hideWeek = (ctx.settings[MatrixTheme.SETTING_HIDE_WEEK] ?: 0f) >= 0.5f
    val hideDate = (ctx.settings[MatrixTheme.SETTING_HIDE_DATE] ?: 0f) >= 0.5f

    val timeFontSize = when (ctx.layout.id) {
        "hh:mm" -> (width / 4.8f).value.sp
        "hh:mm:halfss" -> (width / 6.5f).value.sp
        else -> (width / 6.2f).value.sp
    }
    val subSize = (width / 22f).value.sp

    val digitW: Dp = (width - 48.dp) / 7.6f
    val digitH: Dp = digitW * 2f
    val colonW: Dp = digitW * 0.35f

    val (h1, h2) = ctx.time.hourSlots(ctx.is24Hour)
    val minuteStr = ctx.time.minuteStr
    val secondStr = ctx.time.secondStr

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(scheme.background, CornerShape)
            .border(40.dp, scheme.border, CornerShape)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (ctx.layout.id) {
                "hh:mm" -> TimeRowHhMm(
                    h1, h2, minuteStr, digitW, digitH, colonW, active, inactive, ctx.time.colonLit
                )
                "hh:mm:halfss" -> TimeRowHalfSs(
                    ctx, h1, h2, minuteStr, secondStr, digitW, digitH, colonW,
                    active, inactive, subSize
                )
                else -> TimeRowHhMmSs(
                    h1, h2, minuteStr, secondStr, digitW, digitH, colonW,
                    active, inactive, ctx.time.colonLit
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            BelowRow(
                showIndicator = ctx.layout.id != "hh:mm:halfss",
                showDate = !hideDate,
                showWeek = !hideWeek,
                ctx = ctx,
                active = active,
                inactive = inactive,
                subSize = subSize,
                digitW = digitW,
                digitH = digitH
            )
        }
    }
}

/** hh:mm:ss - all slots the same size, center aligned. */
@Composable
private fun TimeRowHhMmSs(
    h1: Char, h2: Char, minuteStr: String, secondStr: String,
    digitW: Dp, digitH: Dp, colonW: Dp,
    active: Color, inactive: Color, colonLit: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Digit(h1, digitW, digitH, active, inactive)
            Digit(h2, digitW, digitH, active, inactive)
        }
        Spacer(modifier = Modifier.width(8.dp))
        SimpleColon(modifier = Modifier.requiredWidth(colonW).height(digitH), activeColor = active, inactiveColor = inactive, lit = colonLit)
        Spacer(modifier = Modifier.width(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Digit(minuteStr[0], digitW, digitH, active, inactive)
            Digit(minuteStr[1], digitW, digitH, active, inactive)
        }
        Spacer(modifier = Modifier.width(8.dp))
        SimpleColon(modifier = Modifier.requiredWidth(colonW).height(digitH), activeColor = active, inactiveColor = inactive, lit = colonLit)
        Spacer(modifier = Modifier.width(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Digit(secondStr[0], digitW, digitH, active, inactive)
            Digit(secondStr[1], digitW, digitH, active, inactive)
        }
    }
}

/** hh:mm - hours and minutes only, same size, center aligned. */
@Composable
private fun TimeRowHhMm(
    h1: Char, h2: Char, minuteStr: String,
    digitW: Dp, digitH: Dp, colonW: Dp,
    active: Color, inactive: Color, colonLit: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Digit(h1, digitW, digitH, active, inactive)
            Digit(h2, digitW, digitH, active, inactive)
        }
        Spacer(modifier = Modifier.width(8.dp))
        SimpleColon(modifier = Modifier.requiredWidth(colonW).height(digitH), activeColor = active, inactiveColor = inactive, lit = colonLit)
        Spacer(modifier = Modifier.width(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Digit(minuteStr[0], digitW, digitH, active, inactive)
            Digit(minuteStr[1], digitW, digitH, active, inactive)
        }
    }
}

/** hh:mm:halfss - seconds at 50% size, positioned absolutely to align with hh/mm bottom. */
@Composable
private fun TimeRowHalfSs(
    ctx: RenderContext,
    h1: Char, h2: Char, minuteStr: String, secondStr: String,
    digitW: Dp, digitH: Dp, colonW: Dp,
    active: Color, inactive: Color,
    subSize: TextUnit
) {
    val halfDigitW = digitW * 0.5f
    val halfDigitH = digitH * 0.5f
    val halfColonW = colonW * 0.5f
    val indicatorSize = subSize * 0.8f

    // DotMatrixDigit centers a width-bound grid inside its box, leaving an empty
    // inset above/below. Large and small digits have different insets, so
    // compensate the difference to make their drawn bottoms share one baseline.
    val insetLarge = (digitH - digitW * 1.4f) / 2f
    val insetSmall = (halfDigitH - halfDigitW * 1.4f) / 2f
    val ssOffsetY = digitH - halfDigitH - insetLarge + insetSmall

    // Uniform gap around the seconds colon: same between mm and ':' as between ':' and ss.
    val ssGap = 16.dp
    // Horizontal start of the seconds column: right of hh:mm plus the gap.
    val ssStartX = digitW * 2f + colonW + 16.dp + digitW * 2f + 8.dp + ssGap

    // Use Box for absolute positioning
    Box {
        // Main hh:mm row
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Digit(h1, digitW, digitH, active, inactive)
                Digit(h2, digitW, digitH, active, inactive)
            }
            Spacer(modifier = Modifier.width(8.dp))
            SimpleColon(modifier = Modifier.requiredWidth(colonW).height(digitH), activeColor = active, inactiveColor = inactive, lit = ctx.time.colonLit)
            Spacer(modifier = Modifier.width(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Digit(minuteStr[0], digitW, digitH, active, inactive)
                Digit(minuteStr[1], digitW, digitH, active, inactive)
            }
        }

        // Seconds positioned absolutely: right of hh:mm, drawn bottoms aligned
        Row(
            modifier = Modifier.offset(y = ssOffsetY),
            verticalAlignment = Alignment.Top
        ) {
            Spacer(modifier = Modifier.width(ssStartX))
            SimpleColon(modifier = Modifier.requiredWidth(halfColonW).height(halfDigitH), activeColor = active, inactiveColor = inactive, lit = ctx.time.colonLit)
            Spacer(modifier = Modifier.width(ssGap))
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Digit(secondStr[0], halfDigitW, halfDigitH, active, inactive)
                Digit(secondStr[1], halfDigitW, halfDigitH, active, inactive)
            }
        }

        // Indicator positioned above seconds
        Row(
            modifier = Modifier.offset(y = ssOffsetY - indicatorSize.value.dp * 1.5f),
            verticalAlignment = Alignment.Top
        ) {
            Spacer(modifier = Modifier.width(ssStartX))
            Indicator(ctx = ctx, active = active, inactive = inactive, size = indicatorSize)
        }
    }
}

/** Row beneath the time: optional indicator, date and week. */
@Composable
private fun BelowRow(
    showIndicator: Boolean,
    showDate: Boolean,
    showWeek: Boolean,
    ctx: RenderContext,
    active: Color,
    inactive: Color,
    subSize: TextUnit,
    digitW: Dp,
    digitH: Dp
) {
    if (!showIndicator && !showDate && !showWeek) return
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (showIndicator) Indicator(ctx = ctx, active = active, inactive = inactive, size = subSize)
        if (showDate) DateDisplay(ctx.time.dateStr, active, inactive, subSize)
        if (showWeek) WeekDisplay(ctx.time.dayOfWeek, active, inactive, subSize)
    }
}

/** AM/PM / 24H indicator using dot-matrix. */
@Composable
private fun Indicator(ctx: RenderContext, active: Color, inactive: Color, size: TextUnit) {
    val slots = when {
        ctx.is24Hour -> listOf('2', '4', ' ', 'H')
        ctx.time.isPm -> listOf(' ', ' ', 'P', 'M')
        else -> listOf('A', 'M', ' ', ' ')
    }
    val charW = size.value.dp * 0.6f
    val charH = size.value.dp * 1.2f
    Row(horizontalArrangement = Arrangement.spacedBy(1.dp), verticalAlignment = Alignment.CenterVertically) {
        slots.forEach { ch ->
            if (ch != ' ') {
                DotMatrixDigit(
                    char = ch,
                    modifier = Modifier.requiredWidth(charW).height(charH),
                    activeColor = active,
                    inactiveColor = inactive
                )
            } else {
                Spacer(modifier = Modifier.width(charW))
            }
        }
    }
}

/** Date display using dot-matrix. */
@Composable
private fun DateDisplay(dateStr: String, active: Color, inactive: Color, size: TextUnit) {
    val charW = size.value.dp * 0.6f
    val charH = size.value.dp * 1.2f
    Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
        dateStr.forEach { ch ->
            DotMatrixDigit(
                char = ch,
                modifier = Modifier.requiredWidth(charW).height(charH),
                activeColor = active,
                inactiveColor = inactive
            )
        }
    }
}

/** Week display using dot-matrix. */
@Composable
private fun WeekDisplay(weekStr: String, active: Color, inactive: Color, size: TextUnit) {
    val charW = size.value.dp * 0.6f
    val charH = size.value.dp * 1.2f
    Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
        weekStr.forEach { ch ->
            DotMatrixDigit(
                char = ch,
                modifier = Modifier.requiredWidth(charW).height(charH),
                activeColor = active,
                inactiveColor = inactive
            )
        }
    }
}

@Composable
private fun Digit(
    char: Char,
    digitW: Dp,
    digitH: Dp,
    active: Color,
    inactive: Color
) {
    val c = if (char == ' ') ' ' else char
    DotMatrixDigit(
        char = c,
        modifier = Modifier.requiredWidth(digitW).height(digitH),
        activeColor = active,
        inactiveColor = inactive
    )
}
