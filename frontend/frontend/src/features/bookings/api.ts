import { apiRequest } from '../../shared/api/http'
import type { PageResponse } from '../../shared/api/page'
import { serviceBases } from '../../shared/config/services'
import type { BookingDetail, BookingStatus, HoldSeatRequest, HoldSeatResponse } from './types'

const baseUrl = serviceBases.booking
const root = '/api/v1/bookings'

export const bookingApi = {
  holdSeat(request: HoldSeatRequest) {
    return apiRequest<HoldSeatResponse>(baseUrl, `${root}/hold`, {
      method: 'POST',
      body: request,
    })
  },
  getBooking(id: number) {
    return apiRequest<BookingDetail>(baseUrl, `${root}/${id}`)
  },
  getMyBookings(status?: BookingStatus | '') {
    return apiRequest<PageResponse<BookingDetail>>(baseUrl, `${root}/me`, {
      query: { status, page: 0, size: 20 },
    })
  },
  getMine(status?: BookingStatus | '') {
    return apiRequest<PageResponse<BookingDetail>>(baseUrl, `${root}/me`, {
      query: { status, page: 0, size: 20 },
    })
  },
  getAdminBookings(page = 0, size = 200) {
    return apiRequest<PageResponse<BookingDetail>>(baseUrl, `${root}/admin`, {
      query: { page, size },
    })
  },
  cancelBooking(id: number) {
    return apiRequest<BookingDetail>(baseUrl, `${root}/${id}`, { method: 'DELETE' })
  },
}
