package com.example.base.repository.weather

import com.example.base.data.network.WeatherApi
import com.example.base.data.network.WeatherRemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WeatherRepositoryModule {

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(weatherApi: WeatherApi): WeatherDataSource {
        return WeatherRemoteDataSource(weatherApi)
    }
}
