package dk.musgames.app.ui.gameinterface

import android.webkit.JavascriptInterface
import dk.musgames.app.viewModel.GameInterfaceViewModel

class GameBridge(
    private val vm: GameInterfaceViewModel,
    private val gameId:    String,
    private val gameTitle: String
) {
    @JavascriptInterface
    fun getUserJson(): String = vm.userJson

    @JavascriptInterface
    fun submitHighScore(rawScore: Int) {
        vm.submitHighScore(rawScore, gameId, gameTitle)
    }
}
