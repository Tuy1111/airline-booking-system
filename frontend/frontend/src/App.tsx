import { useEffect, useState } from 'react'
import './App.css'
import { Header } from './components/Header'
import { HeroSearch } from './components/HeroSearch'
import { PopularDestinations } from './components/PopularDestinations'
import { FlightResults } from './components/FlightResults'
import { SeatPicker } from './components/SeatPicker'
import { PassengerPayment } from './components/PassengerPayment'
import { MyBookingsETicket } from './components/MyBookingsETicket'
import { UserProfileModal } from './components/UserProfileModal'
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
  login as keycloakLogin,
  logout as keycloakLogout,
  register as keycloakRegister,
} from './shared/auth/keycloak'

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

function getErrorMessage(error: unknown) {
  return error instanceof Error ? error.message : String(error)
}

export function App() {
  const [auth] = useState(() => currentUser())
  const [activeTab, setActiveTab] = useState<'search' | 'bookings' | 'profile' | 'admin'>('search')
  const [step, setStep] = useState<'home' | 'results' | 'seat' | 'passenger-payment'>('home')
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

  const [flights, setFlights] = useState<FlightSummary[]>([])
  const [upcomingFlights, setUpcomingFlights] = useState<FlightSummary[]>([])
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

  // Fetch real master data from backend APIs on startup
  useEffect(() => {
    flightApi
      .getAirports()
      .then(setAirports)
      .catch((err) => console.warn('Airports API error:', err))

    flightApi
      .getAirlines()
      .then(setAirlines)
      .catch((err) => console.warn('Airlines API error:', err))

    flightApi
      .getRoutes()
      .then(setRoutes)
      .catch((err) => console.warn('Routes API error:', err))

    flightApi
      .upcoming(24)
      .then(setUpcomingFlights)
      .catch((err) => console.warn('Upcoming flights API error:', err))
  }, [])

  // Fetch real User Data if logged in
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
    } catch (err) {
      console.warn('Could not load user profile:', err)
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
      const res = await notificationApi.mine()
      setNotifications(res.content || [])
    } catch (err) {
      console.warn('Could not load notifications:', err)
    }
  }

  const handleSearch = async (e?: React.FormEvent) => {
    if (e) e.preventDefault()
    setIsSearching(true)
    try {
      const res = await flightApi.searchFlights(search)
      setFlights(res || [])
    } catch (err) {
      addToast('error', getErrorMessage(err))
      setFlights([])
    } finally {
      setIsSearching(false)
      setStep('results')
    }
  }

  const handleSelectPromoRoute = (from: string, to: string) => {
    setSearch((prev) => ({ ...prev, from, to }))
    handleSearch()
  }

  const handleSelectFlight = async (summary: FlightSummary) => {
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
      const updated = await userApi.updateProfile(1, data)
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
      const updated = await userApi.submitPassport(1, data)
      setUserProfile(updated)
      addToast('success', 'Đã gửi thông tin hộ chiếu xác minh!')
    } catch (err) {
      addToast('error', getErrorMessage(err))
    } finally {
      setIsProfileUpdating(false)
    }
  }

  const handleEarnMiles = async (miles: number, reason: string) => {
    try {
      const updated = await userApi.earnMiles(1, { miles, reason })
      setUserProfile(updated)
      addToast('success', `Đã cộng ${miles} dặm thưởng thành công!`)
    } catch (err) {
      addToast('error', getErrorMessage(err))
    }
  }

  const handleRedeemMiles = async (miles: number, reason: string) => {
    try {
      const updated = await userApi.redeemMiles(1, { miles, reason })
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
      handleSearch()
    } catch (err) {
      addToast('error', getErrorMessage(err))
    }
  }

  const handleDeleteFlight = async (id: number) => {
    try {
      await flightApi.deleteFlight(id)
      addToast('success', `Đã xóa chuyến bay #${id}!`)
      handleSearch()
    } catch (err) {
      addToast('error', getErrorMessage(err))
    }
  }

  const handleUpdateFlightStatus = async (id: number, req: FlightStatusUpdateRequest) => {
    try {
      await flightApi.updateStatus(id, req)
      addToast('success', 'Đã cập nhật trạng thái chuyến bay!')
      handleSearch()
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

  const handleSeatAction = async (
    flightId: number,
    seatNo: string,
    action: 'hold' | 'book' | 'release',
  ) => {
    try {
      if (action === 'hold') await flightApi.holdSeat(flightId, seatNo)
      else if (action === 'book') await flightApi.bookSeat(flightId, seatNo)
      else await flightApi.releaseSeat(flightId, seatNo)

      addToast('success', `Thao tác ${action} ghế ${seatNo} chuyến bay #${flightId} thành công!`)
    } catch (err) {
      addToast('error', getErrorMessage(err))
    }
  }

  return (
    <div className="min-h-screen bg-slate-50 text-slate-900 font-sans pb-16">
      {/* Floating Toast Container */}
      <ToastContainer toasts={toasts} onDismiss={dismissToast} />

      {/* Glass Navigation Header */}
      <Header
        activeTab={activeTab}
        setActiveTab={(tab) => {
          setActiveTab(tab)
          if (tab === 'search') setStep('home')
        }}
        user={auth}
        onLogin={keycloakLogin}
        onLogout={keycloakLogout}
        onRegister={keycloakRegister}
        unreadNotificationsCount={notifications.filter((n) => n.status === 'PENDING').length}
        onToggleNotifications={() => setIsNotificationDrawerOpen((prev) => !prev)}
        onOpenProfileModal={() => setIsProfileModalOpen(true)}
      />

      {/* Main Screen Views */}
      <main>
        {activeTab === 'search' && (
          <>
            {/* SCREEN 1: HOMEPAGE (Trang chủ & Công cụ tìm kiếm) */}
            {step === 'home' && (
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
                <PopularDestinations
                  onSelectRoute={handleSelectPromoRoute}
                  formatMoney={formatMoney}
                />
              </>
            )}

            {/* SCREEN 2: FLIGHT SEARCH RESULTS (Trang kết quả tìm kiếm riêng biệt) */}
            {step === 'results' && (
              <div className="max-w-6xl mx-auto px-4 pt-4">
                {/* Breadcrumbs */}
                <div className="flex items-center gap-2 text-xs font-bold text-slate-500 py-3 border-b border-slate-200/80 mb-6">
                  <button
                    onClick={() => setStep('home')}
                    className="hover:text-slate-900 flex items-center gap-1"
                  >
                    <span className="material-symbols-outlined text-base">home</span> Trang chủ
                  </button>
                  <span>/</span>
                  <span className="text-sky-600 font-extrabold">Kết quả tìm kiếm chuyến bay</span>
                </div>

                {/* Top Search Summary Banner */}
                <div className="bg-slate-900 text-white rounded-3xl p-6 mb-8 flex flex-wrap items-center justify-between gap-4 shadow-xl">
                  <div className="flex items-center gap-4">
                    <div className="w-12 h-12 rounded-2xl bg-sky-500 text-white flex items-center justify-center font-bold text-xl">
                      <span className="material-symbols-outlined text-2xl">flight_takeoff</span>
                    </div>
                    <div>
                      <h2 className="text-xl font-black tracking-tight flex items-center gap-2">
                        <span>{search.from}</span>
                        <span className="material-symbols-outlined text-sky-400 text-base">
                          arrow_forward
                        </span>
                        <span>{search.to}</span>
                      </h2>
                      <p className="text-xs text-slate-300 font-semibold mt-0.5">
                        Ngày đi: {search.date} • {search.passengers} hành khách •{' '}
                        {tripType === 'round-trip' ? 'Khứ hồi' : 'Một chiều'}
                      </p>
                    </div>
                  </div>

                  <button
                    onClick={() => setStep('home')}
                    className="px-5 py-2.5 bg-white/10 hover:bg-white/20 text-white rounded-xl text-xs font-bold backdrop-blur-md border border-white/20 transition-all flex items-center gap-1.5"
                  >
                    <span className="material-symbols-outlined text-base">edit</span>
                    Thay đổi tìm kiếm
                  </button>
                </div>

                <FlightResults
                  flights={flights}
                  upcomingFlights={upcomingFlights}
                  onSelectFlight={handleSelectFlight}
                  isLoading={isSearching}
                  formatMoney={formatMoney}
                  formatDateTime={formatDateTime}
                  durationLabel={durationLabel}
                />
              </div>
            )}

            {/* SCREEN 3: SEAT SELECTION */}
            {step === 'seat' && selectedFlight && (
              <div className="max-w-6xl mx-auto px-4 pt-4">
                <div className="flex items-center gap-2 text-xs font-bold text-slate-500 py-3 border-b border-slate-200/80 mb-6">
                  <button
                    onClick={() => setStep('home')}
                    className="hover:text-slate-900 flex items-center gap-1"
                  >
                    <span className="material-symbols-outlined text-base">home</span> Trang chủ
                  </button>
                  <span>/</span>
                  <button onClick={() => setStep('results')} className="hover:text-slate-900">
                    Kết quả tìm kiếm
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
                  onBackToResults={() => setStep('results')}
                  formatMoney={formatMoney}
                />
              </div>
            )}

            {/* SCREEN 4: PASSENGER INFO & PAYMENT */}
            {step === 'passenger-payment' && selectedFlight && (
              <div className="max-w-6xl mx-auto px-4 pt-4">
                <div className="flex items-center gap-2 text-xs font-bold text-slate-500 py-3 border-b border-slate-200/80 mb-6">
                  <button
                    onClick={() => setStep('home')}
                    className="hover:text-slate-900 flex items-center gap-1"
                  >
                    <span className="material-symbols-outlined text-base">home</span> Trang chủ
                  </button>
                  <span>/</span>
                  <button onClick={() => setStep('results')} className="hover:text-slate-900">
                    Kết quả tìm kiếm
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
