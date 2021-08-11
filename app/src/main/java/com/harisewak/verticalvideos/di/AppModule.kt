package com.harisewak.verticalvideos.di

import android.content.Context
import com.harisewak.verticalvideos.data.DefaultVideoSource
import com.harisewak.verticalvideos.data.VideoListRepository
import com.harisewak.verticalvideos.data.VideoSource
import com.harisewak.verticalvideos.viewmodel.VideoListViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    @Named("default_video_source")
    fun provideVideoSource(@ApplicationContext context: Context): VideoSource =
        DefaultVideoSource(context)

    @Provides
    @Singleton
    fun provideVideoListViewModel(repository: VideoListRepository) =
        VideoListViewModel.Companion.VideoListViewModelFactory(repository)
            .create(VideoListViewModel::class.java)

}