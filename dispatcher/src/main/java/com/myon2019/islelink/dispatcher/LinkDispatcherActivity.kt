package com.myon2019.islelink.dispatcher

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.myon2019.islelink.shared.Constants
import com.myon2019.islelink.shared.UriBuilderExtension.convertQueryParametersToFragment
import com.myon2019.islelink.shared.UriBuilderExtension.getFragmentParameter

class LinkDispatcherActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val originalUrl = this.intent?.dataString

        if (originalUrl.isNullOrEmpty()) {
            this.finish()

            return
        }

        val originalUri = originalUrl.toUri()

        if (originalUri.host == Constants.BRIDGE_HOST_NAME) {
            Log.w(LinkDispatcherActivity::class.java.simpleName, "Received an URL for bridge: $originalUrl. This is not expected. Showing a chooser for sharing instead.")

            this.showSendSheet(originalUri.getFragmentParameter("url") ?: originalUrl)
        } else {
            this.handleOriginalUrl(originalUrl)
        }
    }

    fun handleOriginalUrl(originalUrlString: String) {
        try {
            Log.i(LinkDispatcherActivity::class.java.simpleName, "Dispatching the URL using the Custom scheme strategy.")

            this.dispatchBridgeUrlIntent(originalUrlString, "islelink")
        }
        catch (ie: Exception){
            Log.w(LinkDispatcherActivity::class.java.simpleName, "Failed to dispatch the URL. Attempting to use the HTTPS bridge strategy instead.", ie)

            try {
                this.dispatchBridgeUrlIntent(originalUrlString)
            }
            catch (he: Exception) {
                this.showToast("Failed to dispatch the URL. Showing a chooser for sharing alternatively.")

                Log.e(LinkDispatcherActivity::class.java.simpleName, "Failed to dispatch the URL", he)

                this.showSendSheet(originalUrlString)
            }
        }

        this.delayedFinish()
    }

    fun dispatchBridgeUrlIntent(originalUrl: String, scheme: String = "https") {
        val bridgeUri = Constants.BRIDGE_URL
            .toUri()
            .buildUpon()
            .scheme(scheme)
            .appendQueryParameter("url", originalUrl)
            .convertQueryParametersToFragment()
            .build()

        Log.v(LinkDispatcherActivity::class.java.simpleName, "Dispatching bridge URL: $bridgeUri")

        val forwardIntent = Intent(Intent.ACTION_VIEW, bridgeUri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        startActivity(forwardIntent)
    }

    fun showSendSheet(originalUrl: String) {
        val chooserIntent = Intent(Intent.ACTION_SEND).apply {
            setType("text/plain")
            putExtra(Intent.EXTRA_TEXT, originalUrl)
        }

        val chooser = Intent.createChooser(chooserIntent, "Open URL with")

        try {
            startActivity(chooser)
        }
        catch (e: Exception) {
            Log.e(LinkDispatcherActivity::class.java.simpleName, "Failed to show chooser", e)
        }

        this.delayedFinish()
    }

    fun showToast(message: String) {
        Toast
            .makeText(applicationContext, message, Toast.LENGTH_SHORT)
            .show()
    }

    fun delayedFinish() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (!this.isFinishing && !this.isDestroyed) {
                this.finish()
            }
        }, 500L)
    }
}