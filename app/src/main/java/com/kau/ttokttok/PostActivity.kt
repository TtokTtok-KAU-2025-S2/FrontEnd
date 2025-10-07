package com.kau.ttokttok

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PostActivity : AppCompatActivity() {
    private var selectedCategory: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        val backButton = findViewById<TextView>(R.id.backButton)
        val submitButton = findViewById<TextView>(R.id.submitButton)
        val titleInput = findViewById<EditText>(R.id.editTitle)
        val contentInput = findViewById<EditText>(R.id.editContent)

        // 카테고리 선택
        val categories = listOf(
            findViewById<TextView>(R.id.category_notice),
            findViewById<TextView>(R.id.category_community),
            findViewById<TextView>(R.id.category_suggestion),
            findViewById<TextView>(R.id.category_facility),
            findViewById<TextView>(R.id.category_emergency)
        )

        for (category in categories) {
            category.setOnClickListener {
                selectedCategory?.apply {
                    setBackgroundColor(android.graphics.Color.parseColor("#F3F3F5"))
                    setTextColor(android.graphics.Color.parseColor("#030213"))
                }

                selectedCategory = category
                category.setBackgroundColor(android.graphics.Color.parseColor("#4B33FF"))
                category.setTextColor(android.graphics.Color.WHITE)
            }
        }

        backButton.setOnClickListener { finish() }

        submitButton.setOnClickListener {
            val title = titleInput.text.toString()
            val content = contentInput.text.toString()
            val categoryName = selectedCategory?.text ?: ""

            if (title.isBlank() || content.isBlank() || categoryName.isBlank()) {
                Toast.makeText(this, "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent()
                intent.putExtra("title", title)
                intent.putExtra("content", content)
                intent.putExtra("category", categoryName)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}
