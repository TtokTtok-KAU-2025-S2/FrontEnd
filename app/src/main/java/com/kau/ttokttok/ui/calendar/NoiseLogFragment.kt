package com.kau.ttokttok.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kau.ttokttok.R
import com.kau.ttokttok.databinding.FragmentMyProfileBinding
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class NoiseLogFragment : Fragment() {

    private val viewModel: NoiseLogViewModel by activityViewModels()
    private lateinit var adapter: NoiseLogAdapter

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

        // 초기 로드 시 오늘 날짜를 선택
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
        viewModel.selectDate(today)
    }

    private fun setupRecyclerView() {
        adapter = NoiseLogAdapter(
            onDeleteClick = { log -> viewModel.deleteLog(log.id!!) },
            onEditClick = { log -> /* TODO: 수정 화면으로 이동 */ },
            onItemCheckChanged = { _, _ -> updateSelectionCount() }
        )

        binding.rvLogs.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@NoiseLogFragment.adapter
        }
    }

    private fun setupCalendar() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            viewModel.selectDate(calendar.time)
        }
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, NoiseMeasurementFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupReportButton() {
        binding.btnCreateReport.setOnClickListener {
            val selectedLogs = adapter.getSelectedLogs()

            if (selectedLogs.isEmpty()) {
                Toast.makeText(requireContext(), "리포트로 만들 일기를 선택해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            selectedLogs.forEach { log ->
                viewModel.toggleReportStatus(log)
            }

            adapter.clearSelection()
            Toast.makeText(requireContext(), "${selectedLogs.size}개의 리포트가 생성되었습니다", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedLogs.collect { logs ->
                adapter.submitList(logs) {
                    // submitList가 완료된 후에 카운트를 업데이트
                    updateSelectionCount()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.noiseLogs.collect { logs ->
                updateStats(logs)
            }
        }
    }

    private fun updateSelectionCount() {
        val selectedCount = adapter.getSelectedLogs().size
        binding.tvListSummary.text = if (selectedCount > 0) {
            "${selectedCount}개 선택됨"
        } else {
            "${adapter.currentList.size}개 항목"
        }
    }

    private fun updateStats(logs: List<com.kau.ttokttok.domain.model.NoiseLog>) {
        binding.tvTotalCount.text = "${logs.size}건"

        val thisMonth = Calendar.getInstance().get(Calendar.MONTH)
        val monthCount = logs.count { log ->
            val cal = Calendar.getInstance().apply { time = log.measuredAt }
            cal.get(Calendar.MONTH) == thisMonth
        }
        binding.tvMonthCount.text = "${monthCount}건"

        val avgDb = if (logs.isNotEmpty()) {
            logs.map { it.avgDecibel }.average().toInt()
        } else {
            0
        }
        binding.tvAvgDb.text = "$avgDb"
    }

    override fun onResume() {
        super.onResume()
        // 화면으로 돌아올 때 데이터 새로 고침
        viewModel.loadAllLogs()
        viewModel.selectDate(viewModel.selectedDate.value)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
