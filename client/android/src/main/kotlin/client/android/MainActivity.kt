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
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.lifecycleScope
import client.Navigator
import client.ViewState
import client.android.ui.CineScoutApp
import client.viewModel.DrawerViewModel
import co.touchlab.kermit.Logger
import domain.auth.LinkToTmdb
import entities.Either
import entities.TmdbOauthCallback
import entities.auth.TmdbAuth
import entities.auth.TmdbAuth.LoginState.ApproveRequestToken.Approved
import entities.auth.TmdbAuth.LoginError.TokenApprovalCancelled
import entities.right
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

    private val navigator by inject<Navigator>()
    private val drawerViewModel by inject<DrawerViewModel> { parametersOf(lifecycleScope) }
    private val logger by inject<Logger>()

    private var tokenApprovalChannel: Channel<Either<TokenApprovalCancelled, Approved>>? = null

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
            val linkingState = it.rightOrNull()

            logger.i(linkingState.toString(), "MainActivity: linkingState")

            if (linkingState is LinkToTmdb.State.Login) {
                val loginState = linkingState.loginState
                if (loginState is TmdbAuth.LoginState.ApproveRequestToken) {
                    tokenApprovalChannel = loginState.resultChannel
                    openBrowser(this@MainActivity, loginState.request)
                }
            }
        }.launchIn(lifecycleScope)

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val approved = intent?.getStringExtra(TMDB_OAUTH_EXTRA) != null
        if (approved) checkNotNull(tokenApprovalChannel).offer(Approved.right())
    }

    override fun onBackPressed() {
        navigator.back()
    }

    companion object {
        const val TMDB_OAUTH_EXTRA = "tmdb_oauth"
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
                val isOauthCallback = TmdbOauthCallback in url
                if (isOauthCallback) {
                    startActivity(
                        Intent(this@BrowserActivity, MainActivity::class.java)
                            .putExtra(MainActivity.TMDB_OAUTH_EXTRA, url)
                    )
                    finish()
                }
                return isOauthCallback
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
