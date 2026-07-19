import { apiRequest } from '../../shared/api/http'
import { serviceBases } from '../../shared/config/services'
import type { MilesRequest, PassportSubmitRequest, ProfileUpdateRequest, UserView } from './types'

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
  earnMiles(userId: number, request: MilesRequest) {
    return apiRequest<UserView>(baseUrl, `${root}/${userId}/miles/earn`, {
      method: 'POST',
      body: request,
    })
  },
  redeemMiles(userId: number, request: MilesRequest) {
    return apiRequest<UserView>(baseUrl, `${root}/${userId}/miles/redeem`, {
      method: 'POST',
      body: request,
    })
  },
  submitPassport(userId: number, request: PassportSubmitRequest) {
    return apiRequest<UserView>(baseUrl, `${root}/${userId}/passport`, {
      method: 'POST',
      body: request,
    })
  },
}
