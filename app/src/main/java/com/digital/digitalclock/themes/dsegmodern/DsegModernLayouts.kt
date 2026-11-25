package com.digital.digitalclock.themes.dsegmodern

import com.digital.digitalclock.theme.ClockLayout

/**
 * The layouts available to the DSEG Modern theme. Same as DSEG Classic.
 */
object DsegModernLayouts {

    const val ID_HH_MM_SS = "hh:mm:ss"
    const val ID_HH_MM = "hh:mm"
    const val ID_HH_MM_HALFSS = "hh:mm:halfss"

    val HH_MM_SS = ClockLayout(ID_HH_MM_SS, "hh:mm:ss")
    val HH_MM = ClockLayout(ID_HH_MM, "hh:mm")
    val HH_MM_HALFSS = ClockLayout(ID_HH_MM_HALFSS, "hh:mm:halfss")

    val all: List<ClockLayout> = listOf(HH_MM_SS, HH_MM, HH_MM_HALFSS)
}
