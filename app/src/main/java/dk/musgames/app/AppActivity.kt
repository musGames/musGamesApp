package dk.musgames.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dk.musgames.app.navigation.musgamesNav
import dk.musgames.app.ui.theme.BackgroundBox
import dk.musgames.app.ui.theme.musgamesTheme

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BackgroundBox {
                musgamesTheme {
                    musgamesNav()
                }
            }
        }
    }
}
