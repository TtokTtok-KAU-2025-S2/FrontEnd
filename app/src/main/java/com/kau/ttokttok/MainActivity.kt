package com.kau.ttokttok

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        val addButton = findViewById<TextView>(R.id.addButton)

        addButton.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            startActivityForResult(intent, 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val title = data?.getStringExtra("title")
            val content = data?.getStringExtra("content")
            val category = data?.getStringExtra("category")

            Toast.makeText(this, "[$category] $title 등록 완료!", Toast.LENGTH_LONG).show()
        }
    }
}
