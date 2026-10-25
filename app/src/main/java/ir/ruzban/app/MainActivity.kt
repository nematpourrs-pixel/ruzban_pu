package ir.ruzban.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView

/** Early Android WebView shell, not yet a native or production-ready app. */
class MainActivity : Activity() {
 private var browser: WebView? = null
 private val base = Uri.parse(BuildConfig.RUZBAN_URL)

 @SuppressLint("SetJavaScriptEnabled")
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  if (base.scheme != "https" || base.host.isNullOrBlank() || base.host == "example.invalid") {
   setContentView(TextView(this).apply {
    text = "آدرس HTTPS سرور روزبان تنظیم نشده است. برای ساخت نسخه آزمایشی از -PruzbanUrl استفاده کنید."
    textSize=18f; gravity=Gravity.CENTER; setPadding(32,32,32,32)
    textDirection = TextView.TEXT_DIRECTION_RTL
   })
   return
  }
  val web = WebView(this); browser = web
  web.settings.javaScriptEnabled = true
  web.settings.domStorageEnabled = true
  web.settings.allowFileAccess = false
  web.settings.allowContentAccess = false
  web.settings.javaScriptCanOpenWindowsAutomatically = false
  web.webChromeClient = WebChromeClient()
  web.webViewClient = object : WebViewClient() {
   override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
    val uri = request.url
    val prefix = base.path.orEmpty().let { if (it.endsWith('/')) it else "$"+"it/" }
    if (uri.scheme=="https" && uri.host==base.host && uri.path.orEmpty().startsWith(prefix)) return false
    if (uri.scheme=="https" || uri.scheme=="tel" || uri.scheme=="mailto")
     try { startActivity(Intent(Intent.ACTION_VIEW,uri)) } catch (_:Exception) {}
    return true
   }
   override fun onReceivedSslError(view:WebView?,handler:SslErrorHandler?,error:SslError?) {
    handler?.cancel()
   }
  }
  setContentView(web,ViewGroup.LayoutParams(-1,-1))
  if(savedInstanceState==null) web.loadUrl(base.toString()) else web.restoreState(savedInstanceState)
 }
 @Deprecated("Browser navigation")
 override fun onBackPressed() {
  if(browser?.canGoBack()==true) browser?.goBack() else super.onBackPressed()
 }
 override fun onSaveInstanceState(outState:Bundle) {
  browser?.saveState(outState); super.onSaveInstanceState(outState)
 }
 override fun onDestroy() {
  browser?.destroy();browser=null;super.onDestroy()
 }
}
