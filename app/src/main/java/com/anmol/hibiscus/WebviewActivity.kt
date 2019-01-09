package com.anmol.hibiscus

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import kotlinx.android.synthetic.main.activity_webview.*
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient



class WebviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        webview.isFocusable = true
        webview.isFocusableInTouchMode = true
        webview.settings.javaScriptEnabled = true
        webview.settings.javaScriptCanOpenWindowsAutomatically = true
        webview.settings.loadsImagesAutomatically = true
        webview.settings.setSupportZoom(true)
        webview.settings.builtInZoomControls = true
        //webview.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        //webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        webview.settings.domStorageEnabled = true
        webview.settings.databaseEnabled = true
        webview.settings.loadsImagesAutomatically = true
        //webview.getSettings().setAppCacheEnabled(true);
        webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webview.settings.useWideViewPort = true
//        webview.settings.textZoom = 175

        webview.run {
            setInitialScale(1)
            loadUrl(intent.getStringExtra("weburl"))
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.loadUrl(request.url.toString())
                    }
                    return false
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
