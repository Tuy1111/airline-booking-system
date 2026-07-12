export interface NotificationItem {
  id: number
  templateCode: string
  userId: number
  channel: 'EMAIL' | 'SMS'
  recipient: string
  status: 'PENDING' | 'SENT' | 'FAILED'
  retryCount: number
  errorMessage: string | null
  createdAt: string
  sentAt: string | null
}
