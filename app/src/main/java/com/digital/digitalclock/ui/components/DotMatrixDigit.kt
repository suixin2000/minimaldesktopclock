package com.digital.digitalclock.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

/**
 * Shared dot-matrix drawing components. Re-homed from the legacy DotMatrixDigit.kt
 * with unchanged drawing logic; used by code-rendered themes (e.g. Matrix).
 */
@Composable
fun DotMatrixDigit(
    char: Char,
    modifier: Modifier = Modifier,
    activeColor: Color = Color(0xFFFBFA72),
    inactiveColor: Color = Color(0xFF242000)
) {
    val bitmap = getMatrixBitmap(char)

    Canvas(modifier = modifier) {
        val rows = bitmap.size
        val cols = bitmap[0].length

        val cellSize = minOf(size.width / cols, size.height / rows)
        val dotSize = cellSize * 0.90f
        val spacing = cellSize - dotSize

        val totalGridW = cols * cellSize
        val totalGridH = rows * cellSize
        val offsetX = (size.width - totalGridW) / 2f
        val offsetY = (size.height - totalGridH) / 2f

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val isLit = bitmap[r][c] == '1' || bitmap[r][c] == '#'
                val color = if (isLit) activeColor else inactiveColor
                val x = offsetX + c * cellSize + spacing / 2f
                val y = offsetY + r * cellSize + spacing / 2f
                drawRect(
                    color = color,
                    topLeft = Offset(x, y),
                    size = Size(dotSize, dotSize)
                )
            }
        }
    }
}

@Composable
fun DotMatrixColon(
    modifier: Modifier = Modifier,
    activeColor: Color = Color(0xFFFBFA72),
    inactiveColor: Color = Color(0xFF242000),
    lit: Boolean = true
) {
    val bitmap = arrayOf(
        "     ",
        "  #  ",
        "     ",
        "     ",
        "     ",
        "  #  ",
        "     "
    )
    Canvas(modifier = modifier) {
        val rows = bitmap.size
        val cols = bitmap[0].length

        val cellSize = minOf(size.width / cols, size.height / rows)
        val dotSize = cellSize * 0.90f
        val spacing = cellSize - dotSize

        val totalGridW = cols * cellSize
        val totalGridH = rows * cellSize
        val offsetX = (size.width - totalGridW) / 2f
        val offsetY = (size.height - totalGridH) / 2f

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val isLit = (bitmap[r][c] == '#') && lit
                val color = if (isLit) activeColor else inactiveColor
                val x = offsetX + c * cellSize + spacing / 2f
                val y = offsetY + r * cellSize + spacing / 2f
                drawRect(
                    color = color,
                    topLeft = Offset(x, y),
                    size = Size(dotSize, dotSize)
                )
            }
        }
    }
}

fun getMatrixBitmap(c: Char): Array<String> {
    return when (c.uppercaseChar()) {
        '0' -> arrayOf(
            " ### ",
            "#   #",
            "#  ##",
            "# # #",
            "##  #",
            "#   #",
            " ### "
        )
        '1' -> arrayOf(
            "  #  ",
            " ##  ",
            "  #  ",
            "  #  ",
            "  #  ",
            "  #  ",
            " ### "
        )
        '2' -> arrayOf(
            " ### ",
            "#   #",
            "    #",
            "   # ",
            "  #  ",
            " #   ",
            "#####"
        )
        '3' -> arrayOf(
            "#####",
            "    #",
            "   # ",
            "  ## ",
            "    #",
            "#   #",
            " ####"
        )
        '4' -> arrayOf(
            "   # ",
            "  ## ",
            " # # ",
            "#  # ",
            "#####",
            "   # ",
            "   # "
        )
        '5' -> arrayOf(
            "#####",
            "#    ",
            "#### ",
            "    #",
            "    #",
            "#   #",
            " ### "
        )
        '6' -> arrayOf(
            " ### ",
            "#    ",
            "#    ",
            "#### ",
            "#   #",
            "#   #",
            " ### "
        )
        '7' -> arrayOf(
            "#####",
            "    #",
            "   # ",
            "  #  ",
            " #   ",
            " #   ",
            " #   "
        )
        '8' -> arrayOf(
            " ### ",
            "#   #",
            "#   #",
            " ### ",
            "#   #",
            "#   #",
            " ### "
        )
        '9' -> arrayOf(
            " ### ",
            "#   #",
            "#   #",
            " ####",
            "    #",
            "    #",
            " ### "
        )
        'A' -> arrayOf(
            " ### ",
            "#   #",
            "#   #",
            "#####",
            "#   #",
            "#   #",
            "#   #"
        )
        'B' -> arrayOf(
            "#### ",
            "#   #",
            "#   #",
            "#### ",
            "#   #",
            "#   #",
            "#### "
        )
        'C' -> arrayOf(
            " ####",
            "#    ",
            "#    ",
            "#    ",
            "#    ",
            "#    ",
            " ####"
        )
        'D' -> arrayOf(
            "#### ",
            "#   #",
            "#   #",
            "#   #",
            "#   #",
            "#   #",
            "#### "
        )
        'E' -> arrayOf(
            "#####",
            "#    ",
            "#    ",
            "#### ",
            "#    ",
            "#    ",
            "#####"
        )
        'F' -> arrayOf(
            "#####",
            "#    ",
            "#    ",
            "#### ",
            "#    ",
            "#    ",
            "#    "
        )
        'G' -> arrayOf(
            " ### ",
            "#   #",
            "#    ",
            "#  ##",
            "#   #",
            "#   #",
            " ### "
        )
        'H' -> arrayOf(
            "#   #",
            "#   #",
            "#   #",
            "#####",
            "#   #",
            "#   #",
            "#   #"
        )
        'I' -> arrayOf(
            " ### ",
            "  #  ",
            "  #  ",
            "  #  ",
            "  #  ",
            "  #  ",
            " ### "
        )
        'J' -> arrayOf(
            "    #",
            "    #",
            "    #",
            "    #",
            "#   #",
            "#   #",
            " ### "
        )
        'K' -> arrayOf(
            "#   #",
            "#  # ",
            "# #  ",
            "##   ",
            "# #  ",
            "#  # ",
            "#   #"
        )
        'L' -> arrayOf(
            "#    ",
            "#    ",
            "#    ",
            "#    ",
            "#    ",
            "#    ",
            "#####"
        )
        'M' -> arrayOf(
            "#   #",
            "## ##",
            "# # #",
            "#   #",
            "#   #",
            "#   #",
            "#   #"
        )
        'N' -> arrayOf(
            "#   #",
            "##  #",
            "# # #",
            "#  ##",
            "#   #",
            "#   #",
            "#   #"
        )
        'O' -> arrayOf(
            " ### ",
            "#   #",
            "#   #",
            "#   #",
            "#   #",
            "#   #",
            " ### "
        )
        'P' -> arrayOf(
            "#### ",
            "#   #",
            "#   #",
            "#### ",
            "#    ",
            "#    ",
            "#    "
        )
        'Q' -> arrayOf(
            " ### ",
            "#   #",
            "#   #",
            "#   #",
            "# # #",
            "#  ##",
            " ####"
        )
        'R' -> arrayOf(
            "#### ",
            "#   #",
            "#   #",
            "#### ",
            "# #  ",
            "#  # ",
            "#   #"
        )
        'S' -> arrayOf(
            " ### ",
            "#   #",
            "#    ",
            " ### ",
            "    #",
            "#   #",
            " ### "
        )
        'T' -> arrayOf(
            "#####",
            "  #  ",
            "  #  ",
            "  #  ",
            "  #  ",
            "  #  ",
            "  #  "
        )
        'U' -> arrayOf(
            "#   #",
            "#   #",
            "#   #",
            "#   #",
            "#   #",
            "#   #",
            " ### "
        )
        'V' -> arrayOf(
            "#   #",
            "#   #",
            "#   #",
            "#   #",
            " # # ",
            " # # ",
            "  #  "
        )
        'W' -> arrayOf(
            "#   #",
            "#   #",
            "#   #",
            "# # #",
            "## ##",
            "#   #",
            "#   #"
        )
        'X' -> arrayOf(
            "#   #",
            " # # ",
            "  #  ",
            "  #  ",
            "  #  ",
            " # # ",
            "#   #"
        )
        'Y' -> arrayOf(
            "#   #",
            "#   #",
            " # # ",
            "  #  ",
            "  #  ",
            "  #  ",
            "  #  "
        )
        'Z' -> arrayOf(
            "#####",
            "    #",
            "   # ",
            "  #  ",
            " #   ",
            "#    ",
            "#####"
        )
        '-' -> arrayOf(
            "     ",
            "     ",
            "     ",
            "#####",
            "     ",
            "     ",
            "     "
        )
        '.' -> arrayOf(
            "     ",
            "     ",
            "     ",
            "     ",
            "     ",
            "     ",
            "  #  "
        )
        else -> arrayOf(
            "     ",
            "     ",
            "     ",
            "     ",
            "     ",
            "     ",
            "     "
        )
    }
}
