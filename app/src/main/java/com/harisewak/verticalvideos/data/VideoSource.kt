package com.harisewak.verticalvideos.data

interface VideoSource {

    fun getVideos(): List<Video>
}