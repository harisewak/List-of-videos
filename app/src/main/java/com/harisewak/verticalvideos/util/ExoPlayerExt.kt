package com.harisewak.verticalvideos.util

import com.google.android.exoplayer2.ExoPlayer

fun ExoPlayer.enableVolume() {
    volume = 1F
}

fun ExoPlayer.disableVolume() {
    volume = 0F
}