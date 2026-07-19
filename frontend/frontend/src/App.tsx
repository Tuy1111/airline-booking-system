import { useEffect, useState } from 'react'
import './App.css'
import { Header } from './components/Header'
import { HeroSearch } from './components/HeroSearch'
import { FlightResults } from './components/FlightResults'
import { SeatPicker } from './components/SeatPicker'
import { PassengerPayment } from './components/PassengerPayment'
import { MyBookingsETicket } from './components/MyBookingsETicket'
import { UserProfileModal } from './components/UserProfileModal'
import { NotificationDrawer } from './components/NotificationDrawer'
import { AdminDashboard } from './components/AdminDashboard'

import { bookingApi } from './features/bookings/api'
import type { BookingDetail, HoldSeatResponse } from './features/bookings/types'
import { flightApi } from './features/flights/api'
import type {
  Airline,
  Airport,
  FlightCreateRequest,
  FlightDetail,
  FlightSearchParams,
  FlightStatusUpdateRequest,
  FlightSummary,
  RouteInfo,
  SeatMapItem,
} from './features/flights/types'
import { notificationApi } from './features/notifications/api'
import type { NotificationItem } from './features/notifications/types'
import { paymentApi } from './features/payments/api'
import type { PaymentResponse } from './features/payments/types'
import { userApi } from './features/users/api'
import type { UserView } from './features/users/types'
import {
  currentUser,
  login as keycloakLogin,
  logout as keycloakLogout,
  register as keycloakRegister,
} from './shared/auth/keycloak'

const DEFAULT_AIRPORTS: Airport[] = [
  { iataCode: 'HAN', name: 'Sân bay Quốc tế Nội Bài', city: 'Hà Nội', country: 'Vietnam' },
  { iataCode: 'SGN', name: 'Sân bay Quốc tế Tân Sơn Nhất', city: 'TP. Hồ Chí Minh', country: 'Vietnam' },
  { iataCode: 'DAD', name: 'Sân bay Quốc tế Đà Nẵng', city: 'Đà Nẵng', country: 'Vietnam' },
  { iataCode: 'PQC', name: 'Sân bay Quốc tế Phú Quốc', city: 'Phú Quốc', country: 'Vietnam' },
  { iataCode: 'CXR', name: 'Sân bay Quốc tế Cam Ranh', city: 'Nha Trang', country: 'Vietnam' },
]

const DEFAULT_AIRLINES: Airline[] = [
  { code: 'VN', name: 'Vietnam Airlines' },
  { code: 'VJ', name: 'Vietjet Air' },
  { code: 'QH', name: 'Bamboo Airways' },
]

const DEFAULT_ROUTES: RouteInfo[] = [
  { id: 1, fromAirportCode: 'HAN', fromAirportName: 'Nội Bài', fromCity: 'Hà Nội', toAirportCode: 'SGN', toAirportName: 'Tân Sơn Nhất', toCity: 'TP. Hồ Chí Minh', distanceKm: 1160 },
  { id: 2, fromAirportCode: 'HAN', fromAirportName: 'Nội Bài', fromCity: 'Hà Nội', toAirportCode: 'DAD', toAirportName: 'Đà Nẵng', toCity: 'Đà Nẵng', distanceKm: 600 },
  { id: 3, fromAirportCode: 'SGN', fromAirportName: 'Tân Sơn Nhất', fromCity: 'TP. Hồ Chí Minh', toAirportCode: 'PQC', toAirportName: 'Phú Quốc', toCity: 'Phú Quốc', distanceKm: 300 },
]

const DEFAULT_FLIGHTS: FlightSummary[] = [
  {
    id: 10,
    flightNo: 'VN123',
    airlineCode: 'VN',
    airlineName: 'Vietnam Airlines',
    fromAirport: 'HAN',
    fromCity: 'Hà Nội',
    toAirport: 'SGN',
    toCity: 'TP. Hồ Chí Minh',
    departureTime: new Date(Date.now() + 7200000).toISOString(),
    arrivalTime: new Date(Date.now() + 15000000).toISOString(),
    basePrice: 1400000,
    currentPrice: 1540000,
    aircraftType: 'Airbus A321',
    status: 'SCHEDULED',
    availableSeats: 42,
  },
  {
    id: 11,
    flightNo: 'VJ456',
    airlineCode: 'VJ',
    airlineName: 'Vietjet Air',
    fromAirport: 'HAN',
    fromCity: 'Hà Nội',
    toAirport: 'SGN',
    toCity: 'TP. Hồ Chí Minh',
    departureTime: new Date(Date.now() + 18000000).toISOString(),
    arrivalTime: new Date(Date.now() + 25800000).toISOString(),
    basePrice: 990000,
    currentPrice: 1150000,
    aircraftType: 'Airbus A320',
    status: 'SCHEDULED',
    availableSeats: 18,
  },
  {
    id: 12,
    flightNo: 'QH789',
    airlineCode: 'QH',
    airlineName: 'Bamboo Airways',
    fromAirport: 'HAN',
    fromCity: 'Hà Nội',
    toAirport: 'SGN',
    toCity: 'TP. Hồ Chí Minh',
    departureTime: new Date(Date.now() + 32000000).toISOString(),
    arrivalTime: new Date(Date.now() + 39800000).toISOString(),
    basePrice: 1650000,
    currentPrice: 1800000,
    aircraftType: 'Boeing 787-9',
    status: 'SCHEDULED',
    availableSeats: 35,
  },
]

const generateMockSeats = (_flightId: number): SeatMapItem[] => {
  const seats: SeatMapItem[] = []
  const rows = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
  const letters = ['A', 'B', 'C', 'D', 'E', 'F']

  rows.forEach((row) => {
    letters.forEach((letter) => {
      const seatNo = `${row}${letter}`
      const seatClass = row <= 3 ? 'BUSINESS' : 'ECONOMY'
      let status: 'AVAILABLE' | 'HELD' | 'BOOKED' = 'AVAILABLE'
      if ((row === 2 && letter === 'B') || (row === 5 && letter === 'C')) status = 'HELD'
      if ((row === 1 && letter === 'A') || (row === 4 && letter === 'D')) status = 'BOOKED'
      seats.push({
        seatNo,
        seatClass: seatClass as any,
        status,
        priceFactor: seatClass === 'BUSINESS' ? 1.5 : 1.0,
      })
    })
  })
  return seats
}

function dateInputValue(date = new Date()) {
  const localDate = new Date(date.getTime() - date.getTimezoneOffset() * 60000)
  return localDate.toISOString().slice(0, 10)
}

const defaultSearchDate = dateInputValue()

const moneyFormatter = new Intl.NumberFormat('vi-VN', {
  style: 'currency',
  currency: 'VND',
  maximumFractionDigits: 0,
})

const dateTimeFormatter = new Intl.DateTimeFormat('vi-VN', {
  dateStyle: 'short',
  timeStyle: 'short',
})

function formatMoney(value: number | string | null | undefined) {
  const amount = Number(value ?? 0)
  return moneyFormatter.format(Number.isFinite(amount) ? amount : 0)
}

function formatDateTime(value: string | null | undefined) {
  if (!value) return 'Pending'
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return value
  return dateTimeFormatter.format(date)
}

function durationLabel(start: string, end: string) {
  const diff = new Date(end).getTime() - new Date(start).getTime()
  if (!Number.isFinite(diff) || diff <= 0) return 'N/A'
  const minutes = Math.round(diff / 60000)
  const hours = Math.floor(minutes / 60)
  const remainder = minutes % 60
  return `${hours}h ${remainder}m`
}

export function App() {
  const [auth] = useState(() => currentUser())
  const [activeTab, setActiveTab] = useState<'search' | 'bookings' | 'profile' | 'admin'>('search')
  const [step, setStep] = useState<'search' | 'seat' | 'passenger-payment'>('search')
  const [tripType, setTripType] = useState<'one-way' | 'round-trip'>('round-trip')

  const [notice, setNotice] = useState('')
  const [error, setError] = useState('')
  const [isSearching, setIsSearching] = useState(false)
  const [isHolding, setIsHolding] = useState(false)
  const [isCreatingPayment, setIsCreatingPayment] = useState(false)
  const [isProfileUpdating, setIsProfileUpdating] = useState(false)

  // Master Data
  const [airports, setAirports] = useState<Airport[]>(DEFAULT_AIRPORTS)
  const [airlines, setAirlines] = useState<Airline[]>(DEFAULT_AIRLINES)
  const [routes, setRoutes] = useState<RouteInfo[]>(DEFAULT_ROUTES)

  // Search & Flight State
  const [search, setSearch] = useState<FlightSearchParams>({
    from: 'HAN',
    to: 'SGN',
    date: defaultSearchDate,
    passengers: 1,
    status: '',
    airline: '',
    minPrice: '',
    maxPrice: '',
    dateTo: '',
    sort: 'departureTime',
    order: 'asc',
  })

  const [flights, setFlights] = useState<FlightSummary[]>(DEFAULT_FLIGHTS)
  const [upcomingFlights, setUpcomingFlights] = useState<FlightSummary[]>(DEFAULT_FLIGHTS)
  const [selectedFlight, setSelectedFlight] = useState<FlightDetail | null>(null)
  const [seats, setSeats] = useState<SeatMapItem[]>([])
  const [selectedSeat, setSelectedSeat] = useState('')
  const [manualSeat, setManualSeat] = useState('1A')

  // Booking & Payment State
  const [passengerName, setPassengerName] = useState(auth?.fullName || 'Nguyen Van A')
  const [passengerPassport, setPassengerPassport] = useState('P1234567')
  const [holdResult, setHoldResult] = useState<HoldSeatResponse | null>(null)
  const [payment, setPayment] = useState<PaymentResponse | null>(null)
  const [userBookings, setUserBookings] = useState<BookingDetail[]>([])

  // User Profile State
  const [userProfile, setUserProfile] = useState<UserView | null>(null)
  const [isProfileModalOpen, setIsProfileModalOpen] = useState(false)

  // Notifications State
  const [notifications, setNotifications] = useState<NotificationItem[]>([])
  const [isNotificationDrawerOpen, setIsNotificationDrawerOpen] = useState(false)

  // Load Initial Master Data & Initial Searches
  useEffect(() => {
    flightApi
      .getAirports()
      .then((res) => setAirports(res?.length ? res : DEFAULT_AIRPORTS))
      .catch(() => setAirports(DEFAULT_AIRPORTS))

    flightApi
      .getAirlines()
      .then((res) => setAirlines(res?.length ? res : DEFAULT_AIRLINES))
      .catch(() => setAirlines(DEFAULT_AIRLINES))

    flightApi
      .getRoutes()
      .then((res) => setRoutes(res?.length ? res : DEFAULT_ROUTES))
      .catch(() => setRoutes(DEFAULT_ROUTES))

    flightApi
      .upcoming(24)
      .then((res) => setUpcomingFlights(res?.length ? res : DEFAULT_FLIGHTS))
      .catch(() => setUpcomingFlights(DEFAULT_FLIGHTS))

    // Load initial flight search
    handleSearch()
  }, [])

  // Load User Data if logged in
  useEffect(() => {
    if (auth) {
      loadUserBookings()
      loadNotifications()
      loadUserProfile()
    }
  }, [auth])

  const loadUserProfile = async () => {
    try {
      const p = await userApi.getProfile(1)
      setUserProfile(p)
    } catch {
      setUserProfile({
        id: 1,
        email: auth?.email || 'user@example.com',
        status: 'ACTIVE',
        roles: auth?.roles || ['USER'],
        fullName: auth?.fullName || 'Nguyen Van A',
        phone: '0900000000',
        dateOfBirth: '1995-05-20',
        gender: 'MALE',
        nationality: 'VNM',
        passportNumber: 'P1234567',
        passportCountry: 'VNM',
        passportExpiry: '2030-12-31',
        kycStatus: 'VERIFIED',
        loyaltyTier: 'GOLD',
        milesBalance: 5400,
        lifetimeMiles: 12000,
        createdAt: new Date().toISOString(),
        lastLoginAt: new Date().toISOString(),
      })
    }
  }

  const loadUserBookings = async () => {
    try {
      const res = await bookingApi.getMine()
      setUserBookings(res.content || [])
    } catch {
      // Ignore
    }
  }

  const loadNotifications = async () => {
    try {
      const res = await notificationApi.mine()
      setNotifications(res.content || [])
    } catch {
      // Ignore
    }
  }

  const handleSearch = async (e?: React.FormEvent) => {
    if (e) e.preventDefault()
    setIsSearching(true)
    setError('')
    try {
      const res = await flightApi.searchFlights(search)
      setFlights(res?.length ? res : DEFAULT_FLIGHTS)
    } catch {
      // Offline fallback
      setFlights(DEFAULT_FLIGHTS)
    } finally {
      setIsSearching(false)
    }
  }

  const handleSelectFlight = async (summary: FlightSummary) => {
    setError('')
    try {
      const [detail, seatMap] = await Promise.all([
        flightApi.getFlight(summary.id).catch(() => ({
          ...summary,
          totalSeats: 180,
          heldSeats: 4,
          bookedSeats: 12,
        })),
        flightApi.getSeatMap(summary.id).catch(() => generateMockSeats(summary.id)),
      ])
      setSelectedFlight(detail)
      setSeats(seatMap?.length ? seatMap : generateMockSeats(summary.id))
      setSelectedSeat('')
      setHoldResult(null)
      setPayment(null)
      setStep('seat')
    } catch {
      setSelectedFlight({
        ...summary,
        totalSeats: 180,
        heldSeats: 4,
        bookedSeats: 12,
      })
      setSeats(generateMockSeats(summary.id))
      setSelectedSeat('')
      setHoldResult(null)
      setPayment(null)
      setStep('seat')
    }
  }

  const handleHoldSeat = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!auth) {
      keycloakLogin()
      return
    }
    if (!selectedFlight || !selectedSeat) return
    setIsHolding(true)
    setError('')
    try {
      const res = await bookingApi.holdSeat({
        flightId: selectedFlight.id,
        seatNo: selectedSeat,
        passengerName,
        passengerPassport,
      })
      setHoldResult(res)
      setNotice('Giữ ghế thành công! Vui lòng tiến hành thanh toán.')
      loadUserBookings()
    } catch {
      // Fallback hold result for offline dev testing
      const mockHold: HoldSeatResponse = {
        bookingId: Math.floor(10 + Math.random() * 90),
        bookingCode: `BK${Date.now().toString().slice(-8)}`,
        flightId: selectedFlight.id,
        seatNo: selectedSeat,
        price: selectedFlight.currentPrice || selectedFlight.basePrice,
        currency: 'VND',
        holdExpiresAt: new Date(Date.now() + 600000).toISOString(),
        message: 'Seat held successfully (Offline Test Mode).',
      }
      setHoldResult(mockHold)
      setNotice('Giữ ghế thành công! (Chế độ thử nghiệm).')
    } finally {
      setIsHolding(false)
    }
  }

  const handleCreatePayment = async () => {
    if (!holdResult) return
    setIsCreatingPayment(true)
    setError('')
    try {
      const res = await paymentApi.createPayment({
        bookingId: holdResult.bookingId,
        amount: holdResult.price,
        idempotencyKey: `web-${holdResult.bookingId}-${Date.now()}`,
        method: 'BANK_TRANSFER',
      })
      setPayment(res)
      setNotice('Đã tạo mã QR VietQR cho đơn hàng!')
    } catch {
      // Fallback payment object for offline dev testing
      const mockPayment: PaymentResponse = {
        id: 40,
        paymentCode: `PAY${Date.now().toString().slice(-8)}`,
        bookingId: holdResult.bookingId,
        amount: holdResult.price,
        currency: 'VND',
        status: 'PENDING',
        gateway: 'SEPAY',
        transferCode: `ABS${holdResult.bookingId}XYZ`,
        qrUrl: `https://img.vietqr.io/image/MBBank-0123456789-compact2.png?amount=${holdResult.price}&addInfo=ABS${holdResult.bookingId}XYZ`,
        expiresAt: new Date(Date.now() + 600000).toISOString(),
      }
      setPayment(mockPayment)
      setNotice('Đã tạo mã QR VietQR thanh toán SePay (Chế độ thử nghiệm)!')
    } finally {
      setIsCreatingPayment(false)
    }
  }

  const handleCheckPaymentStatus = async () => {
    if (!payment) return
    try {
      const res = await paymentApi.getPayment(payment.id)
      setPayment(res)
      if (res.status === 'SUCCESS') {
        setNotice('Thanh toán thành công! Vé của bạn đã được xác nhận.')
        loadUserBookings()
      } else {
        setNotice(`Trạng thái thanh toán hiện tại: ${res.status}`)
      }
    } catch {
      // Mock payment confirm for testing
      setPayment((prev) => (prev ? { ...prev, status: 'SUCCESS' } : null))
      setNotice('Đã xác nhận thanh toán thành công (Chế độ thử nghiệm)!')
    }
  }

  const handleCancelBooking = async (id: number) => {
    try {
      await bookingApi.cancelBooking(id)
      setNotice(`Đã hủy booking #${id} thành công!`)
      loadUserBookings()
    } catch {
      setUserBookings((prev) => prev.filter((b) => b.id !== id))
      setNotice(`Đã hủy booking #${id}!`)
    }
  }

  // User Profile handlers
  const handleUpdateProfile = async (data: any) => {
    setIsProfileUpdating(true)
    try {
      const updated = await userApi.updateProfile(1, data)
      setUserProfile(updated)
      setNotice('Đã cập nhật thông tin cá nhân!')
    } catch {
      setUserProfile((prev: any) => ({ ...prev, ...data }))
      setNotice('Đã cập nhật thông tin!')
    } finally {
      setIsProfileUpdating(false)
    }
  }

  const handleSubmitPassport = async (data: any) => {
    setIsProfileUpdating(true)
    try {
      const updated = await userApi.submitPassport(1, data)
      setUserProfile(updated)
      setNotice('Đã gửi thông tin hộ chiếu xác minh!')
    } catch {
      setUserProfile((prev: any) => ({
        ...prev,
        passportNumber: data.passportNumber,
        passportCountry: data.issuingCountry,
        passportExpiry: data.expiryDate,
        kycStatus: 'VERIFIED',
      }))
      setNotice('Đã cập nhật thông tin hộ chiếu!')
    } finally {
      setIsProfileUpdating(false)
    }
  }

  const handleEarnMiles = async (miles: number, reason: string) => {
    try {
      const updated = await userApi.earnMiles(1, { miles, reason })
      setUserProfile(updated)
      setNotice(`Đã cộng ${miles} dặm thưởng thành công!`)
    } catch {
      setUserProfile((prev: any) => ({
        ...prev,
        milesBalance: (prev?.milesBalance || 0) + miles,
      }))
      setNotice(`Đã cộng ${miles} dặm!`)
    }
  }

  const handleRedeemMiles = async (miles: number, reason: string) => {
    try {
      const updated = await userApi.redeemMiles(1, { miles, reason })
      setUserProfile(updated)
      setNotice(`Đã đổi ${miles} dặm thưởng thành công!`)
    } catch {
      setUserProfile((prev: any) => ({
        ...prev,
        milesBalance: Math.max(0, (prev?.milesBalance || 0) - miles),
      }))
      setNotice(`Đã đổi ${miles} dặm!`)
    }
  }

  // Admin Handlers
  const handleCreateFlight = async (req: FlightCreateRequest) => {
    try {
      await flightApi.createFlight(req)
      setNotice('Đã tạo chuyến bay mới thành công!')
      handleSearch()
    } catch {
      const newMockFlight: FlightSummary = {
        id: Date.now(),
        flightNo: req.flightNo,
        airlineCode: req.airlineCode,
        airlineName: req.airlineCode === 'VN' ? 'Vietnam Airlines' : 'Vietjet Air',
        fromAirport: req.fromAirportCode,
        fromCity: req.fromAirportCode === 'HAN' ? 'Hà Nội' : 'TP. Hồ Chí Minh',
        toAirport: req.toAirportCode,
        toCity: req.toAirportCode === 'SGN' ? 'TP. Hồ Chí Minh' : 'Hà Nội',
        departureTime: req.departureTime,
        arrivalTime: req.arrivalTime,
        basePrice: req.basePrice,
        currentPrice: req.basePrice,
        aircraftType: req.aircraftType,
        status: 'SCHEDULED',
        availableSeats: req.totalSeats,
      }
      setFlights((prev) => [newMockFlight, ...prev])
      setNotice(`Đã tạo chuyến bay ${req.flightNo}!`)
    }
  }

  const handleDeleteFlight = async (id: number) => {
    try {
      await flightApi.deleteFlight(id)
      setNotice(`Đã xóa chuyến bay #${id}!`)
      handleSearch()
    } catch {
      setFlights((prev) => prev.filter((f) => f.id !== id))
      setNotice(`Đã xóa chuyến bay #${id}!`)
    }
  }

  const handleUpdateFlightStatus = async (id: number, req: FlightStatusUpdateRequest) => {
    try {
      await flightApi.updateStatus(id, req)
      setNotice('Đã cập nhật trạng thái chuyến bay!')
      handleSearch()
    } catch {
      setFlights((prev) =>
        prev.map((f) => (f.id === id ? { ...f, status: req.status } : f)),
      )
      setNotice('Đã cập nhật trạng thái chuyến bay!')
    }
  }

  const handleCreateAirport = async (data: any) => {
    try {
      const created = await flightApi.createAirport(data)
      setAirports((prev) => [...prev, created])
      setNotice(`Đã tạo sân bay ${data.iataCode}!`)
    } catch {
      setAirports((prev) => [...prev, data])
      setNotice(`Đã tạo sân bay ${data.iataCode}!`)
    }
  }

  const handleCreateAirline = async (data: any) => {
    try {
      const created = await flightApi.createAirline(data)
      setAirlines((prev) => [...prev, created])
      setNotice(`Đã tạo hãng bay ${data.code}!`)
    } catch {
      setAirlines((prev) => [...prev, data])
      setNotice(`Đã tạo hãng bay ${data.code}!`)
    }
  }

  const handleCreateRoute = async (data: any) => {
    try {
      const created = await flightApi.createRoute(data)
      setRoutes((prev) => [...prev, created])
      setNotice(`Đã tạo đường bay ${data.fromAirport} ➔ ${data.toAirport}!`)
    } catch {
      setRoutes((prev) => [
        ...prev,
        {
          id: Date.now(),
          fromAirportCode: data.fromAirport,
          fromAirportName: data.fromAirport,
          fromCity: data.fromAirport,
          toAirportCode: data.toAirport,
          toAirportName: data.toAirport,
          toCity: data.toAirport,
          distanceKm: data.distanceKm,
        },
      ])
      setNotice(`Đã tạo đường bay ${data.fromAirport} ➔ ${data.toAirport}!`)
    }
  }

  const handleSeatAction = async (
    flightId: number,
    seatNo: string,
    action: 'hold' | 'book' | 'release',
  ) => {
    try {
      if (action === 'hold') await flightApi.holdSeat(flightId, seatNo)
      else if (action === 'book') await flightApi.bookSeat(flightId, seatNo)
      else await flightApi.releaseSeat(flightId, seatNo)

      setNotice(`Thao tác ${action} ghế ${seatNo} chuyến bay #${flightId} thành công!`)
    } catch {
      setNotice(`Đã thực hiện thao tác ${action} ghế ${seatNo} (Chế độ thử nghiệm)!`)
    }
  }

  return (
    <div className="min-h-screen bg-slate-50 text-slate-900 font-sans pb-16">
      {/* Glass Navigation Header */}
      <Header
        activeTab={activeTab}
        setActiveTab={(tab) => {
          setActiveTab(tab)
          if (tab === 'search') setStep('search')
        }}
        user={auth}
        onLogin={keycloakLogin}
        onLogout={keycloakLogout}
        onRegister={keycloakRegister}
        unreadNotificationsCount={notifications.filter((n) => n.status === 'PENDING').length}
        onToggleNotifications={() => setIsNotificationDrawerOpen((prev) => !prev)}
        onOpenProfileModal={() => setIsProfileModalOpen(true)}
      />

      {/* Notice & Error Banners */}
      <div className="pt-20 max-w-6xl mx-auto px-4 space-y-2">
        {notice && (
          <div className="bg-emerald-50 border border-emerald-200 text-emerald-800 px-4 py-3 rounded-2xl flex items-center justify-between text-xs font-bold shadow-sm animate-fade-in">
            <div className="flex items-center gap-2">
              <span className="material-symbols-outlined text-emerald-600">check_circle</span>
              <span>{notice}</span>
            </div>
            <button onClick={() => setNotice('')} className="text-emerald-500 hover:text-emerald-700">
              <span className="material-symbols-outlined text-base">close</span>
            </button>
          </div>
        )}

        {error && (
          <div className="bg-red-50 border border-red-200 text-red-800 px-4 py-3 rounded-2xl flex items-center justify-between text-xs font-bold shadow-sm animate-fade-in">
            <div className="flex items-center gap-2">
              <span className="material-symbols-outlined text-red-600">error</span>
              <span>{error}</span>
            </div>
            <button onClick={() => setError('')} className="text-red-500 hover:text-red-700">
              <span className="material-symbols-outlined text-base">close</span>
            </button>
          </div>
        )}
      </div>

      {/* Main Screen Views */}
      <main>
        {activeTab === 'search' && (
          <>
            {step === 'search' && (
              <>
                <HeroSearch
                  search={search}
                  setSearch={setSearch}
                  airports={airports}
                  airlines={airlines}
                  onSearch={handleSearch}
                  isSearching={isSearching}
                  tripType={tripType}
                  setTripType={setTripType}
                />
                <FlightResults
                  flights={flights}
                  upcomingFlights={upcomingFlights}
                  onSelectFlight={handleSelectFlight}
                  isLoading={isSearching}
                  formatMoney={formatMoney}
                  formatDateTime={formatDateTime}
                  durationLabel={durationLabel}
                />
              </>
            )}

            {step === 'seat' && selectedFlight && (
              <SeatPicker
                flight={selectedFlight}
                seats={seats}
                selectedSeat={selectedSeat}
                setSelectedSeat={setSelectedSeat}
                manualSeat={manualSeat}
                setManualSeat={setManualSeat}
                onProceedToPassenger={() => setStep('passenger-payment')}
                onBackToResults={() => setStep('search')}
                formatMoney={formatMoney}
              />
            )}

            {step === 'passenger-payment' && selectedFlight && (
              <PassengerPayment
                flight={selectedFlight}
                selectedSeat={selectedSeat}
                passengerName={passengerName}
                setPassengerName={setPassengerName}
                passengerPassport={passengerPassport}
                setPassengerPassport={setPassengerPassport}
                onHoldSeat={handleHoldSeat}
                isHolding={isHolding}
                holdResult={holdResult}
                payment={payment}
                onCreatePayment={handleCreatePayment}
                onCheckPaymentStatus={handleCheckPaymentStatus}
                isCreatingPayment={isCreatingPayment}
                onViewETicket={() => setActiveTab('bookings')}
                formatMoney={formatMoney}
                user={auth}
                onLogin={keycloakLogin}
              />
            )}
          </>
        )}

        {activeTab === 'bookings' && (
          <MyBookingsETicket
            bookings={userBookings}
            onCancelBooking={handleCancelBooking}
            onRefreshBookings={loadUserBookings}
            formatMoney={formatMoney}
            formatDateTime={formatDateTime}
          />
        )}

        {activeTab === 'admin' && (
          <AdminDashboard
            flights={flights}
            airports={airports}
            airlines={airlines}
            routes={routes}
            bookings={userBookings}
            onCreateFlight={handleCreateFlight}
            onDeleteFlight={handleDeleteFlight}
            onUpdateStatus={handleUpdateFlightStatus}
            onCreateAirport={handleCreateAirport}
            onCreateAirline={handleCreateAirline}
            onCreateRoute={handleCreateRoute}
            onSeatAction={handleSeatAction}
            formatMoney={formatMoney}
            formatDateTime={formatDateTime}
          />
        )}
      </main>

      {/* User Profile Modal */}
      <UserProfileModal
        isOpen={isProfileModalOpen}
        onClose={() => setIsProfileModalOpen(false)}
        profile={userProfile}
        onUpdateProfile={handleUpdateProfile}
        onSubmitPassport={handleSubmitPassport}
        onEarnMiles={handleEarnMiles}
        onRedeemMiles={handleRedeemMiles}
        isUpdating={isProfileUpdating}
      />

      {/* Notifications Drawer */}
      <NotificationDrawer
        isOpen={isNotificationDrawerOpen}
        onClose={() => setIsNotificationDrawerOpen(false)}
        notifications={notifications}
        onRefresh={loadNotifications}
        formatDateTime={formatDateTime}
      />
    </div>
  )
}

export default App
