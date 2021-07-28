package com.harisewak.verticalvideos.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.harisewak.verticalvideos.util.loadVideosFromAssets
import dagger.hilt.android.qualifiers.ApplicationContext

class TestVideoSource(): VideoSource {



    // In production, we mock or create a fake Video source
    // here we use the same implementation for demo purposes
    override fun getVideos() = loadVideosFromAssets(ApplicationProvider.getApplicationContext())

}
