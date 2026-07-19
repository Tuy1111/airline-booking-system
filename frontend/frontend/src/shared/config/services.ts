const globalApiBase = import.meta.env.VITE_API_BASE_URL ?? ''

export const serviceBases = {
  flight: globalApiBase,
  booking: globalApiBase,
  user: globalApiBase,
  payment: globalApiBase,
  notification: globalApiBase,
} as const

export const serviceCatalog = [
  {
    key: 'flight',
    name: 'Flight Search',
    scope: 'flights, routes, seats, inventory',
  },
  {
    key: 'booking',
    name: 'Booking',
    scope: 'seat hold, booking lifecycle, outbox events',
  },
  {
    key: 'user',
    name: 'User',
    scope: 'accounts, profiles, loyalty, KYC',
  },
  {
    key: 'payment',
    name: 'Payment',
    scope: 'SePay payment, QR, payment status',
  },
  {
    key: 'notification',
    name: 'Notification',
    scope: 'booking confirmation and reminder history',
  },
] as const
