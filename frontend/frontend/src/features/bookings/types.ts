export type BookingStatus = 'HELD' | 'CONFIRMED' | 'CANCELLED' | 'EXPIRED' | 'REFUNDED'

export interface HoldSeatRequest {
  flightId: number
  seatNo: string
  passengerName: string
  passengerPassport?: string
  extraBaggageKg?: number
}

export interface HoldSeatResponse {
  bookingId: number
  bookingCode: string
  flightId: number
  seatNo: string
  price: number
  baggageWeightKg?: number
  baggageFee?: number
  currency: string
  holdExpiresAt: string
  message: string
}

export interface BookingItem {
  id: number
  seatNo: string
  passengerName: string
  passengerPassport: string | null
  price: number
}

export interface BookingDetail {
  id: number
  bookingCode: string
  userId: number
  flightId: number
  status: BookingStatus
  totalAmount: number
  baggageWeightKg?: number
  baggageFee?: number
  currency: string
  heldAt: string | null
  expiresAt: string | null
  confirmedAt: string | null
  cancelledAt: string | null
  paymentId: string | null
  createdAt: string
  items: BookingItem[]
}

