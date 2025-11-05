package com.kau.ttokttok.ui.navigation

// Application 에서 이동이 필요한 Destination 모음
enum class Destination(val fragmentName: String) {
    LOGIN("login"),
    MAIN("main"),
    PRECONSIDERATION("preConsideration"),
    COMMUNITY("community"),
    TRUST_SCORE("trustScore"),
    CARE_POINT("carePoint"),
    BADGE("badge"),
    STEP1("step1"),
    STEP2("step2"),
    STEP3("step3"),
    SETTING("setting"),
    NOTIFICATION("notification")
}