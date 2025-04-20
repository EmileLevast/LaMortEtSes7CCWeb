package org.levast.project.configuration

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import lamortetses7ccweb.composeapp.generated.resources.Aniron_7BaP
import lamortetses7ccweb.composeapp.generated.resources.Res

class GraphicConstantsFullGrid {
    val widthBorder = 2.dp
    val cellSpace = 20.dp
    val statsBigImagePadding = 20.dp
    val fontCard = Res.font.Aniron_7BaP

    val paddingCellLayoutJoueur = 15.dp


    fun colorToBrush(color: Color, colorEndBrush:Color): Brush {
        val colorsBrushed = arrayOf(
            0.0f to color,
            0.2f to color,
            1.3f to colorEndBrush,
        )

        return Brush.linearGradient(colorStops = colorsBrushed)
    }
}