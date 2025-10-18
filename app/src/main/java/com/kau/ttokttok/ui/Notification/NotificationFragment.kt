package com.kau.ttokttok.ui.Notification

import com.kau.ttokttok.R
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kau.ttokttok.databinding.ActivityNotificationBinding

// 알림 화면 - 앱 시작 시 보이는 메인 화면
// 기능: 알림 목록 표시, 탭 전환(전체/읽지않음), 읽음/삭제 처리

class NotificationFragment : Fragment() {

    private var _binding: ActivityNotificationBinding? = null  // ViewBinding (메모리 누수 방지용)
    private val binding get() = _binding!!  // null 체크 없이 안전하게 접근

    private val vm: NotificationViewModel by viewModels()  // ViewModel (화면 회전 시에도 유지)
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

        binding.btnSettings.setOnClickListener {  // 설정 화면 이동 (TODO: 네비게이션 연결)
            showToast("설정 화면으로 이동")
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
            vm.markAllAsRead()
            refreshUI()
            showToast("모두 읽음 처리됨")
        }

        binding.btnDeleteAll.setOnClickListener {  // 모두 삭제
            vm.deleteAllNotifications()
            refreshUI()
            showToast("모두 삭제됨")
        }
    }

    // UI 업데이트 (목록, 빈 상태, 탭 배경색)
    private fun refreshUI() {
        val list = vm.getCurrentNotifications()  // 현재 탭의 알림 목록
        adapter.updateNotifications(list)

        // 알림 없으면 빈 상태 표시
        binding.emptyStateLayout.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE

        // 탭 배경색 (선택: 보라색, 비선택: 투명)
        val colorSelected = requireContext().getColor(R.color.tab_selected_bg)
        val colorTransparent = requireContext().getColor(android.R.color.transparent)

        if (vm.isAllTabSelected) {
            binding.btnTabAll.setCardBackgroundColor(colorSelected)
            binding.btnTabUnread.setCardBackgroundColor(colorTransparent)
        } else {
            binding.btnTabAll.setCardBackgroundColor(colorTransparent)
            binding.btnTabUnread.setCardBackgroundColor(colorSelected)
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
