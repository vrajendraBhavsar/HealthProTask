package com.example.healthprotask.auth.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserActivitiesResponse(
    val activities: List<Activity>,
    val goals: Goals,
    val summary: Summary
) : Serializable {
    data class Activity(
        val activityId: Int,
        val activityParentId: Int,
        val activityParentName: String,
        val calories: Int,
        val description: String,
        val distance: Double,
        val duration: Int,
        val hasActiveZoneMinutes: Boolean,
        val hasStartTime: Boolean,
        val isFavorite: Boolean,
        val lastModified: String,
        val logId: Long,
        val name: String,
        val startDate: String,
        val startTime: String,
        val steps: Int
    )

    data class Goals(
        val activeMinutes: Int,
        val caloriesOut: Int,
        val distance: Double,
        val steps: Int
    )

    data class Summary(
        val activeScore: Int,
        val activityCalories: Int,
        val caloriesBMR: Int,
        val caloriesOut: Int,
        val distances: List<Distance>,
        val fairlyActiveMinutes: Int,
        val lightlyActiveMinutes: Int,
        val marginalCalories: Int,
        val sedentaryMinutes: Int,
        val steps: Int,
        val veryActiveMinutes: Int
    ) {
        data class Distance(
            val activity: String,
            val distance: Double
        )
    }
}