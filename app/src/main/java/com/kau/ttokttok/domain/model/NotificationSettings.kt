package com.example.noisepreventionapp.model

data class NotificationSettings(
    var allNotifications: Boolean = true,
    var noiseDetection: Boolean = true,
    var priorConsent: Boolean = true,
    var comments: Boolean = true,
    var announcements: Boolean = true,
    var doNotDisturbStart: String = "22:00",
    var doNotDisturbEnd: String = "07:00"
)
