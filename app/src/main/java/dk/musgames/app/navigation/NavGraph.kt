package dk.musgames.app.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import dk.musgames.app.ui.games.Game         // ← model
import dk.musgames.app.ui.games.GamesPage
import dk.musgames.app.ui.gameinterface.GameInterfacePage
import dk.musgames.app.ui.leaderboard.LeaderboardPage
import dk.musgames.app.ui.login.LoginScreen
import dk.musgames.app.ui.navigation.NAVIGATIONPAGE
import dk.musgames.app.ui.settings.SettingsPage
import dk.musgames.app.ui.userpage.UserPage
import dk.musgames.app.ui.login.ForgotPasswordScreen
object Routes {
    const val LOGIN           = "login"
    const val HUB             = "NavigationPage"
    const val USER            = "user"
    const val SETTINGS        = "settings"
    const val GAMES           = "games"
    const val GAME            = "game"
    const val LEADERBOARD     = "leaderboard"

    const val FORGOT_PASSWORD = "forgot_password"
}

@Composable
fun musgamesNav() {

    val nav = rememberNavController()

    NavHost(nav, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoggedIn = {
                    nav.navigate(Routes.HUB) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onForgotPassword = {
                    nav.navigate(Routes.FORGOT_PASSWORD)
                }
            )
        }
        /* ---------- Forgot Password ---------- */
        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onBack = { nav.popBackStack() }
            )
        }




        composable(Routes.HUB) {
            NAVIGATIONPAGE(
                onNavigate = nav::navigate,
                onLogout   = {
                    nav.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.USER)      { UserPage(onBack = { nav.popBackStack() }) }
        composable(Routes.SETTINGS)  { SettingsPage(onBack = { nav.popBackStack() }) }
        composable(Routes.LEADERBOARD) {
            LeaderboardPage(onBack = { nav.popBackStack() })
        }

        composable(Routes.GAMES) {
            GamesPage(
                onBack = { nav.popBackStack() },
                onGameClicked = { game: Game ->
                    val encUrl   = Uri.encode(game.netlifyUrl)
                    val encTitle = Uri.encode(game.title)

                    nav.navigate(
                        "${Routes.GAME}?" +
                                "url=$encUrl&" +
                                "id=${game.id}&" +
                                "title=$encTitle"
                    )
                }
            )
        }

        composable(
            route =
                "${Routes.GAME}?url={url}&id={id}&title={title}",
            arguments = listOf(
                navArgument("url")   { type = NavType.StringType },
                navArgument("id")    { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType }
            )
        ) { back ->
            val url   = back.arguments?.getString("url")   ?: ""
            val id    = back.arguments?.getString("id")    ?: ""
            val title = Uri.decode(back.arguments?.getString("title") ?: "")

            GameInterfacePage(
                encodedUrl = url,
                gameId     = id,
                gameTitle  = title,
                onBack     = { nav.popBackStack() }
            )
        }
    }
}
