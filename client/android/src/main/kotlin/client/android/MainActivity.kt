@file:Suppress("PackageDirectoryMismatch", "UnusedImport") // IDE will remove collect
package studio.forface.cinescout

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.lifecycleScope
import client.Navigator
import client.ViewState
import client.android.ui.CineScoutApp
import client.viewModel.DrawerViewModel
import co.touchlab.kermit.Logger
import domain.auth.Link
import entities.Either
import entities.TmdbOauthCallback
import entities.TraktOauthCallback
import entities.auth.Auth.LoginError.TokenApprovalCancelled
import entities.auth.Auth.LoginState
import entities.auth.Auth.LoginState.ApproveRequestToken.Approved
import entities.right
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

    private val navigator by inject<Navigator>()
    private val drawerViewModel by inject<DrawerViewModel> { parametersOf(lifecycleScope) }
    private val logger by inject<Logger>()

    private var tmdbApprovalChannel: Channel<Either<TokenApprovalCancelled, Approved.WithoutCode>>? = null
    private var traktApprovalChannel: Channel<Either<TokenApprovalCancelled, Approved.WithCode>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init the ViewModel
        drawerViewModel

        setContent {
            CineScoutApp(getKoin())
        }


        navigator.screen.onEach {
            if (it == ViewState.None) {
                navigator.toHome()
                super.onBackPressed()
            }
        }.launchIn(lifecycleScope)

        drawerViewModel.tmdbLinkResult.onEach {
            logger.i(it.toString(), "MainActivity: Tmdb linkResult")

            val linkingState = it.rightOrNull()

            if (linkingState is Link.State.Login) {
                val loginState = linkingState.loginState
                if (loginState is LoginState.ApproveRequestToken.WithoutCode) {
                    tmdbApprovalChannel = loginState.resultChannel
                    startAuth(loginState.request)
                }
            }
        }.launchIn(lifecycleScope)

        drawerViewModel.traktLinkResult.onEach {
            logger.i(it.toString(), "MainActivity: Trakt linkResult")

            val linkingState = it.rightOrNull()

            if (linkingState is Link.State.Login) {
                val loginState = linkingState.loginState
                if (loginState is LoginState.ApproveRequestToken.WithCode) {
                    traktApprovalChannel = loginState.resultChannel
                    startAuth(loginState.request)
                }
            }
        }.launchIn(lifecycleScope)

    }

    private fun startAuth(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)))
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val dataString = intent?.dataString ?: ""
        logger.d(dataString, "MainActivity - onNewIntent")

        val tmdbApproved = dataString.startsWith(TmdbOauthCallback)
        val traktApproved = dataString.startsWith(TraktOauthCallback)

        if (tmdbApproved) {
            logger.i("Tmdb approved", "MainActivity")
            checkNotNull(tmdbApprovalChannel).offer(Approved.WithoutCode.right())
        }
        if (traktApproved) {
            val traktCode = checkNotNull(dataString.substringAfter("code="))
            logger.i("Trakt approved. Code: $traktCode", "MainActivity")
            checkNotNull(traktApprovalChannel).offer(Approved.WithCode(traktCode).right())
        }
    }

    override fun onBackPressed() {
        navigator.back()
    }
}

