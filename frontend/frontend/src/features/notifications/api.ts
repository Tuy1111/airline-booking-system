import { apiRequest } from '../../shared/api/http'
import type { PageResponse } from '../../shared/api/page'
import { serviceBases } from '../../shared/config/services'
import type { NotificationItem, UnreadCountResponse } from './types'

const baseUrl = serviceBases.notification
const root = '/api/v1/notifications'

export const notificationApi = {
  mine() {
    return apiRequest<PageResponse<NotificationItem>>(baseUrl, `${root}/me`, {
      query: { page: 0, size: 20 },
    })
  },
  unreadCount() {
    return apiRequest<UnreadCountResponse>(baseUrl, `${root}/me/unread-count`)
  },
  markRead(id: number) {
    return apiRequest<NotificationItem>(baseUrl, `${root}/me/${id}/read`, {
      method: 'PATCH',
    })
  },
  markAllRead() {
    return apiRequest<void>(baseUrl, `${root}/me/read-all`, {
      method: 'PATCH',
    })
  },
  list() {
    return apiRequest<PageResponse<NotificationItem>>(baseUrl, root, {
      query: { page: 0, size: 10 },
    })
  },
}
