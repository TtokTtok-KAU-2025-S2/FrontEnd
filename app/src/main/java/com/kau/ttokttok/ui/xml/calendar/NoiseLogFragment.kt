package com.kau.ttokttok.ui.xml.calendar

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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import com.kau.ttokttok.R
import com.kau.ttokttok.databinding.FragmentMyProfileBinding
import com.kau.ttokttok.ui.navigation.Destination
import com.kau.ttokttok.ui.navigation.navigateTo
import dagger.hilt.android.AndroidEntryPoint

/**
 * 소음 일기 캘린더 화면
 * - 캘린더를 통해 날짜별 소음 일기 조회
 * - 소음 측정 및 일기 작성 기능
 * - 선택한 일기들로 리포트 생성
 */
@AndroidEntryPoint
class NoiseLogFragment : Fragment() {

    private val viewModel: NoiseLogViewModel by activityViewModels() // 공유 ViewModel
    private lateinit var adapter: NoiseLogAdapter

    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

    // FAB 애니메이션을 위한 Handler
    private val handler = Handler(Looper.getMainLooper())
    private val flipAnimationRunnable = object : Runnable {
        override fun run() {
            // 3D 동전 뒤집기 애니메이션 실행
            startFlipAnimation()

            // 10초 후에 다시 실행 (반복)
            handler.postDelayed(this, 10000)
        }
    }

    /**
     * FAB 3D 회전 애니메이션
     * Y축 기준 180도 회전 + 상하 점프 효과
     */
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

        // 초기 로드 시 오늘 날짜의 일기 자동 조회
        val todayCal = Calendar.getInstance()
        val today = todayCal.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
        viewModel.selectDate(today)

        // 월간 캘린더 데이터 로드는 ViewModel.init과 onResume에서 처리하므로 여기서는 생략
        // viewModel.fetchMonthlyCalendar(...)

        // FAB 애니메이션 시작 (10초 후 첫 실행)
        handler.postDelayed(flipAnimationRunnable, 10000)
    }

    /**
     * RecyclerView 설정
     * - 삭제: 해당 일기 삭제
     * - 수정: 일기 수정 화면으로 이동
     * - 체크박스: 리포트 생성용 일기 선택
     */
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

    /**
     * 캘린더 날짜 선택 이벤트
     * 선택한 날짜의 소음 일기 목록 로드
     */
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

            // 월이 바뀔 때 월간 캘린더 API 호출
            viewModel.fetchMonthlyCalendar(
                year = year,
                month = month + 1 // Calendar.MONTH는 0부터 시작
            )
        }
    }

    /**
     * FAB 클릭 이벤트
     * 소음 측정 화면으로 이동
     */
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

            val ids = selectedLogs.mapNotNull { it.id }
            viewModel.createReport(ids)

            adapter.clearSelection()
            // 성공/실패 메시지는 ViewModel의 uiMessage Flow를 통해 표시됨
        }
    }

    /**
     * ViewModel의 StateFlow 관찰
     * - selectedLogs: 선택된 날짜의 일기 목록
     * - noiseLogs: 전체 일기 목록 (통계용)
     */
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedLogs.collect { logs ->
                adapter.submitList(logs) {
                    // submitList가 완료된 후에 카운트를 업데이트
                    updateSelectionCount()
                }
            }
        }

        // 전체 일기 목록은 다른 통계/기능에 활용될 수 있음
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.noiseLogs.collect { logs ->
                // 현재는 월간/평균 계산을 서버 값으로 대체했으므로 별도 로직 없음
                updateStats(logs)
            }
        }

        // 서버 전체 소음 기록 수(total-count) 관찰
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.totalNoiseRecordCount.collectLatest { count ->
                val displayCount = count ?: 0
                binding.tvTotalCount.text = "${displayCount}건"
            }
        }

        // 서버 이번 달 소음 기록 수(monthly-count) 관찰
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.monthlyNoiseRecordCount.collectLatest { count ->
                val displayCount = count ?: 0
                binding.tvMonthCount.text = "${displayCount}건"
            }
        }

        // 서버 평균 데시벨(average-db) 관찰
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.averageNoiseDb.collectLatest { avg ->
                val displayAvg = avg ?: 0.0
                binding.tvAvgDb.text = displayAvg.toInt().toString()
            }
        }

        // 월간 캘린더 데이터 관찰 (향후 커스텀 UI에 사용 가능)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.noiseLogDatesInMonth.collectLatest { datesWithNoise ->
                // TODO: CalendarView 커스텀 시, datesWithNoise 정보를 활용해 해당 날짜에 파란 네모 표시 등 적용
            }
        }

        // ViewModel에서 설정한 uiMessage를 Toast로 표시
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiMessage.collectLatest { message ->
                if (message != null) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    viewModel.consumeUiMessage()
                }
            }
        }
    }

    /**
     * 통계 정보 업데이트
     * 현재는 서버 값과의 일관성을 위해 로컬 계산은 최소화.
     * 필요시 추후 확장용 훅.
     */
    private fun updateStats(logs: List<com.kau.ttokttok.domain.model.NoiseLog>) {
        // 총 기록 / 이번 달 / 평균 dB는 모두 서버 값으로 표시하므로
        // 로컬 기반 통계 계산은 생략.
    }

    /**
     * 선택된 일기 개수 표시 업데이트
     * 체크박스 선택 시 호출됨
     */
    private fun updateSelectionCount() {
        val selectedCount = adapter.getSelectedLogs().size
        binding.tvListSummary.text = if (selectedCount > 0) {
            "${selectedCount}개 선택됨"
        } else {
            "${adapter.currentList.size}개 항목"
        }
    }

    override fun onResume() {
        super.onResume()
        // 다른 화면에서 돌아올 때 데이터 새로고침 (일기 추가/수정 후)
        viewModel.loadAllLogs()
        viewModel.fetchTotalNoiseRecordCount()
        viewModel.fetchMonthlyNoiseRecordCount()
        viewModel.fetchAverageNoiseDb()

        // 월간 캘린더는 ViewModel.refreshHeaderCounters()에서 이미 호출되므로 별도 호출 생략
        // val currentCal = Calendar.getInstance()
        // viewModel.fetchMonthlyCalendar(
        //     year = currentCal.get(Calendar.YEAR),
        //     month = currentCal.get(Calendar.MONTH) + 1
        // )

        viewModel.selectDate(viewModel.selectedDate.value)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 메모리 누수 방지: Handler 콜백 제거 및 binding 해제
        handler.removeCallbacks(flipAnimationRunnable)
        _binding = null
    }
}
