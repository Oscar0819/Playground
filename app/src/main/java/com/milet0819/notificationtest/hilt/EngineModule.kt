package com.milet0819.notificationtest.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object EngineModule {
    @Provides
    fun provideEngine(): Engine {
        return Engine()
    }
}

class Engine {
    fun start() = "Engine started"
}