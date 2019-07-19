package com.example.weathersample.ui.main

import androidx.lifecycle.ViewModelProvider
import com.example.base.repository.weather.WeatherRepository
import com.example.base.ui.ViewModelProviderFactory
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    internal fun provideMainViewModel(weatherRepository: WeatherRepository): MainViewModel {
        return MainViewModel(weatherRepository)
    }

    @Provides
    internal fun mainViewModelProvider(viewModel: MainViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(viewModel)
    }
}
