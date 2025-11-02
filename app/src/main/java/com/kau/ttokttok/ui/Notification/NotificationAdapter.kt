package com.kau.ttokttok.ui.Notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kau.ttokttok.R
import com.kau.ttokttok.databinding.ItemNotificationBinding
import com.kau.ttokttok.domain.model.NotificationItem

// RecyclerView 어댑터
// onItemClick: 알림 클릭 시 실행될 콜백 (읽음 처리용)

class NotificationAdapter(
    private val onItemClick: (NotificationItem) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    private var notifications = listOf<NotificationItem>()  // 표시할 알림 목록

    fun updateNotifications(newList: List<NotificationItem>) {  // 목록 업데이트
        notifications = newList
        notifyDataSetChanged()
    }

    // ViewHolder 생성 (item_notification.xml inflate)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // ViewHolder에 데이터 바인딩
    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(notifications[position])

    override fun getItemCount(): Int = notifications.size  // 전체 아이템 개수

    // 개별 알림 아이템 ViewHolder
    inner class ViewHolder(private val b: ItemNotificationBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(item: NotificationItem) {
            b.tvNotificationTitle.text = item.title       // 제목
            b.tvNotificationMessage.text = item.message   // 내용
            b.tvTimestamp.text = item.timeAgo             // 시간

            // 알림 타입별 아이콘 설정 (변경된 카테고리에 맞게 업데이트)
            val iconRes = when (item.iconType) {
                "noise_status" -> R.drawable.ic_volume_up      // 소음 현황판
                "prior_consent" -> R.drawable.ic_favorite      // 사전 양해
                "announcement" -> R.drawable.ic_building       // 공지사항
                "monthly_report" -> R.drawable.ic_bar_chart    // 월간 리포트
                else -> R.drawable.ic_bell                     // 기본
            }
            b.ivNotificationIcon.setImageResource(iconRes)

            // NEW 배지 표시 (새 알림일 때만)
            b.newBadgeContainer.visibility = if (item.isNew) View.VISIBLE else View.GONE

            // 읽지 않음 인디케이터 (안 읽은 알림일 때만)
            b.unreadIndicatorContainer.visibility = if (!item.isRead) View.VISIBLE else View.GONE

            b.root.setOnClickListener { onItemClick(item) }  // 클릭 시 읽음 처리
        }
    }
}
