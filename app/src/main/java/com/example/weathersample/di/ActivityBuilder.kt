package com.example.weathersample.di

import com.example.weathersample.ui.main.MainActivity
import com.example.weathersample.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    fun bindMainActivity(): MainActivity
}
