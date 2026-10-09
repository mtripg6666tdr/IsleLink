package com.myon2019.islelink.next

import android.app.Activity
import android.content.Intent
import android.content.pm.CrossProfileApps
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LinkDispatcherActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val originalUrl = this.intent?.dataString

        if (originalUrl.isNullOrEmpty()) {
            this.finish()

            return
        }

        val crossProfileApps = getSystemService(CrossProfileApps::class.java)
        val userHandles = crossProfileApps.targetUserProfiles

        val intent = Intent(this, LinkReceiverActivity::class.java).apply {
            data = originalUrl.toUri()
        }

        try {
            crossProfileApps.startActivity(intent, userHandles.first(), null)

            Toast.makeText(applicationContext, getString(R.string.using_app_outside_of_work_profile), Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception) {
            Toast.makeText(applicationContext, getString(R.string.failed_to_open), Toast.LENGTH_SHORT).show()
            Toast.makeText(applicationContext, getString(R.string.how_to_resolve), Toast.LENGTH_LONG).show()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (!this.isFinishing && !this.isDestroyed) {
                this.finish()
            }
        }, 500L)
    }
}