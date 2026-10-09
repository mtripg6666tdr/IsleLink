package com.myon2019.islelink.receiver

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.net.toUri
import com.myon2019.islelink.shared.UriBuilderExtension.getFragmentParameter

class LinkReceiverActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bridgeUrl = this.intent?.dataString

        if (bridgeUrl.isNullOrEmpty()){
            this.finish()
            return
        }

        Log.v(LinkReceiverActivity::class.java.simpleName, bridgeUrl)

        this.handleBridgeUrl(bridgeUrl)
    }

    fun handleBridgeUrl(bridgeUrl: String){
        val bridgeUri = bridgeUrl.toUri()

        if (bridgeUri.path != "/open") {
            Log.w(LinkReceiverActivity::class.java.simpleName, "Received an URL for bridge: $bridgeUrl. This is not expected.")

            this.finish()
            return
        }

        val originalUrl = bridgeUri.getFragmentParameter("url")

        if (originalUrl.isNullOrEmpty()){
            Log.w(LinkReceiverActivity::class.java.simpleName, "Received an empty URL for bridge: $bridgeUrl. This is not expected.")

            this.finish()
            return
        }

        Log.d(LinkReceiverActivity::class.java.simpleName, "Starting activity for the received URL: $originalUrl")

        val intent = Intent(Intent.ACTION_VIEW, originalUrl.toUri())

        startActivity(intent);

        Handler(Looper.getMainLooper()).postDelayed({
            if (!this.isFinishing && !this.isDestroyed) {
                this.finish()
            }
        }, 500L)
    }
}