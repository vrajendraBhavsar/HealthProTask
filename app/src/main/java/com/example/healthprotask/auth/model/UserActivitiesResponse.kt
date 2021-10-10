package com.example.healthprotask.auth.model


import com.google.gson.annotations.SerializedName

data class UserActivitiesResponse(
    @SerializedName("lifetime")
    val lifetime: Lifetime
) {
    data class Lifetime(
        @SerializedName("total")
        val total: Total,
        @SerializedName("tracker")
        val tracker: Tracker
    ) {
        data class Total(
            @SerializedName("activeScore")
            val activeScore: Int,
            @SerializedName("caloriesOut")
            val caloriesOut: Int,
            @SerializedName("distance")
            val distance: Int,
            @SerializedName("steps")
            val steps: Int
        )

        data class Tracker(
            @SerializedName("activeScore")
            val activeScore: Int,
            @SerializedName("caloriesOut")
            val caloriesOut: Int,
            @SerializedName("distance")
            val distance: Int,
            @SerializedName("steps")
            val steps: Int
        )
    }
}