package com.myon2019.islelink.shared

import android.net.Uri
import androidx.core.net.toUri

object UriBuilderExtension {
    fun Uri.Builder.convertQueryParametersToFragment(): Uri.Builder {
        return this
            .encodedFragment(this.build().encodedQuery)
            .clearQuery()
    }

    fun Uri.getFragmentParameter(key: String): String? {
        val fragment = this.encodedFragment ?: return null

        return "dummy://dummy?$fragment".toUri().getQueryParameter(key)
    }
}