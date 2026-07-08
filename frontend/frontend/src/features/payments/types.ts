export type PaymentMethod = 'CARD' | 'WALLET' | 'BANK_TRANSFER'
export type PaymentStatus = 'PENDING' | 'SUCCESS' | 'FAILED'

export interface CreatePaymentRequest {
  bookingId: number
  userId: number
  amount: number
  idempotencyKey: string
  method?: PaymentMethod
}

export interface PaymentResponse {
  id: number
  paymentCode: string
  bookingId: number
  amount: number
  currency: string
  status: PaymentStatus
  gateway: string | null
  transferCode: string | null
  qrUrl: string | null
  expiresAt: string | null
}
