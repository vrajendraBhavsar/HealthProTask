package com.example.healthprotask.auth.model

import com.google.gson.annotations.SerializedName

data class DistanceResponse(
    @SerializedName("activities-distance")
    val activitiesDistance: MutableList<ActivitiesDistance>
) {
    data class ActivitiesDistance(
        val dateTime: String,
        val value: String
    )
}