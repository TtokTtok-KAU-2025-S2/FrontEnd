package com.kau.ttokttok.ui.navigation

// Application 에서 이동이 필요한 Destination 모음
enum class Destination(val fragmentName: String) {
    LOGIN("login"),
    REGISTER("register"),
    MAIN("main"),

    CALENDAR("calendar"),
    NOISE_VOTE("noiseVote"),
    MONTH_REPORT("monthReport"),
    PRECONSIDERATION("preConsideration"),
    WRITING_PRECONSIDERATION("writingPreConsideration"),
    COMMUNITY("community"),
    WRITING_COMMUNITY("writingCommunity"),
    SETTING("setting"),
    NOTIFICATION("notification")
}