package com.myon2019.islelink.next

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LinkReceiverActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val originalUrl = this.intent?.dataString

        if (originalUrl.isNullOrEmpty()) {
            this.finish()
            return
        }

        val intent = Intent(Intent.ACTION_VIEW, originalUrl.toUri())

        startActivity(intent)
    }
}