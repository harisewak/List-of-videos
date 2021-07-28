package com.harisewak.verticalvideos.util

import android.content.Context
import com.harisewak.verticalvideos.ASSET_JSON_VIDEOS
import com.harisewak.verticalvideos.data.Video
import org.json.JSONArray
import java.io.IOException


fun loadVideosFromAssets(context: Context) = read(context).toJsonArray().toList()

private fun read(context: Context) = try {
        val input = context.assets.open(ASSET_JSON_VIDEOS)
        val size = input.available()
        val buffer = ByteArray(size)
        input.read(buffer)
        input.close()
        String(buffer)

    } catch (e: IOException) {
        e.printStackTrace()
        ""
    }

private fun String.toJsonArray(): JSONArray {
    return JSONArray(this)
}

private fun JSONArray.toList(): List<Video> {

    val result = ArrayList<Video>()

    for (i in 0 until this.length()) {
        val json = this.getJSONObject(i)
        val video = Video(
            description = json.getString("description"),
            videoUrl = json.getString("video_url"),
            subtitle = json.getString("subtitle"),
            thumb = json.getString("thumb"),
            title = json.getString("title")
        )
        result.add(video)
    }

    return result
}
