package com.kau.ttokttok.ui.calendar

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kau.ttokttok.R
import com.kau.ttokttok.databinding.FragmentMyProfileBinding
import com.kau.ttokttok.ui.navigation.Destination
import com.kau.ttokttok.ui.navigation.navigateTo
import kotlinx.coroutines.launch
import java.util.Calendar

class NoiseLogFragment : Fragment() {

    private val viewModel: NoiseLogViewModel by activityViewModels()
    private lateinit var adapter: NoiseLogAdapter

    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

    // FAB 애니메이션을 위한 Handler
    private val handler = Handler(Looper.getMainLooper())
    private val flipAnimationRunnable = object : Runnable {
        override fun run() {
            // 3D 동전 뒤집기 애니메이션
            startFlipAnimation()

            // 10초 후에 다시 실행
            handler.postDelayed(this, 10000)
        }
    }

    private fun startFlipAnimation() {
        val fab = binding.fabAdd

        // 3D 효과를 위한 카메라 거리 설정 (값이 클수록 원근감이 줄어듦)
        val scale = resources.displayMetrics.density
        fab.cameraDistance = 8000 * scale

        // Y축을 기준으로 180도 회전 (첫 번째 반쪽)
        val rotateOut = ObjectAnimator.ofFloat(fab, "rotationY", 0f, 90f).apply {
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()
        }

        // Y축을 기준으로 180도 회전 (두 번째 반쪽)
        val rotateIn = ObjectAnimator.ofFloat(fab, "rotationY", -90f, 0f).apply {
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()
        }

        // 약간 위로 튀어오르는 효과
        val jumpUp = ObjectAnimator.ofFloat(fab, "translationY", 0f, -30f).apply {
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()
        }

        val jumpDown = ObjectAnimator.ofFloat(fab, "translationY", -30f, 0f).apply {
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()
        }

        // 애니메이션 조합
        val animatorSet = AnimatorSet()
        animatorSet.play(rotateOut).with(jumpUp)
        animatorSet.play(rotateIn).with(jumpDown).after(rotateOut)
        animatorSet.start()
    }

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

        // FAB 애니메이션 시작 (10초 후 첫 실행)
        handler.postDelayed(flipAnimationRunnable, 10000)
    }

    private fun setupRecyclerView() {
        adapter = NoiseLogAdapter(
            onDeleteClick = { log -> viewModel.deleteLog(log.id!!) },
            onEditClick = { log ->
                // 수정 화면으로 이동 (Navigator 방식)
                val bundle = Bundle().apply {
                    putString("log_id", log.id)
                    putString("noise_type", log.noiseType)
                    putString("memo", log.memo)
                    putDouble("max_db", log.maxDecibel)
                    putDouble("avg_db", log.avgDecibel)
                    putLong("measured_at", log.measuredAt.time)
                }
                findNavController().navigateTo(Destination.NOISE_LOG_FORM, bundle)
            },
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
            // 측정 화면으로 이동 (Navigator 방식)
            findNavController().navigateTo(Destination.NOISE_MEASUREMENT)
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
        // Handler 콜백 제거
        handler.removeCallbacks(flipAnimationRunnable)
        _binding = null
    }
}
