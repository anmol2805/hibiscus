package com.anmol.hibiscus.fragments

import android.app.Fragment
import android.os.Build
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

import com.anmol.hibiscus.R

class privacypolicy : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.privacypolicy, container, false)
        activity?.title = "Privacy Policy"
        val privacyweb = vi.findViewById<WebView>(R.id.privacyweb)
        privacyweb.isFocusable = true
        privacyweb.isFocusableInTouchMode = true
        privacyweb.settings.javaScriptEnabled = true
        privacyweb.settings.javaScriptCanOpenWindowsAutomatically = true
        privacyweb.settings.loadsImagesAutomatically = true
        privacyweb.settings.setSupportZoom(true)
        privacyweb.settings.builtInZoomControls = true
        //privacyweb.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        //privacyweb.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        privacyweb.settings.domStorageEnabled = true
        privacyweb.settings.databaseEnabled = true
        privacyweb.settings.loadsImagesAutomatically = true
        //privacyweb.getSettings().setAppCacheEnabled(true);
        privacyweb.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        privacyweb.settings.useWideViewPort = true
        privacyweb.run {
            setInitialScale(1)
            loadUrl("https://firebasestorage.googleapis.com/v0/b/iiitcloud-e9d6b.appspot.com/o/canopypp.html?alt=media&token=e9092095-8d94-40e3-9c59-43ce6f3d006e")
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.loadUrl(request.url.toString())
                    }
                    return false
                }
            }
        }
        return vi
    }
}
