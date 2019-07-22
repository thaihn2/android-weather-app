package com.example.base.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.base.entity.Currently
import com.example.base.entity.DataDaily
import com.example.base.entity.DataHourly
import com.example.base.entity.Info
import io.reactivex.Observable

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCurrently(currently: Currently)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDaily(daily: List<DataDaily>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveHourly(hourly: List<DataHourly>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveInfo(info: List<Info>)

    @Query("SELECT * FROM currently")
    fun queryCurrently(): Observable<List<Currently>>

    @Query("SELECT * FROM hourly")
    fun queryHourly(): Observable<List<DataHourly>>

    @Query("SELECT * FROM daily")
    fun queryDaily(): Observable<List<DataDaily>>

    @Query("SELECT * FROM info")
    fun queryInfo(): Observable<List<Info>>

    @Query("DELETE FROM currently")
    fun clearCurrently()

    @Query("DELETE FROM daily")
    fun clearDaily()

    @Query("DELETE FROM hourly")
    fun clearHourly()

    @Query("DELETE FROM info")
    fun clearInfo()
}
