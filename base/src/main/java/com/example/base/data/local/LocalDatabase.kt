package com.example.base.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.base.data.local.dao.WeatherDao
import com.example.base.entity.Currently
import com.example.base.entity.DataDaily
import com.example.base.entity.DataHourly
import com.example.base.entity.Info

@Database(
        entities = [
            Currently::class,
            DataDaily::class,
            DataHourly::class,
            Info::class
        ],
        version = 1
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
}
