import { apiRequest } from '../../shared/api/http'
import { serviceBases } from '../../shared/config/services'
import type { MilesRequest, PassportSubmitRequest, ProfileUpdateRequest, UserView } from './types'

const baseUrl = serviceBases.user
const root = '/api/v1/users'

export const userApi = {
  getProfile() {
    return apiRequest<UserView>(baseUrl, `${root}/me`)
  },
  updateProfile(request: ProfileUpdateRequest) {
    return apiRequest<UserView>(baseUrl, `${root}/me/profile`, {
      method: 'PUT',
      body: request,
    })
  },
  earnMiles(request: MilesRequest) {
    return apiRequest<UserView>(baseUrl, `${root}/me/miles/earn`, {
      method: 'POST',
      body: request,
    })
  },
  redeemMiles(request: MilesRequest) {
    return apiRequest<UserView>(baseUrl, `${root}/me/miles/redeem`, {
      method: 'POST',
      body: request,
    })
  },
  submitPassport(request: PassportSubmitRequest) {
    return apiRequest<UserView>(baseUrl, `${root}/me/passport`, {
      method: 'POST',
      body: request,
    })
  },
}
