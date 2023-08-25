package test.zadanie.app.com

import android.app.DownloadManager
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var sharedPreferences: SharedPreferences
    private var lastLoadedUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        sharedPreferences = getSharedPreferences("WebViewPreferences", Context.MODE_PRIVATE)

        webView = findViewById(R.id.webView)
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.allowFileAccess = true
        webSettings.allowContentAccess = true

        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        val cookieManager: CookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)

        webView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val request = DownloadManager.Request(Uri.parse(url))
                .setMimeType(mimetype)
                .setTitle("File Download")
                .setDescription("Downloading...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    URLUtil.guessFileName(url, contentDisposition, mimetype)
                )

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)


                saveUrl(url)
            }
        }






        val url = intent.getStringExtra("url")
        if (!url.isNullOrEmpty()) {
            webView.loadUrl(url)
        } else {
            lastLoadedUrl = sharedPreferences.getString("lastLoadedUrl", null)
            if (!lastLoadedUrl.isNullOrEmpty()) {
                webView.loadUrl(lastLoadedUrl!!)
            }
        }
    }

    private fun saveUrl(url: String) {
        lastLoadedUrl = url
        val editor = sharedPreferences.edit()
        editor.putString("lastLoadedUrl", lastLoadedUrl)
        editor.apply()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("lastLoadedUrl", lastLoadedUrl)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        lastLoadedUrl = savedInstanceState.getString("lastLoadedUrl")
    }

    override fun onResume() {
        super.onResume()


        lastLoadedUrl = sharedPreferences.getString("lastLoadedUrl", null)
        if (!lastLoadedUrl.isNullOrEmpty()) {
            webView.loadUrl(lastLoadedUrl!!)
        }
    }

    override fun onPause() {
        super.onPause()


        val editor = sharedPreferences.edit()
        editor.putString("lastLoadedUrl", lastLoadedUrl)
        editor.apply()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}



