package com.kau.ttokttok.data.remote.dto.community.req

data class CreatePostCommunityReq(
    val title: String,
    val content: String,
    // TODO: 추후 바이너리 파일로 변경
    val noticePicture: String?
)
