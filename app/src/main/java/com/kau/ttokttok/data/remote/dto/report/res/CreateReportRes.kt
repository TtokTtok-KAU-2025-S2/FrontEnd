package com.kau.ttokttok.data.remote.dto.report.res

/**
 * 소음 일기 → 소음현황판 전송 응답 (POST /noise/records/{recordId}/send)
 *
 * 백엔드 동작:
 * 1. 소음 일기의 description(사용자 메모)을 기반으로 AI가 summary(요약) 자동 생성
 *    예: "아 음악소리좀 제발 너무 시끄러워"
 *    → "11월 29일 22:40경, 60초간 악기/음악 소음 발생"
 *
 * 2. 생성된 summary와 함께 소음현황판에 게시글 생성
 *
 * 3. 생성된 게시글 ID 반환
 *
 * @return 소음현황판에 생성된 게시글의 ID
 */
typealias CreateReportRes = Long

