package com.harisewak.verticalvideos.data

import android.content.Context
import com.harisewak.verticalvideos.util.loadVideosFromAssets
import dagger.hilt.android.qualifiers.ApplicationContext

class DefaultVideoSource(private val context: Context): VideoSource {

    override fun getVideos() = loadVideosFromAssets(context)

}
