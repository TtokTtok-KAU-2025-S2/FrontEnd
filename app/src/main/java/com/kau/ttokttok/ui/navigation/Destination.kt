package com.kau.ttokttok.ui.navigation

// Application 에서 이동이 필요한 Destination 모음
enum class Destination(val fragmentName: String) {
    LOGIN("login"),
    REGISTER("register"),

    MAIN("main"),

    NOISE_VOTE("noiseVote"),
    NOISE_VOTE_DETAIL("noiseVoteDetail"),

    MONTH_REPORT("monthReport"),

    PRECONSIDERATION("preConsideration"),
    WRITING_PRECONSIDERATION("writingPreConsideration"),
    PRECONSIDERATION_DETAIL("preConsiderationDetail"),

    COMMUNITY("community"),
    WRITING_COMMUNITY("writingCommunity"),
    COMMUNITY_DETAIL("communityDetail"),
    SETTING("setting"),
    NOTIFICATION("notification"),
    NOISE_LOG("noiseLogFragment"),
    NOISE_LOG_FORM("noiseLogFormFragment"),
    NOISE_MEASUREMENT("noiseMeasurementFragment"),
    EDIT_PROFILE("editProfileFragment"),
    CHANGE_NICKNAME("changeNicknameFragment"),
    CHANGE_ADDRESS("changeAddressFragment"),
    CHANGE_PASSWORD("changePasswordFragment")
}