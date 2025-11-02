package com.kau.ttokttok.ui.Notification

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kau.ttokttok.databinding.ActivityNotificationSettingsBinding

// 알림 설정 화면
class NotificationSettingsFragment : Fragment() {

    private var _binding: ActivityNotificationSettingsBinding? = null
    private val binding get() = _binding!!
    private val vm: NotificationViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityNotificationSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSettings()  // 설정값 불러오기
        setupButtons()  // 버튼 연결
    }

    // 저장된 설정을 화면에 표시
    private fun loadSettings() {
        // TODO: 백엔드 연동 시 서버에서 사용자별 설정 불러오기
        val s = vm.settings
        binding.apply {
            switchAllNotifications.isChecked = s.allNotifications  // 전체 알림
            switchNoiseStatus.isChecked = s.noiseStatus  // 소음 현황판
            switchPriorConsent.isChecked = s.priorConsent  // 사전 양해
            switchAnnouncements.isChecked = s.announcements  // 공지사항
            switchMonthlyReport.isChecked = s.monthlyReport  // 월간 리포트
            tvStartTime.text = s.doNotDisturbStart  // 방해금지 시작
            tvEndTime.text = s.doNotDisturbEnd  // 방해금지 종료
        }
    }

    // 버튼 클릭 이벤트 연결
    private fun setupButtons() {
        binding.apply {
            btnBack.setOnClickListener { goBack() }  // 뒤로가기
            btnSave.setOnClickListener { save() }  // 저장
            startTimeContainer.setOnClickListener { pickTime(true) }  // 시작 시간 선택
            endTimeContainer.setOnClickListener { pickTime(false) }  // 종료 시간 선택
        }
    }

    // 설정 저장
    private fun save() {
        binding.apply {
            val newSettings = vm.settings.copy(
                allNotifications = switchAllNotifications.isChecked,
                noiseStatus = switchNoiseStatus.isChecked,
                priorConsent = switchPriorConsent.isChecked,
                announcements = switchAnnouncements.isChecked,
                monthlyReport = switchMonthlyReport.isChecked,
                doNotDisturbStart = tvStartTime.text.toString(),
                doNotDisturbEnd = tvEndTime.text.toString()
            )
            // TODO: 백엔드 연동 시 서버에 설정 저장 요청
            // TODO: 서버 응답 성공/실패에 따라 Toast 메시지 변경 필요
            vm.updateSettings(newSettings)  // ViewModel에 저장
        }
        Toast.makeText(requireContext(), "저장 완료", Toast.LENGTH_SHORT).show()
        goBack()
    }

    // 시간 선택 다이얼로그
    private fun pickTime(isStart: Boolean) {
        val currentTime = if (isStart) binding.tvStartTime.text.toString() else binding.tvEndTime.text.toString()
        val (hour, minute) = currentTime.split(":").map { it.toInt() }

        TimePickerDialog(requireContext(), { _, h, m ->
            val time = "%02d:%02d".format(h, m)
            if (isStart) binding.tvStartTime.text = time else binding.tvEndTime.text = time
        }, hour, minute, true).show()
    }

    private fun goBack() = requireActivity().onBackPressedDispatcher.onBackPressed()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
