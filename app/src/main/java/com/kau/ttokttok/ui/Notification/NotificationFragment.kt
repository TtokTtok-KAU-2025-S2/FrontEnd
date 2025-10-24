package com.kau.ttokttok.ui.Notification

import com.kau.ttokttok.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kau.ttokttok.databinding.ActivityNotificationBinding
import com.kau.ttokttok.ui.MainActivity

// 알림 화면 - 앱 시작 시 보이는 메인 화면
// 기능: 알림 목록 표시, 탭 전환(전체/읽지않음), 읽음/삭제 처리, 설정

class NotificationFragment : Fragment() {

    private var _binding: ActivityNotificationBinding? = null  // ViewBinding
    private val binding get() = _binding!!  // null 체크 없이 안전하게 접근

    private val vm: NotificationViewModel by viewModels()  // ViewModel
    private lateinit var adapter: NotificationAdapter  // RecyclerView 어댑터

    // Fragment 뷰 생성 (레이아웃 inflate)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    // 뷰 생성 완료 후 초기화 작업
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()  // RecyclerView 설정
        setupListeners()     // 버튼 이벤트 설정
        refreshUI()          // 초기 데이터 표시
    }

    // RecyclerView 초기화 및 설정
    private fun setupRecyclerView() {
        adapter = NotificationAdapter { notif ->
            // TODO: 백엔드 연동 시 알림 클릭 시 상세 화면으로 이동하거나 관련 화면으로 라우팅
            vm.markAsRead(notif.id)  // 클릭 시 읽음 처리
            refreshUI()
            showToast("읽음 처리: ${notif.title}")
        }
        binding.recyclerViewNotifications.apply {
            layoutManager = LinearLayoutManager(requireContext())  // 세로 리스트
            adapter = this@NotificationFragment.adapter
        }
    }

    // 모든 버튼 클릭 이벤트 설정
    private fun setupListeners() {
        binding.btnBack.setOnClickListener {  // 뒤로가기
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSettings.setOnClickListener {  // 설정 화면으로 전환
            (requireActivity() as? MainActivity)?.show(NotificationSettingsFragment())
        }

        binding.btnTabAll.setOnClickListener {  // "전체" 탭
            vm.selectTab(true)
            refreshUI()
        }

        binding.btnTabUnread.setOnClickListener {  // "읽지 않음" 탭
            vm.selectTab(false)
            refreshUI()
        }

        binding.btnMarkAllRead.setOnClickListener {  // 모두 읽음
            // TODO: 백엔드 연동 시 확인 다이얼로그 추가 후 서버에 요청
            vm.markAllAsRead()
            refreshUI()
            showToast("모두 읽음 처리됨")
        }

        binding.btnDeleteAll.setOnClickListener {  // 모두 삭제
            // TODO: 백엔드 연동 시 확인 다이얼로그 추가 후 서버에 요청
            vm.deleteAllNotifications()
            refreshUI()
            showToast("모두 삭제됨")
        }
    }

    // UI 업데이트 (목록, 빈 상태, 탭 배경색 및 텍스트 색상)
    private fun refreshUI() {
        // TODO: 백엔드 연동 시 서버에서 실시간으로 알림 목록 가져오기 (LiveData/Flow 사용)
        val list = vm.getCurrentNotifications()  // 현재 탭의 알림 목록
        adapter.updateNotifications(list)

        // 알림 없으면 빈 상태 표시
        binding.emptyStateLayout.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE

        // 선택된 탭 색상 설정
        val colorSelected = requireContext().getColor(R.color.tab_selected_bg)  // 보라색
        val colorTransparent = requireContext().getColor(android.R.color.transparent)  // 투명
        val textColorSelected = requireContext().getColor(R.color.white)  // 선택: 흰색
        val textColorUnselected = requireContext().getColor(android.R.color.darker_gray)  // 비선택: 회색

        if (vm.isAllTabSelected) {  // "전체" 탭 선택됨
            binding.btnTabAll.setCardBackgroundColor(colorSelected)
            binding.btnTabUnread.setCardBackgroundColor(colorTransparent)
            binding.tvTabAll.setTextColor(textColorSelected)  // 전체 텍스트: 흰색
            binding.tvTabUnread.setTextColor(textColorUnselected)  // 읽지않음 텍스트: 회색
        } else {  // "읽지 않음" 탭 선택됨
            binding.btnTabAll.setCardBackgroundColor(colorTransparent)
            binding.btnTabUnread.setCardBackgroundColor(colorSelected)
            binding.tvTabAll.setTextColor(textColorUnselected)  // 전체 텍스트: 회색
            binding.tvTabUnread.setTextColor(textColorSelected)  // 읽지않음 텍스트: 흰색
        }
    }

    private fun showToast(msg: String) {  // 짧은 메시지 표시
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {  // 뷰 파괴 시 binding 해제 (메모리 누수 방지)
        super.onDestroyView()
        _binding = null
    }
}
