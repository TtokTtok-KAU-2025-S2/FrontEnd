package com.kau.ttokttok

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        // 1️⃣ XML에 있는 "+" 버튼을 찾아서 연결
        val addButton = findViewById<TextView>(R.id.addButton)

        // 2️⃣ 클릭 시 PostActivity로 이동
        addButton.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            startActivity(intent)
        }
    }
}
