package com.harisewak.verticalvideos.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.harisewak.verticalvideos.R
import com.harisewak.verticalvideos.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


const val TOTAL_VIDEOS = 13

@MediumTest
@HiltAndroidTest
class VideoListFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun onViewCreated_failIfVideoListNotDisplayed() {
        launchFragmentInHiltContainer<VideoListFragment> {
            val videoCount = getLoadedVideoCount()
            assertThat(videoCount == 0).isFalse()
        }
    }

    private fun Fragment.getLoadedVideoCount() =
        view!!.findViewById<RecyclerView>(R.id.rv_video_list).adapter!!.itemCount

    @Test
    fun onViewCreated_failIfVideoCountIsLessThan13() {
        launchFragmentInHiltContainer<VideoListFragment> {
            val videoCount = getLoadedVideoCount()
            assertThat(videoCount < TOTAL_VIDEOS).isFalse()
        }
    }

    /*
    * Test fails if...
    * ...currently visible view does not play video
    * ...currently invisible view plays the video
    * ...incorrect video is played
    * ...video is not played from previous position
    * ...wrong current play positions (-ve OR greater than total duration)are not handled
    * ...video does not repeat itself
    * */

}