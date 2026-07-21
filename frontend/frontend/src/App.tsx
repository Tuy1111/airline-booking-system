import { useEffect, useState } from 'react'
import './App.css'
import { Header } from './components/Header'
import { HeroSearch } from './components/HeroSearch'
import { PopularDestinations } from './components/PopularDestinations'
import { FlightSearchPage } from './components/FlightSearchPage'
import { SeatPicker } from './components/SeatPicker'
import { PassengerPayment } from './components/PassengerPayment'
import { MyBookingsETicket } from './components/MyBookingsETicket'
import { UserProfilePage } from './components/UserProfilePage'
import { NotificationDrawer } from './components/NotificationDrawer'
import { AdminDashboard } from './components/AdminDashboard'
import { ToastContainer, type ToastMessage } from './components/ToastContainer'

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
  isAdminUser,
  login as keycloakLogin,
  logout as keycloakLogout,
  register as keycloakRegister,
} from './shared/auth/keycloak'

function dateInputValue(date = new Date()) {
  const localDate = new Date(date.getTime() - date.getTimezoneOffset() * 60000)
  return localDate.toISOString().slice(0, 10)
}

const defaultSearchDate = dateInputValue()

const emptySearch: FlightSearchParams = {
  from: '',
  to: '',
  date: '',
  passengers: 1,
  status: '',
  airline: '',
  minPrice: '',
  maxPrice: '',
  dateTo: '',
  sort: 'departureTime',
  order: 'asc',
}

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

function getErrorMessage(error: unknown) {
  return error instanceof Error ? error.message : String(error)
}

export function App() {
  const [auth] = useState(() => currentUser())
  const isAdmin = isAdminUser(auth)
  const [activeTab, setActiveTab] = useState<'home' | 'search' | 'bookings' | 'profile' | 'admin'>('home')
  const [step, setStep] = useState<'list' | 'seat' | 'passenger-payment'>('list')
  const [tripType, setTripType] = useState<'one-way' | 'round-trip'>('round-trip')

  // Toast Notification System
  const [toasts, setToasts] = useState<ToastMessage[]>([])

  const addToast = (type: 'success' | 'error' | 'info' | 'warning', message: string) => {
    const id = Date.now().toString() + Math.random().toString().slice(2, 6)
    setToasts((prev) => [...prev, { id, type, message }])
  }

  const dismissToast = (id: string) => {
    setToasts((prev) => prev.filter((t) => t.id !== id))
  }

  const [isSearching, setIsSearching] = useState(false)
  const [isHolding, setIsHolding] = useState(false)
  const [isCreatingPayment, setIsCreatingPayment] = useState(false)
  const [isProfileUpdating, setIsProfileUpdating] = useState(false)

  // Master Data loaded directly from backend API
  const [airports, setAirports] = useState<Airport[]>([])
  const [airlines, setAirlines] = useState<Airline[]>([])
  const [routes, setRoutes] = useState<RouteInfo[]>([])

  // Search & Flight State
  const [search, setSearch] = useState<FlightSearchParams>({ ...emptySearch })

  const [flights, setFlights] = useState<FlightSummary[]>([])
  const [searchError, setSearchError] = useState('')
  const [selectedFlight, setSelectedFlight] = useState<FlightDetail | null>(null)
  const [seats, setSeats] = useState<SeatMapItem[]>([])
  const [selectedSeat, setSelectedSeat] = useState('')
  const [manualSeat, setManualSeat] = useState('1A')

  // Booking & Payment State
  const [passengerName, setPassengerName] = useState(auth?.fullName || 'Nguyen Van A')
  const [passengerPassport, setPassengerPassport] = useState('P1234567')
  const [extraBaggageKg, setExtraBaggageKg] = useState<number>(0)
  const [holdResult, setHoldResult] = useState<HoldSeatResponse | null>(null)

  const [payment, setPayment] = useState<PaymentResponse | null>(null)
  const [userBookings, setUserBookings] = useState<BookingDetail[]>([])
  const [adminFlights, setAdminFlights] = useState<FlightSummary[]>([])
  const [adminBookings, setAdminBookings] = useState<BookingDetail[]>([])

  // User Profile State
  const [userProfile, setUserProfile] = useState<UserView | null>(null)
  const [isProfileLoading, setIsProfileLoading] = useState(false)

  // Notifications State
  const [notifications, setNotifications] = useState<NotificationItem[]>([])
  const [unreadNotificationsCount, setUnreadNotificationsCount] = useState(0)
  const [isNotificationDrawerOpen, setIsNotificationDrawerOpen] = useState(false)

  // Fetch real master data from backend APIs on startup
  useEffect(() => {
    flightApi
      .getAirports()
      .then((items) => items.length && setAirports(items))
      .catch((err) => console.warn('Airports API error:', err))

    flightApi
      .getAirlines()
      .then((items) => items.length && setAirlines(items))
      .catch((err) => console.warn('Airlines API error:', err))

    flightApi
      .getRoutes()
      .then(setRoutes)
      .catch((err) => console.warn('Routes API error:', err))

    setIsSearching(true)
    flightApi
      .searchFlights(emptySearch)
      .then((items) => {
        setFlights(items)
        setAirports((current) => {
          const options = new Map(current.map((airport) => [airport.iataCode, airport]))
          items.forEach((flight) => {
            options.set(flight.fromAirport, options.get(flight.fromAirport) ?? { iataCode: flight.fromAirport, city: flight.fromCity, name: flight.fromCity, country: 'VN' })
            options.set(flight.toAirport, options.get(flight.toAirport) ?? { iataCode: flight.toAirport, city: flight.toCity, name: flight.toCity, country: 'VN' })
          })
          return [...options.values()]
        })
        setAirlines((current) => {
          const options = new Map(current.map((airline) => [airline.code, airline]))
          items.forEach((flight) => options.set(flight.airlineCode, { code: flight.airlineCode, name: flight.airlineName }))
          return [...options.values()]
        })
      })
      .catch((err) => setSearchError(getErrorMessage(err)))
      .finally(() => setIsSearching(false))
  }, [])

  // Fetch real User Data if logged in
  useEffect(() => {
    if (auth) {
      if (!isAdmin) loadUserBookings()
      loadUserProfile()
    }
  }, [auth, isAdmin])

  useEffect(() => {
    if (!auth) {
      setNotifications([])
      setUnreadNotificationsCount(0)
      return
    }

    loadNotifications()
    const intervalId = window.setInterval(() => {
      if (document.visibilityState === 'visible') loadNotifications()
    }, 30_000)

    return () => window.clearInterval(intervalId)
  }, [auth])

  // Poll an active SePay payment until the backend reaches a terminal state.
  // The webhook updates the backend asynchronously, so the payment screen
  // must not rely on the user clicking the manual status-check button.
  useEffect(() => {
    if (!payment || payment.status !== 'PENDING') return

    const intervalId = window.setInterval(async () => {
      try {
        const latest = await paymentApi.getPayment(payment.id)
        setPayment(latest)
        if (latest.status === 'SUCCESS') {
          addToast('success', 'Thanh toán thành công! Vé của bạn đã được xác nhận.')
          loadUserBookings()
        } else if (latest.status === 'FAILED') {
          addToast('error', 'Thanh toán thất bại hoặc đã hết hạn giữ ghế.')
        }
      } catch (err) {
        console.warn('Could not poll payment status:', err)
      }
    }, 3000)

    return () => window.clearInterval(intervalId)
  }, [payment?.id, payment?.status])

  useEffect(() => {
    if (isAdmin && activeTab === 'admin') loadAdminData()
  }, [activeTab, isAdmin])

  const loadUserProfile = async () => {
    setIsProfileLoading(true)
    try {
      const p = await userApi.getProfile()
      setUserProfile(p)
    } catch (err) {
      console.warn('Could not load user profile:', err)
    } finally {
      setIsProfileLoading(false)
    }
  }

  const loadUserBookings = async () => {
    try {
      const res = await bookingApi.getMine()
      setUserBookings(res.content || [])
    } catch (err) {
      console.warn('Could not load user bookings:', err)
    }
  }

  const loadNotifications = async () => {
    try {
      const [res, unread] = await Promise.all([
        notificationApi.mine(),
        notificationApi.unreadCount(),
      ])
      setNotifications(res.content || [])
      setUnreadNotificationsCount(unread.unreadCount)
    } catch (err) {
      console.warn('Could not load notifications:', err)
    }
  }

  const handleToggleNotifications = () => {
    const opening = !isNotificationDrawerOpen
    setIsNotificationDrawerOpen(opening)
    if (opening) loadNotifications()
  }

  const handleMarkNotificationRead = async (id: number) => {
    const current = notifications.find((item) => item.id === id)
    if (!current || current.readAt) return

    try {
      const updated = await notificationApi.markRead(id)
      setNotifications((items) => items.map((item) => (item.id === id ? updated : item)))
      setUnreadNotificationsCount((count) => Math.max(0, count - 1))
    } catch (err) {
      addToast('error', getErrorMessage(err))
    }
  }

  const handleMarkAllNotificationsRead = async () => {
    try {
      await notificationApi.markAllRead()
      const readAt = new Date().toISOString()
      setNotifications((items) => items.map((item) => ({ ...item, readAt: item.readAt ?? readAt })))
      setUnreadNotificationsCount(0)
    } catch (err) {
      addToast('error', getErrorMessage(err))
    }
  }

  const loadAdminData = async () => {
    try {
      const [flightItems, firstBookingPage] = await Promise.all([
        flightApi.getAdminFlights(),
        bookingApi.getAdminBookings(),
      ])
      const remainingBookingPages = firstBookingPage.totalPages > 1
        ? await Promise.all(
            Array.from(
              { length: firstBookingPage.totalPages - 1 },
              (_, index) => bookingApi.getAdminBookings(index + 1),
            ),
          )
        : []
      setAdminFlights(flightItems || [])
      setAdminBookings([
        ...(firstBookingPage.content || []),
        ...remainingBookingPages.flatMap((page) => page.content || []),
      ])
    } catch (err) {
      addToast('error', getErrorMessage(err))
    }
  }

  const handleSearch = async (e?: React.FormEvent) => {
    if (e) e.preventDefault()
    setIsSearching(true)
    setSearchError('')
    try {
      const res = await flightApi.searchFlights(search)
      setFlights(res || [])
    } catch (err) {
      const message = getErrorMessage(err)
      setSearchError(message)
      addToast('error', message)
      setFlights([])
    } finally {
      setIsSearching(false)
      setActiveTab('search')
      setStep('list')
    }
  }

  const handleResetSearch = async () => {
    const next = { ...emptySearch }
    setSearch(next)
    setTripType('one-way')
    setIsSearching(true)
    setSearchError('')
    try {
      setFlights((await flightApi.searchFlights(next)) || [])
    } catch (err) {
      const message = getErrorMessage(err)
      setSearchError(message)
      addToast('error', message)
    } finally {
      setIsSearching(false)
    }
  }

  const handleSelectPromoRoute = async (from: string, to: string) => {
    const next = { ...emptySearch, from, to, date: defaultSearchDate }
    setSearch(next)
    setActiveTab('search')
    setStep('list')
    setIsSearching(true)
    setSearchError('')
    try {
      setFlights((await flightApi.searchFlights(next)) || [])
    } catch (err) {
      const message = getErrorMessage(err)
      setSearchError(message)
      addToast('error', message)
    } finally {
      setIsSearching(false)
    }
  }

  const handleSelectFlight = async (summary: FlightSummary) => {
    if (isAdmin) {
      addToast('warning', 'Tài khoản quản trị không được đặt vé.')
      return
    }
    try {
      const [detail, seatMap] = await Promise.all([
        flightApi.getFlight(summary.id),
        flightApi.getSeatMap(summary.id),
      ])
      setSelectedFlight(detail)
      setSeats(seatMap || [])
      setSelectedSeat('')
      setHoldResult(null)
      setPayment(null)
      setStep('seat')
    } catch (err) {
      addToast('error', getErrorMessage(err))
    }
  }

  const handleHoldSeat = async (e: React.FormEvent) => {
    e.preventDefault()
    if (isAdmin) {
      addToast('warning', 'Tài khoản quản trị không được giữ chỗ.')
      return
    }
    if (!auth) {
      keycloakLogin()
      return
    }
    if (!selectedFlight || !selectedSeat) return
    setIsHolding(true)
    try {
      const res = await bookingApi.holdSeat({
        flightId: selectedFlight.id,
        seatNo: selectedSeat,
        passengerName,
        passengerPassport,
        extraBaggageKg,
      })
      setHoldResult(res)
      addToast('success', 'Giữ ghế thành công! Vui lòng tiến hành thanh toán.')
      loadUserBookings()
    } catch (err) {
      addToast('error', getErrorMessage(err))
    } finally {
      setIsHolding(false)
    }
  }

  const handleCreatePayment = async () => {
    if (isAdmin) {
      addToast('warning', 'Tài khoản quản trị không được thanh toán.')
      return
    }
    if (!holdResult) return
    setIsCreatingPayment(true)
    try {
      const res = await paymentApi.createPayment({
        bookingId: holdResult.bookingId,
        amount: holdResult.price,
        idempotencyKey: `web-${holdResult.bookingId}-${Date.now()}`,
        method: 'BANK_TRANSFER',
      })
      setPayment(res)
      addToast('success', 'Đã tạo mã QR VietQR cho đơn hàng!')
    } catch (err) {
      addToast('error', getErrorMessage(err))
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
        addToast('success', 'Thanh toán thành công! Vé của bạn đã được xác nhận.')
        loadUserBookings()
      } else {
        addToast('info', `Trạng thái thanh toán hiện tại: ${res.status}`)
      }
    } catch (err) {
      addToast('error', getErrorMessage(err))
    }
  }

  const handleCancelBooking = async (id: number) => {
    try {
      await bookingApi.cancelBooking(id)
      addToast('success', `Đã hủy booking #${id} thành công!`)
      loadUserBookings()
    } catch (err) {
      addToast('error', getErrorMessage(err))
    }
  }

  // User Profile handlers
  const handleUpdateProfile = async (data: any) => {
    setIsProfileUpdating(true)
    try {
      const updated = await userApi.updateProfile(data)
      setUserProfile(updated)
      addToast('success', 'Đã cập nhật thông tin cá nhân!')
    } catch (err) {
      addToast('error', getErrorMessage(err))
    } finally {
      setIsProfileUpdating(false)
    }
  }

  const handleSubmitPassport = async (data: any) => {
    setIsProfileUpdating(true)
    try {
      const updated = await userApi.submitPassport(data)
      setUserProfile(updated)
      await loadNotifications()
      addToast(
        updated.kycStatus === 'VERIFIED' ? 'success' : 'warning',
        updated.kycStatus === 'VERIFIED'
          ? 'Hộ chiếu đã được xác minh thành công!'
          : 'Hộ chiếu không được chấp nhận.',
      )
    } catch (err) {
      addToast('error', getErrorMessage(err))
    } finally {
      setIsProfileUpdating(false)
    }
  }

  const handleEarnMiles = async (miles: number, reason: string) => {
    try {
      const updated = await userApi.earnMiles({ miles, reason })
      setUserProfile(updated)
      addToast('success', `Đã cộng ${miles} dặm thưởng thành công!`)
    } catch (err) {
      addToast('error', getErrorMessage(err))
    }
  }

  const handleRedeemMiles = async (miles: number, reason: string) => {
    try {
      const updated = await userApi.redeemMiles({ miles, reason })
      setUserProfile(updated)
      addToast('success', `Đã đổi ${miles} dặm thưởng thành công!`)
    } catch (err) {
      addToast('error', getErrorMessage(err))
    }
  }

  // Admin Handlers
  const handleCreateFlight = async (req: FlightCreateRequest) => {
    try {
      await flightApi.createFlight(req)
      addToast('success', 'Đã tạo chuyến bay mới thành công!')
      loadAdminData()
    } catch (err) {
      addToast('error', getErrorMessage(err))
    }
  }

  const handleDeleteFlight = async (id: number) => {
    try {
      await flightApi.deleteFlight(id)
      addToast('success', `Đã xóa chuyến bay #${id}!`)
      loadAdminData()
    } catch (err) {
      addToast('error', getErrorMessage(err))
    }
  }

  const handleUpdateFlightStatus = async (id: number, req: FlightStatusUpdateRequest) => {
    try {
      await flightApi.updateStatus(id, req)
      addToast('success', 'Đã cập nhật trạng thái chuyến bay!')
      loadAdminData()
    } catch (err) {
      addToast('error', getErrorMessage(err))
    }
  }

  const handleCreateAirport = async (data: any) => {
    try {
      const created = await flightApi.createAirport(data)
      setAirports((prev) => [...prev, created])
      addToast('success', `Đã tạo sân bay ${data.iataCode}!`)
    } catch (err) {
      addToast('error', getErrorMessage(err))
    }
  }

  const handleCreateAirline = async (data: any) => {
    try {
      const created = await flightApi.createAirline(data)
      setAirlines((prev) => [...prev, created])
      addToast('success', `Đã tạo hãng bay ${data.code}!`)
    } catch (err) {
      addToast('error', getErrorMessage(err))
    }
  }

  const handleCreateRoute = async (data: any) => {
    try {
      const created = await flightApi.createRoute(data)
      setRoutes((prev) => [...prev, created])
      addToast('success', `Đã tạo đường bay ${data.fromAirport} ➔ ${data.toAirport}!`)
    } catch (err) {
      addToast('error', getErrorMessage(err))
    }
  }

  return (
    <div className={`site-shell ${activeTab === 'admin' ? 'admin-mode' : ''}`}>
      <a className="skip-link" href="#main-content">Bỏ qua điều hướng</a>
      {/* Floating Toast Container */}
      <ToastContainer toasts={toasts} onDismiss={dismissToast} />

      {/* Glass Navigation Header */}
      <Header
        activeTab={activeTab}
        setActiveTab={(tab) => {
          setActiveTab(tab)
          if (tab === 'search') setStep('list')
        }}
        user={auth}
        onLogin={keycloakLogin}
        onLogout={keycloakLogout}
        onRegister={keycloakRegister}
        unreadNotificationsCount={unreadNotificationsCount}
        onToggleNotifications={handleToggleNotifications}
      />

      {/* Main Screen Views */}
      <main id="main-content" tabIndex={-1}>
        {/* TAB 1: HOMEPAGE (Trang chủ sạch đẹp với Hero Slogan & Promos) */}
        {activeTab === 'home' && (
          <>
            <HeroSearch onGoToSearch={() => setActiveTab('search')} />
            <PopularDestinations
              onSelectRoute={handleSelectPromoRoute}
              formatMoney={formatMoney}
            />
          </>
        )}

        {/* TAB 2: FLIGHT SEARCH PAGE (Trang Tìm kiếm chuyến bay riêng biệt với cụm Search Card chuẩn ở trên & 2 cột kết quả bên dưới) */}
        {activeTab === 'search' && (
          <>
            {step === 'list' && (
              <FlightSearchPage
                search={search}
                setSearch={setSearch}
                airports={airports}
                airlines={airlines}
                flights={flights}
                onSearch={handleSearch}
                onReset={handleResetSearch}
                onSelectFlight={handleSelectFlight}
                isLoading={isSearching}
                error={searchError}
                tripType={tripType}
                setTripType={setTripType}
                formatMoney={formatMoney}
                formatDateTime={formatDateTime}
                durationLabel={durationLabel}
                canBook={!isAdmin}
              />
            )}

            {step === 'seat' && selectedFlight && (
              <div className="booking-workflow booking-workflow-seat max-w-6xl mx-auto px-4 pt-24">
                <div className="workflow-breadcrumb flex items-center gap-2 text-xs font-bold text-slate-500 py-3 border-b border-slate-200/80 mb-6">
                  <button
                    onClick={() => setActiveTab('home')}
                    className="hover:text-slate-900 flex items-center gap-1"
                  >
                    <span className="material-symbols-outlined text-base">home</span> Trang chủ
                  </button>
                  <span>/</span>
                  <button onClick={() => setStep('list')} className="hover:text-slate-900">
                    Tìm chuyến bay
                  </button>
                  <span>/</span>
                  <span className="text-sky-600 font-extrabold">Chọn chỗ ngồi</span>
                </div>

                <SeatPicker
                  flight={selectedFlight}
                  seats={seats}
                  selectedSeat={selectedSeat}
                  setSelectedSeat={setSelectedSeat}
                  manualSeat={manualSeat}
                  setManualSeat={setManualSeat}
                  onProceedToPassenger={() => setStep('passenger-payment')}
                  onBackToResults={() => setStep('list')}
                  formatMoney={formatMoney}
                />
              </div>
            )}

            {step === 'passenger-payment' && selectedFlight && (
              <div className="booking-workflow booking-workflow-payment max-w-6xl mx-auto px-4 pt-24">
                <div className="workflow-breadcrumb flex items-center gap-2 text-xs font-bold text-slate-500 py-3 border-b border-slate-200/80 mb-6">
                  <button
                    onClick={() => setActiveTab('home')}
                    className="hover:text-slate-900 flex items-center gap-1"
                  >
                    <span className="material-symbols-outlined text-base">home</span> Trang chủ
                  </button>
                  <span>/</span>
                  <button onClick={() => setStep('list')} className="hover:text-slate-900">
                    Tìm chuyến bay
                  </button>
                  <span>/</span>
                  <button onClick={() => setStep('seat')} className="hover:text-slate-900">
                    Chọn chỗ ngồi
                  </button>
                  <span>/</span>
                  <span className="text-sky-600 font-extrabold">Thanh toán</span>
                </div>

                <PassengerPayment
                  flight={selectedFlight}
                  selectedSeat={selectedSeat}
                  passengerName={passengerName}
                  setPassengerName={setPassengerName}
                  passengerPassport={passengerPassport}
                  setPassengerPassport={setPassengerPassport}
                  extraBaggageKg={extraBaggageKg}
                  setExtraBaggageKg={setExtraBaggageKg}
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
              </div>
            )}
          </>
        )}

        {/* TAB 3: MY BOOKINGS */}
        {activeTab === 'bookings' && !isAdmin && (
          <div className="route-shell route-bookings pt-8">
            <MyBookingsETicket
              bookings={userBookings}
              onCancelBooking={handleCancelBooking}
              onRefreshBookings={loadUserBookings}
              formatMoney={formatMoney}
              formatDateTime={formatDateTime}
            />
          </div>
        )}

        {activeTab === 'profile' && auth && (
          isProfileLoading ? (
            <section className="mx-auto max-w-6xl px-6 py-16" aria-live="polite">
              <div className="h-8 w-56 animate-pulse rounded-lg bg-slate-200" />
              <div className="mt-8 h-80 animate-pulse rounded-3xl bg-slate-100" />
            </section>
          ) : userProfile ? (
            <UserProfilePage
              profile={userProfile}
              onBack={() => setActiveTab('home')}
              onUpdateProfile={handleUpdateProfile}
              onSubmitPassport={handleSubmitPassport}
              onEarnMiles={handleEarnMiles}
              onRedeemMiles={handleRedeemMiles}
              isUpdating={isProfileUpdating}
            />
          ) : (
            <section className="mx-auto max-w-xl px-6 py-20 text-center">
              <span className="material-symbols-outlined text-4xl text-slate-400">person_off</span>
              <h1 className="mt-4 text-2xl font-black tracking-tight text-slate-900">Không tải được hồ sơ</h1>
              <p className="mt-2 text-sm text-slate-500">Kiểm tra tài khoản người dùng rồi thử lại.</p>
              <button type="button" className="primary-button mt-6" onClick={loadUserProfile}>Thử lại</button>
            </section>
          )
        )}

        {/* TAB 4: ADMIN DASHBOARD */}
        {activeTab === 'admin' && isAdmin && (
          <div className="route-shell route-admin pt-8">
            <AdminDashboard
              flights={adminFlights}
              airports={airports}
              airlines={airlines}
              routes={routes}
              bookings={adminBookings}
              onCreateFlight={handleCreateFlight}
              onDeleteFlight={handleDeleteFlight}
              onUpdateStatus={handleUpdateFlightStatus}
              onCreateAirport={handleCreateAirport}
              onCreateAirline={handleCreateAirline}
              onCreateRoute={handleCreateRoute}
              formatMoney={formatMoney}
              formatDateTime={formatDateTime}
            />
          </div>
        )}
      </main>

      {/* Notifications Drawer */}
      <NotificationDrawer
        isOpen={isNotificationDrawerOpen}
        onClose={() => setIsNotificationDrawerOpen(false)}
        notifications={notifications}
        unreadCount={unreadNotificationsCount}
        onRefresh={loadNotifications}
        onMarkRead={handleMarkNotificationRead}
        onMarkAllRead={handleMarkAllNotificationsRead}
        formatDateTime={formatDateTime}
      />
    </div>
  )
}

export default App
