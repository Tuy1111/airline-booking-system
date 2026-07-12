import { apiRequest, tryFallback } from '../../shared/api/http'
import type { PageResponse } from '../../shared/api/page'
import { serviceBases } from '../../shared/config/services'
import type { BookingDetail, BookingStatus, HoldSeatRequest, HoldSeatResponse } from './types'

const baseUrl = serviceBases.booking
const versionedRoot = '/api/v1/bookings'
const legacyRoot = '/bookings'

function userHeader(userId: number) {
  return { 'X-User-Id': String(userId) }
}

function withBookingFallback<T>(
  path: string,
  options: Parameters<typeof apiRequest<T>>[2],
) {
  return tryFallback(
    () => apiRequest<T>(baseUrl, `${versionedRoot}${path}`, options),
    () => apiRequest<T>(baseUrl, `${legacyRoot}${path}`, options),
  )
}

export const bookingApi = {
  holdSeat(userId: number, request: HoldSeatRequest) {
    return withBookingFallback<HoldSeatResponse>('/hold', {
      method: 'POST',
      headers: userHeader(userId),
      body: request,
    })
  },
  getBooking(id: number) {
    return withBookingFallback<BookingDetail>(`/${id}`, {})
  },
  getMyBookings(userId: number, status?: BookingStatus | '') {
    return withBookingFallback<PageResponse<BookingDetail>>('/me', {
      headers: userHeader(userId),
      query: { status, page: 0, size: 20 },
    })
  },
  cancelBooking(id: number, userId: number) {
    return withBookingFallback<BookingDetail>(`/${id}`, {
      method: 'DELETE',
      headers: userHeader(userId),
    })
  },
}
