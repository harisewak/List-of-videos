package com.harisewak.verticalvideos.data

import android.content.Context
import com.harisewak.verticalvideos.util.loadVideosFromAssets
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class VideoListRepository(private val context: Context) {

    fun getListOfVideos(): List<Video> {
        return loadVideosFromAssets(context)
    }

}