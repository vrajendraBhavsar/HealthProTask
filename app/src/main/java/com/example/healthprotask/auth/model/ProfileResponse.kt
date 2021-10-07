package com.example.healthprotask.auth.model

data class ProfileResponse(
    val user: User,
    //to handle error case
    val success: Boolean?,
    val errors: List<Error>?
) {
    data class User(
        val age: Int?,
        val ambassador: Boolean?,
        val avatar: String?,
        val avatar150: String?,
        val avatar640: String?,
        val averageDailySteps: Int?,
        val challengesBeta: Boolean?,
        val clockTimeDisplayFormat: String?,
        val corporate: Boolean?,
        val corporateAdmin: Boolean?,
        val dateOfBirth: String?,
        val displayName: String?,
        val displayNameSetting: String?,
        val distanceUnit: String?,
        val encodedId: String?,
        val features: Features,
        val firstName: String?,
        val foodsLocale: String?,
        val fullName: String?,
        val gender: String?,
        val glucoseUnit: String?,
        val height: Double?,
        val heightUnit: String?,
        val isBugReportEnabled: Boolean?,
        val isChild: Boolean?,
        val isCoach: Boolean?,
        val languageLocale: String?,
        val lastName: String?,
        val legalTermsAcceptRequired: Boolean?,
        val locale: String?,
        val memberSince: String?,
        val mfaEnabled: Boolean?,
        val offsetFromUTCMillis: Int?,
        val sdkDeveloper: Boolean?,
        val sleepTracking: String?,
        val startDayOfWeek: String?,
        val strideLengthRunning: Double?,
        val strideLengthRunningType: String?,
        val strideLengthWalking: Double?,
        val strideLengthWalkingType: String?,
        val swimUnit: String?,
        val timezone: String?,
        val topBadges: List<Any>?,
        val waterUnit: String?,
        val waterUnitName: String?,
        val weight: Double?,
        val weightUnit: String?,
    ) {
        data class Features(
            val exerciseGoal: Boolean?
        )
    }
    data class Error(
        val errorType: String?,
        val message: String?
    )
}