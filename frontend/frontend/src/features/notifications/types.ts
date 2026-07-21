export interface NotificationItem {
  id: number
  templateCode: string
  userId: number
  channel: 'EMAIL' | 'SMS' | 'PUSH'
  recipient: string
  status: 'PENDING' | 'SENT' | 'FAILED'
  retryCount: number
  errorMessage: string | null
  title: string
  content: string
  createdAt: string
  sentAt: string | null
  readAt: string | null
}

export interface UnreadCountResponse {
  unreadCount: number
}
