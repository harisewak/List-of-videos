package com.harisewak.verticalvideos.di

import android.content.Context
import com.harisewak.verticalvideos.data.DefaultVideoSource
import com.harisewak.verticalvideos.data.TestVideoSource
import com.harisewak.verticalvideos.data.VideoListRepository
import com.harisewak.verticalvideos.data.VideoSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class TestAppModule {

    @Provides
    @Named("test_video_source")
    fun provideVideoSource(): VideoSource = TestVideoSource()

    @Provides
    fun provideVideoListRepository(@Named("test_video_source") videoSource: VideoSource) =
        VideoListRepository(videoSource)

}