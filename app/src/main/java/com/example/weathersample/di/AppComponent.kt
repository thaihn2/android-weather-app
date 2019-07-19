package com.example.weathersample.di

import android.app.Application
import com.example.weathersample.MainApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidInjectionModule::class,
            AppModule::class,
            ActivityBuilder::class
        ]
)
interface AppComponent {

    @Component.Builder
    interface MainBuilder {

        @BindsInstance
        fun application(application: Application): MainBuilder

        fun build(): AppComponent
    }

    fun inject(application: MainApplication)
}
