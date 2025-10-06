package com.kau.ttokttok.ui.step2

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kau.ttokttok.databinding.ActivityStep2CategoryBinding

class NoiseInquiryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStep2CategoryBinding
    private val viewModel: NoiseInquiryViewModel by viewModels()
    private lateinit var responseAdapter: ResponseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStep2CategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView(){
    }

    private fun setupClickListeners(){

    }


    private fun observeViewModel(){

    }

}