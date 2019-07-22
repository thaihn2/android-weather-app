package com.example.base.entity

import com.google.gson.annotations.SerializedName

data class Daily(

        @SerializedName("data") val data: List<DataDaily>
)
