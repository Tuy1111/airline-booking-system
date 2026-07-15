import { apiRequest } from '../../shared/api/http'
import { serviceBases } from '../../shared/config/services'
import type { AuthResult, LoginRequest, ProfileUpdateRequest, RegisterRequest, UserView } from './types'

const baseUrl = serviceBases.user
const root = '/api/v1/users'

export const userApi = {
  login(request: LoginRequest) {
    return apiRequest<AuthResult>(baseUrl, `${root}/login`, {
      method: 'POST',
      body: request,
    })
  },
  register(request: RegisterRequest) {
    return apiRequest<AuthResult>(baseUrl, `${root}/register`, {
      method: 'POST',
      body: request,
    })
  },
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
