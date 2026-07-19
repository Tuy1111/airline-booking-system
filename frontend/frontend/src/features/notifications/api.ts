import { apiRequest } from '../../shared/api/http'
import type { PageResponse } from '../../shared/api/page'
import { serviceBases } from '../../shared/config/services'
import type { NotificationItem } from './types'

const baseUrl = serviceBases.notification
const root = '/api/v1/notifications'

export const notificationApi = {
  mine() {
    return apiRequest<PageResponse<NotificationItem>>(baseUrl, `${root}/me`, {
      query: { page: 0, size: 10 },
    })
  },
  list() {
    return apiRequest<PageResponse<NotificationItem>>(baseUrl, root, {
      query: { page: 0, size: 10 },
    })
  },
}
