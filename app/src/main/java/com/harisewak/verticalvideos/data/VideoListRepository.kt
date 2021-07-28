package com.harisewak.verticalvideos.data

import javax.inject.Inject
import javax.inject.Named

class VideoListRepository @Inject constructor(@Named("default_video_source") private val source: VideoSource) {

    fun getListOfVideos(): List<Video> {
        return source.getVideos()
    }

}