import { apiRequest } from '../../shared/api/http'
import { serviceBases } from '../../shared/config/services'
import type { ProfileUpdateRequest, UserView } from './types'

const baseUrl = serviceBases.user
const root = '/api/v1/users'

export const userApi = {
  getProfile(userId: number) {
    return apiRequest<UserView>(baseUrl, `${root}/${userId}/profile`)
  },
  updateProfile(userId: number, request: ProfileUpdateRequest) {
    return apiRequest<UserView>(baseUrl, `${root}/${userId}/profile`, {
      method: 'PUT',
      body: request,
    })
  },
}
