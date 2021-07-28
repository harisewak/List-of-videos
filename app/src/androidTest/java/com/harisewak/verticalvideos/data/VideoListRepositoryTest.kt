package com.harisewak.verticalvideos.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

/*
    * Test should fail if...
    * ...empty list is returned
    * ...video items is NOT equal to 13
    * */

@SmallTest
@HiltAndroidTest
class VideoListRepositoryTest {

     @Inject
     @Named("test_video_source")
     lateinit var videoSource: VideoSource

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var repository: VideoListRepository

    @Before
    fun setup() {
        hiltRule.inject()
        repository = VideoListRepository(videoSource)
    }

    @Test
    fun getVideos_failsWhenVideoListIsEmpty() {
        val result = repository.getListOfVideos()
        assertThat(result).isNotEmpty()
    }

    @Test
    fun getVideos_failsWhenVideoListIsNOTEqualTo13() {
        val result = repository.getListOfVideos()
        assertThat(result.size).isEqualTo(13)
    }

}