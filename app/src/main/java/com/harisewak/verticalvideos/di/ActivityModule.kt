package com.harisewak.verticalvideos.di

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Singleton

@InstallIn(ActivityRetainedComponent::class)
@Module
class ActivityModule {

    @Provides
    @ActivityRetainedScoped
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer =
        SimpleExoPlayer.Builder(context).build()
}