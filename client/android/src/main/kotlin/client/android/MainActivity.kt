@file:Suppress("PackageDirectoryMismatch", "UnusedImport") // IDE will remove collect
package studio.forface.cinescout

import android.content.Context
import android.content.Intent
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
                    openBrowser(this@MainActivity, loginState.request)
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
                    openBrowser(this@MainActivity, loginState.request)
                }
            }
        }.launchIn(lifecycleScope)

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val approved = intent?.getStringExtra(TMDB_OAUTH_EXTRA) != null
        if (approved) checkNotNull(tmdbApprovalChannel).offer(Approved.WithoutCode.right())
    }

    override fun onBackPressed() {
        navigator.back()
    }

    companion object {
        const val TMDB_OAUTH_EXTRA = "tmdb_oauth"
        const val TRAKT_OAUTH_EXTRA = "trakt_oauth"
    }
}

private fun openBrowser(context: Context, url: String) {
    context.startActivity(
        Intent(context, BrowserActivity::class.java)
            .putExtra(BrowserActivity.URL_EXTRA, url)
    )
}

class BrowserActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webView = WebView(this)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val url = request.url.toString()
                val isTmdbOauthCallback = TmdbOauthCallback in url
                val isTraktOauthCallback = TraktOauthCallback in url
                if (isTmdbOauthCallback) {
                    startActivity(
                        Intent(this@BrowserActivity, MainActivity::class.java)
                            .putExtra(MainActivity.TMDB_OAUTH_EXTRA, url)
                    )
                    finish()
                }
                if (isTraktOauthCallback) {
                    startActivity(
                        Intent(this@BrowserActivity, MainActivity::class.java)
                            .putExtra(MainActivity.TRAKT_OAUTH_EXTRA, url)
                    )
                    finish()
                }
                return isTmdbOauthCallback
            }
        }
        setContentView(webView)
        val url = intent!!.getStringExtra(URL_EXTRA)!!
        webView.loadUrl(url)
        Log.i("Browser", url)
    }

    companion object {
        const val URL_EXTRA = "url"
    }
}
