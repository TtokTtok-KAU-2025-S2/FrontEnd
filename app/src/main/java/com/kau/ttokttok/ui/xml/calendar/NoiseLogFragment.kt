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
import com.kau.ttokttok.R
import com.kau.ttokttok.databinding.FragmentMyProfileBinding
import com.kau.ttokttok.ui.navigation.Destination
import com.kau.ttokttok.ui.navigation.navigateTo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

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
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
        viewModel.selectDate(today)

        // TODO: 실제 userId는 로그인한 사용자의 ID로 교체해야 함
        val userId = 3L // 테스트용 userId (API 명세서 예시)

        // 캘린더 API 호출 - 현재 월의 데이터 로드
        viewModel.loadCurrentMonthCalendar(userId)

        // 총 소음 기록 수 API 호출
        viewModel.loadTotalCount(userId)

        // 이번 달 소음 기록 수 API 호출
        viewModel.loadMonthlyCount(userId)

        // 전체 평균 dB API 호출
        viewModel.loadAverageDb(userId)

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
            // yyyy-MM-dd 형식으로 날짜 문자열 생성
            val dateStr = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)

            // 새로운 API 호출: 날짜별 소음 기록 상세 조회
            viewModel.loadNoiseRecordsByDate(dateStr)

            // 선택된 날짜도 업데이트 (기존 로직 유지)
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

    /**
     * 리포트 생성 버튼 설정
     * 선택된 일기들을 PDF 리포트로 생성
     */
    private fun setupReportButton() {
        binding.btnCreateReport.setOnClickListener {
            val selectedLogs = adapter.getSelectedLogs()

            if (selectedLogs.isEmpty()) {
                Toast.makeText(requireContext(), "리포트로 만들 일기를 선택해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 리포트 생성 API 미구현 - 임시로 로컬 상태만 변경
            selectedLogs.forEach { log ->
                viewModel.toggleReportStatus(log)
            }

            adapter.clearSelection()
            Toast.makeText(requireContext(), "${selectedLogs.size}개의 리포트가 생성되었습니다", Toast.LENGTH_SHORT).show()
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

        // 에러 상태 관찰
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.errorState.collect { error ->
                error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    viewModel.clearError()
                }
            }
        }

        // 로딩 상태 관찰
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                // TODO: 프로그레스바 표시/숨김 처리
                // binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        // 캘린더 데이터 관찰 - hasNoiseLog가 true인 날짜를 하이라이트
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.calendarData.collect { calendarMap ->
                // TODO: CalendarView에 데이터 반영
                // calendarMap의 각 날짜에 대해 hasNoiseLog가 true이면 파란색 표시
                // 예: calendarView.markDates(calendarMap.filter { it.value }.keys)

                // 디버그용 로그 (실제로는 CalendarView 커스터마이징 필요)
                val datesWithLog = calendarMap.filter { it.value }.keys
                if (datesWithLog.isNotEmpty()) {
                    // 소음 일기가 있는 날짜 개수 표시
                    android.util.Log.d("NoiseLogFragment", "소음 일기가 있는 날짜: ${datesWithLog.size}개")
                }
            }
        }

        // 총 소음 기록 수 관찰 (API)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.totalCount.collect { count ->
                binding.tvTotalCount.text = "${count}건"
                android.util.Log.d("NoiseLogFragment", "총 소음 기록 수: ${count}건")
            }
        }

        // 이번 달 소음 기록 수 관찰 (API)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.monthlyCount.collect { count ->
                binding.tvMonthCount.text = "${count}건"
                android.util.Log.d("NoiseLogFragment", "이번 달 소음 기록 수: ${count}건")
            }
        }

        // 평균 dB 관찰 (API)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.averageDb.collect { avgDb ->
                binding.tvAvgDb.text = String.format("%.1f", avgDb)
                android.util.Log.d("NoiseLogFragment", "평균 소음 레벨: ${String.format("%.2f", avgDb)} dB")
            }
        }
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
        viewModel.selectDate(viewModel.selectedDate.value)

        // API 통계 데이터도 새로고침
        val userId = 3L // TODO: 실제 userId로 교체
        viewModel.loadTotalCount(userId)
        viewModel.loadMonthlyCount(userId)
        viewModel.loadAverageDb(userId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 메모리 누수 방지: Handler 콜백 제거 및 binding 해제
        handler.removeCallbacks(flipAnimationRunnable)
        _binding = null
    }
}
