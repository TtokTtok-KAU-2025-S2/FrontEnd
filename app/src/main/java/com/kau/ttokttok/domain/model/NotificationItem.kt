package com.example.noisepreventionapp.model

data class NotificationItem(
    val id: Long,
    val title: String,
    val message: String,
    val timeAgo: String,
    var isRead: Boolean,
    var isNew: Boolean,
    val iconType: String
)
