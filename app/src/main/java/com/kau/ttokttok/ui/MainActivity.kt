package com.kau.ttokttok.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kau.ttokttok.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        // 메인 홈, 내비게이션이 완성되기 전까지는 show 안의 Fragment를 바꿔서 테스트
        if (savedInstanceState == null) {
            show(ComposeFragment.newInstance(), addToBackStack = false)
        }
    }
    fun show(fragment: Fragment, addToBackStack: Boolean = true) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .apply { if (addToBackStack) addToBackStack(fragment::class.java.simpleName)}
            .commit()
    }
}