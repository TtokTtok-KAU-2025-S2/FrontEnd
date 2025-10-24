package com.kau.ttokttok.ui.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.kau.ttokttok.R

fun NavController.navigateTo(
    dest: Destination,
    args: Bundle? = null,
    builder: (androidx.navigation.NavOptionsBuilder.() -> Unit)? = null
) {
    val options = builder?.let { navOptions(it) }

    /**
     * 네비게이션 진입점(확장 함수). (Notion에 사용법 올려 놓겠습니다.)
     *
     * # 목적
     * - 컴포저블/프래그먼트 어디서든 목적지(Destination)만 넘기면 이동할 수 있도록 하는 공용 API.
     * - 실제 R.id.* 매핑과 NavOptions 정책(백스택/중복방지/상태복원 등)은 이 함수 내부에서 일관되게 관리.
     *
     * # 사용 원칙
     * 0) 사용 전, **res/navigation/nav_graph.xml** 에 해당 fragment를 반드시 등록해야 한다.
     *    (등록하지 않으면 `IllegalArgumentException: unknown destination id` 발생)
     *
     * 1) NavController는 **UI 레이어(Activity/Fragment)** 에서만 호출한다.
     *    - ViewModel에서는 호출 금지 (UI 의존성 침범).
     *    - 컴포저블에서는 콜백(`onNavigate: (Destination) -> Unit`)을 통해 Fragment로 위임한다.
     *
     * 2) 인자/옵션이 필요할 땐 `args`/`builder`를 사용한다.
     *    - `args`: `bundleOf("key" to value)` 로 전달.
     *    - `builder`: `navOptions { … }` DSL로 `popUpTo`, `launchSingleTop`, `restoreState` 등 정책 지정.
     *
     * # 기본 예시
     * // 0) 가장 단순한 이동 (push)
     * findNavController().navigateTo(Destination.SETTING)
     *
     * // 1) 인자 전달이 필요한 경우
     * val args = bundleOf("itemId" to 42L)
     * findNavController().navigateTo(Destination.STEP2, args)
     *
     * # Compose 쪽 사용 패턴 (권장)
     * // Composable
     * Button(
     *   onClick = { onNavigate(Destination.COMMUNITY) }
     * )
     *
     * // Fragment
     * setContent {
     *   MainRoute(
     *     onNavigate = { dest -> findNavController().navigateTo(dest) },
     *     onBack = { findNavController().popBackStack() }
     *   )
     * }
     *
     * # 주의사항
     * - 이 함수 안의 `when(dest)` 매핑만 수정하면 전역 정책이 반영된다.
     * - 사용 전, 반드시 해당 fragment를 nav_graph.xml 에 추가해야 한다.
     * - `builder`를 쓰면 **해당 호출부의 NavOptions가 우선 적용**된다.
     * - NavController를 직접 ViewModel에서 접근하지 말고, 항상 UI 레이어에서 처리한다.
     */

    when (dest) {
        Destination.LOGIN -> navigate(R.id.loginFragment, args, options)
        Destination.MAIN -> navigate(R.id.mainFragment, args, options)
        Destination.PRECONSIDERATION -> TODO()
        Destination.COMMUNITY -> TODO()
        Destination.TRUST_SCORE -> TODO()
        Destination.CARE_POINT -> TODO()
        Destination.BADGE -> TODO()
        Destination.STEP1 -> TODO()
        Destination.STEP2 -> TODO()
        Destination.STEP3 -> TODO()
        Destination.SETTING -> navigate(R.id.settingFragment, args, options)
        Destination.NOTIFICATION -> TODO()
    }
}

// Login에 성공 했을 때 사용
fun NavController.onSuccessLogin() {
    navigate (
        R.id.mainFragment,
        null,
        navOptions {
            popUpTo(R.id.loginFragment) {
                inclusive = true
            }

            launchSingleTop = true
        }
    )
}

// Logout 시 사용
fun NavController.resetToLogin() {
    navigate (
        R.id.loginFragment,
        null,
        navOptions {
            popUpTo(graph.startDestinationId) {
                inclusive = true
            }

            launchSingleTop = true
        }
    )
}