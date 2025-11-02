package com.kau.ttokttok.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kau.ttokttok.databinding.FragmentMyProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

// 소음 일기 캘린더 Fragment
// ViewBinding을 사용하여 UI 요소에 접근
@AndroidEntryPoint
class NoiseLogFragment : Fragment() {

    private val viewModel: NoiseLogViewModel by viewModels()
    private lateinit var adapter: NoiseLogAdapter

    // ViewBinding
    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupCalendar()
        setupFab()
        setupReportButton()
        observeViewModel()
        updateStats()
    }

    // RecyclerView 설정
    private fun setupRecyclerView() {
        adapter = NoiseLogAdapter(
            onDeleteClick = { log -> viewModel.deleteLog(log.id!!) },
            onEditClick = { log -> /* TODO: 수정 화면으로 이동 */ },
            onCheckChanged = { log -> viewModel.toggleReportStatus(log) }
        )

        binding.rvLogs.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@NoiseLogFragment.adapter
        }
    }

    // 캘린더 설정
    private fun setupCalendar() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            viewModel.selectDate(calendar.time)
        }
    }

    // FAB 버튼 설정 (소음 측정 추가)
    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            // TODO: 소음 측정 화면으로 이동
        }
    }

    // 리포트 생성 버튼 설정
    private fun setupReportButton() {
        binding.btnCreateReport.setOnClickListener {
            // TODO: 리포트 생성 로직 구현
        }
    }

    // ViewModel 데이터 관찰
    private fun observeViewModel() {
        // 선택된 날짜의 일기 목록 관찰
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedLogs.collect { logs ->
                adapter.submitList(logs)
                binding.tvListSummary.text = "${logs.size}개 선택됨"
            }
        }
    }

    // 통계 정보 업데이트 (총 기록, 이번 달, 평균 dB)
    private fun updateStats() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.noiseLogs.collect { logs ->
                // 총 기록 수
                binding.tvTotalCount.text = "${logs.size}건"

                // 이번 달 기록 수
                val thisMonth = Calendar.getInstance().get(Calendar.MONTH)
                val monthCount = logs.count {
                    val cal = Calendar.getInstance()
                    cal.time = it.measuredAt
                    cal.get(Calendar.MONTH) == thisMonth
                }
                binding.tvMonthCount.text = "${monthCount}건"

                // 평균 dB
                val avgDb = if (logs.isNotEmpty()) {
                    logs.map { it.avgDecibel }.average().toInt()
                } else 0
                binding.tvAvgDb.text = "$avgDb"
            }
        }
    }

    // Fragment 종료 시 ViewBinding 정리
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
