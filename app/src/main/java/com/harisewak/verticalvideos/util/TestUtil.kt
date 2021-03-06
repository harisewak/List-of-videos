package com.harisewak.verticalvideos.util

import android.util.Log

const val TAG = "VerticalVideosApp"

const val ENABLE = true

fun logd(message: String) {
    if (ENABLE) Log.d(TAG, message)
}