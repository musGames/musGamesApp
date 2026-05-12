package dk.musgames.app.ui.theme

import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.Typography
import androidx.compose.ui.unit.sp
import dk.musgames.app.R
val Inconsolata = FontFamily(
    Font(R.font.inconsolata_regular, weight = FontWeight.Normal),
)

val AppTypography = Typography(
    displayLarge = TextStyle(fontFamily = Inconsolata, fontWeight = FontWeight.Bold,   fontSize = 50.sp),
    titleLarge   = TextStyle(fontFamily = Inconsolata, fontWeight = FontWeight.Bold,   fontSize = 26.sp),
    bodyLarge    = TextStyle(fontFamily = Inconsolata, fontWeight = FontWeight.Bold, fontSize = 20.sp),
    labelLarge   = TextStyle(fontFamily = Inconsolata, fontWeight = FontWeight.Bold,   fontSize = 18.sp)
)
