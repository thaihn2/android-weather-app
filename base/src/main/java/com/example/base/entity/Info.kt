package com.example.base.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "info")
data class Info(

        @PrimaryKey(autoGenerate = true) val id: Int = 0,

        val title: String,

        val content: String
)
