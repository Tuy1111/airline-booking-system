import { apiRequest } from '../../shared/api/http'
import { serviceBases } from '../../shared/config/services'
import type { CreatePaymentRequest, PaymentResponse } from './types'

const baseUrl = serviceBases.payment
const root = '/api/v1/payments'

export const paymentApi = {
  createPayment(request: CreatePaymentRequest) {
    return apiRequest<PaymentResponse>(baseUrl, root, {
      method: 'POST',
      body: request,
    })
  },
  getPayment(id: number) {
    return apiRequest<PaymentResponse>(baseUrl, `${root}/${id}`)
  },
  getByCode(code: string) {
    return apiRequest<PaymentResponse>(baseUrl, `${root}/by-code/${encodeURIComponent(code)}`)
  },
  getByBooking(bookingId: number) {
    return apiRequest<PaymentResponse[]>(baseUrl, `${root}/by-booking/${bookingId}`)
  },
}
