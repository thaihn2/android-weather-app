package com.example.weathersample.ui.main

import androidx.lifecycle.ViewModelProvider
import com.example.base.data.prefs.AppPreferenceHelper
import com.example.base.repository.weather.WeatherRepository
import com.example.base.ui.ViewModelProviderFactory
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    internal fun provideMainViewModel(weatherRepository: WeatherRepository, appPreferenceHelper: AppPreferenceHelper): MainViewModel {
        return MainViewModel(weatherRepository, appPreferenceHelper)
    }

    @Provides
    internal fun mainViewModelProvider(viewModel: MainViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(viewModel)
    }
}
