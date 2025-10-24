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
        Destination.SETTING -> TODO()
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