package com.harisewak.verticalvideos.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import com.harisewak.verticalvideos.data.VideoListRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@SmallTest
class VideoListViewModelTest {

    @Inject
    lateinit var repository: VideoListRepository

    lateinit var videoListViewModel: VideoListViewModel

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        hiltRule.inject()
        videoListViewModel = VideoListViewModel(repository)
    }


    @Test
    fun videoList_failsIfNotReturned() {
        val result = videoListViewModel.videoList.getOrAwaitValue()
        assertThat(result).isNotEmpty()
    }

}