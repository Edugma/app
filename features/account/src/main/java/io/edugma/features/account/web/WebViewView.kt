package io.edugma.features.account.web

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewView(context: Context): WebView(context) {

    companion object {
        private const val AUTH_TOKEN_HEADER = "setAuthToken"
    }

    var failListener: ((errorCode: Int, description: String) -> Unit)? = null
    var pageLoadedListener: (() -> Unit)? = null

    var authToken: String? = null

    init {
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        @SuppressLint("SetJavaScriptEnabled")
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true

        webViewClient = object : WebViewClient() {

            @Suppress("OverridingDeprecatedMember")
            override fun onReceivedError(
                webView: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                failListener?.invoke(errorCode, description)
            }

            override fun onReceivedError(webView: WebView, req: WebResourceRequest, error: WebResourceError) {
                failListener?.invoke(error.errorCode, error.description.toString())
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                authToken?.let(::addAuthToken)
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(webView: WebView, url: String) {
                pageLoadedListener?.invoke()
            }

            @Suppress("OverridingDeprecatedMember")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
    }

    private fun addAuthToken(token: String) {
        addLocalStorageValue(AUTH_TOKEN_HEADER, "{\"token\":\"$token\",\"jwt\":null,\"jwt_refresh\":null}")
    }

    private fun addLocalStorageValue(key: String, value: String) {
        evaluateJavascript("window.localStorage.setItem('$key','$value');", null)
    }

}
