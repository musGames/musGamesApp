package dk.musgames.app.ui.theme

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.palette.graphics.Palette
import dk.musgames.app.R
import dk.musgames.app.data.ColorPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val LocalIsDarkBackground = compositionLocalOf { true }

@Composable
fun BackgroundBox(
    resId: Int = R.drawable.bg_retro,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val ctx = LocalContext.current
    val userColors by ColorPrefs.colors(ctx)
        .collectAsState(ColorPrefs.DefaultBg to ColorPrefs.DefaultBtn)
    val userBg = userColors.first
    val isDark = remember(resId, userBg) { mutableStateOf(true) }

    LaunchedEffect(resId, userBg) {
        isDark.value = withContext(Dispatchers.Default) {
            val bmp = BitmapFactory.decodeResource(ctx.resources, resId)
            val dom = Palette.from(bmp).generate().getDominantColor(0xFFFFFF)
            val r = (dom shr 16) and 0xFF
            val g = (dom shr 8) and 0xFF
            val b = dom and 0xFF
            val domLum = (r * 299 + g * 587 + b * 114) / 1000

            val avgLum = ((domLum / 255f) + userBg.luminance()) / 2f
            avgLum < .5f
        }
    }

    CompositionLocalProvider(LocalIsDarkBackground provides isDark.value) {
        Box(modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(userBg)
            )

            Image(
                painter = painterResource(resId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            content()
        }
    }
}