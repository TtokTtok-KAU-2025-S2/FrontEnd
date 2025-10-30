package com.kau.ttokttok.domain.model.setting

data class NotificationSetting(
    val canAlarm: Boolean,
    val canNoiseCheckAlarm: Boolean,
    val canNotificationBoardAlarm: Boolean,
    val canFreeBoardAlarm: Boolean
)