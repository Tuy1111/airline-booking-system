export interface UserView {
  id: number
  email: string
  status: string
  roles: string[]
  fullName: string
  phone: string | null
  dateOfBirth: string | null
  gender: string | null
  nationality: string | null
  passportNumber: string | null
  passportCountry: string | null
  passportExpiry: string | null
  kycStatus: string
  loyaltyTier: string
  milesBalance: number
  lifetimeMiles: number
  createdAt: string
  lastLoginAt: string | null
}

export interface ProfileUpdateRequest {
  fullName: string
  phone: string
  dateOfBirth?: string
  gender?: 'MALE' | 'FEMALE' | 'OTHER' | ''
  nationality?: string
}
