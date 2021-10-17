package com.example.healthprotask.auth.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserActivitiesResponse(
    val activities: MutableList<Activity>,
    val pagination: Pagination
): Serializable {
    data class Activity(
        val activeDuration: Int,
        val activityLevel: List<ActivityLevel>,
        val activityName: String,
        val activityTypeId: Int,
        val calories: Int,
        val distance: Double,
        val distanceUnit: String,
        val duration: Int,
        val hasActiveZoneMinutes: Boolean,
        val lastModified: String,
        val logId: Long,
        val logType: String,
        val manualValuesSpecified: ManualValuesSpecified,
        val originalDuration: Int,
        val originalStartTime: String,
        val pace: Double,
        val speed: Double,
        val startTime: String,
        val steps: Int
    ) {
        data class ActivityLevel(
            val minutes: Int,
            val name: String
        )

        data class ManualValuesSpecified(
            val calories: Boolean,
            val distance: Boolean,
            val steps: Boolean
        )
    }

    data class Pagination(
        val beforeDate: String,
        val limit: Int,
        val next: String,
        val offset: Int,
        val previous: String,
        val sort: String
    )
}