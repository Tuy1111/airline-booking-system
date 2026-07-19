import { useEffect, useMemo, useState, type FormEvent } from 'react'
import './App.css'
import { bookingApi } from './features/bookings/api'
import type { BookingDetail, BookingStatus, HoldSeatResponse } from './features/bookings/types'
import { flightApi } from './features/flights/api'
import type {
  Airline,
  Airport,
  FlightCreateRequest,
  FlightDetail,
  FlightSearchParams,
  FlightStatus,
  FlightSummary,
  RouteInfo,
  SeatMapItem,
} from './features/flights/types'
import { notificationApi } from './features/notifications/api'
import type { NotificationItem } from './features/notifications/types'
import { paymentApi } from './features/payments/api'
import type { PaymentResponse } from './features/payments/types'
import { pageItems } from './shared/api/page'
import {
  currentUser,
  login as keycloakLogin,
  logout as keycloakLogout,
  register as keycloakRegister,
} from './shared/auth/keycloak'
import heroImage from './assets/airline-hero.png'

type PageMode = 'user' | 'admin'
type BusyKey =
  | 'catalog'
  | 'search'
  | 'flight'
  | 'hold'
  | 'payment'
  | 'bookings'
  | 'notifications'
  | 'admin'
  | ''

function dateInputValue(date = new Date()) {
  const localDate = new Date(date.getTime() - date.getTimezoneOffset() * 60000)
  return localDate.toISOString().slice(0, 10)
}

function dateTimeInputValue(daysFromNow: number, hour: number, minute: number) {
  const date = new Date()
  date.setDate(date.getDate() + daysFromNow)
  date.setHours(hour, minute, 0, 0)
  const localDate = new Date(date.getTime() - date.getTimezoneOffset() * 60000)
  return localDate.toISOString().slice(0, 16)
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
  if (!value) {
    return 'Pending'
  }

  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return value
  }

  return dateTimeFormatter.format(date)
}

function durationLabel(start: string, end: string) {
  const diff = new Date(end).getTime() - new Date(start).getTime()
  if (!Number.isFinite(diff) || diff <= 0) {
    return 'N/A'
  }

  const minutes = Math.round(diff / 60000)
  const hours = Math.floor(minutes / 60)
  const remainder = minutes % 60
  return `${hours}h ${remainder}m`
}

function getErrorMessage(error: unknown) {
  return error instanceof Error ? error.message : String(error)
}

function nextFlightNo() {
  return `VN${Math.floor(300 + Math.random() * 600)}`
}

function statusTone(status: string) {
  if (['SCHEDULED', 'AVAILABLE', 'SUCCESS', 'SENT', 'CONFIRMED'].includes(status)) {
    return 'success'
  }

  if (['HELD', 'PENDING', 'DELAYED'].includes(status)) {
    return 'warning'
  }

  if (['BOOKED', 'CANCELLED', 'FAILED', 'EXPIRED', 'DEPARTED'].includes(status)) {
    return 'danger'
  }

  return 'neutral'
}

function sortSeats(seats: SeatMapItem[]) {
  return [...seats].sort((a, b) => {
    const left = /^(\d+)([A-Z])$/.exec(a.seatNo)
    const right = /^(\d+)([A-Z])$/.exec(b.seatNo)
    const leftRow = left ? Number(left[1]) : 0
    const rightRow = right ? Number(right[1]) : 0

    if (leftRow !== rightRow) {
      return leftRow - rightRow
    }

    return a.seatNo.localeCompare(b.seatNo)
  })
}

function groupSeats(seats: SeatMapItem[]) {
  return sortSeats(seats).reduce<{ row: string; seats: SeatMapItem[] }[]>((rows, seat) => {
    const match = /^(\d+)/.exec(seat.seatNo)
    const row = match?.[1] ?? 'Other'
    const current = rows.at(-1)

    if (current?.row === row) {
      current.seats.push(seat)
    } else {
      rows.push({ row, seats: [seat] })
    }

    return rows
  }, [])
}

function Badge({ value }: { value: string }) {
  return <span className={`badge ${statusTone(value)}`}>{value}</span>
}

function App() {
  const [page, setPage] = useState<PageMode>('user')
  const [busy, setBusy] = useState<BusyKey>('')
  const [error, setError] = useState('')
  const [notice, setNotice] = useState('')

  const [auth] = useState(() => currentUser())

  const [airports, setAirports] = useState<Airport[]>([])
  const [airlines, setAirlines] = useState<Airline[]>([])
  const [routes, setRoutes] = useState<RouteInfo[]>([])

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
  const [flightDetail, setFlightDetail] = useState<FlightDetail | null>(null)
  const [seats, setSeats] = useState<SeatMapItem[]>([])
  const [selectedSeat, setSelectedSeat] = useState('')
  const [manualSeat, setManualSeat] = useState('1A')

  const [passengerName, setPassengerName] = useState(auth?.fullName ?? 'Passenger')
  const [passengerPassport, setPassengerPassport] = useState('P1234567')
  const [holdResult, setHoldResult] = useState<HoldSeatResponse | null>(null)
  const [activeBooking, setActiveBooking] = useState<BookingDetail | null>(null)
  const [bookingStatus, setBookingStatus] = useState<BookingStatus | ''>('')
  const [bookings, setBookings] = useState<BookingDetail[]>([])
  const [payment, setPayment] = useState<PaymentResponse | null>(null)
  const [bookingPayments, setBookingPayments] = useState<PaymentResponse[]>([])
  const [notifications, setNotifications] = useState<NotificationItem[]>([])

  const [flightForm, setFlightForm] = useState<FlightCreateRequest>({
    flightNo: nextFlightNo(),
    fromAirportCode: 'HAN',
    toAirportCode: 'SGN',
    airlineCode: 'VN',
    departureTime: dateTimeInputValue(1, 9, 0),
    arrivalTime: dateTimeInputValue(1, 11, 15),
    totalSeats: 60,
    basePrice: 1400000,
    aircraftType: 'Airbus A321',
  })

  const seatRows = useMemo(() => groupSeats(seats), [seats])
  const availableSeats = useMemo(
    () => seats.filter((seat) => seat.status === 'AVAILABLE').length,
    [seats],
  )

  useEffect(() => {
    void refreshCatalog()
  }, [])

  async function run(action: () => Promise<void>, key: BusyKey) {
    setBusy(key)
    setError('')
    setNotice('')

    try {
      await action()
    } catch (caught) {
      setError(getErrorMessage(caught))
    } finally {
      setBusy('')
    }
  }

  async function refreshCatalog() {
    await run(async () => {
      const [airportData, airlineData, routeData] = await Promise.all([
        flightApi.getAirports(),
        flightApi.getAirlines(),
        flightApi.getRoutes(),
      ])
      setAirports(airportData)
      setAirlines(airlineData)
      setRoutes(routeData)
    }, 'catalog')
  }

  async function handleSearch(event?: FormEvent<HTMLFormElement>) {
    event?.preventDefault()

    await run(async () => {
      const data = await flightApi.searchFlights(search)
      setFlights(data)
      setFlightDetail(null)
      setSeats([])
      setSelectedSeat('')
      setHoldResult(null)
      setActiveBooking(null)
      setPayment(null)
      setBookingPayments([])
      if (data.length === 0) {
        setNotice('No flights matched this search.')
      }
    }, 'search')
  }

  async function loadFlight(flightId: number) {
    await run(async () => {
      const [detail, seatMap] = await Promise.all([
        flightApi.getFlight(flightId),
        flightApi.getSeatMap(flightId),
      ])
      setFlightDetail(detail)
      setSeats(seatMap)
      setSelectedSeat('')
      setHoldResult(null)
      setActiveBooking(null)
      setPayment(null)
      setBookingPayments([])
      if (seatMap.length > 0) {
        setManualSeat(seatMap.find((seat) => seat.status === 'AVAILABLE')?.seatNo ?? seatMap[0].seatNo)
      }
    }, 'flight')
  }

  async function holdSeat() {
    await run(async () => {
      if (!auth) {
        throw new Error('Đăng nhập bằng Keycloak trước khi đặt chỗ.')
      }
      if (!flightDetail) {
        throw new Error('Select a flight first.')
      }

      const seatNo = selectedSeat || manualSeat.trim().toUpperCase()
      if (!seatNo) {
        throw new Error('Select or enter a seat number.')
      }

      const held = await bookingApi.holdSeat({
        flightId: flightDetail.id,
        seatNo,
        passengerName: passengerName.trim(),
        passengerPassport: passengerPassport.trim() || undefined,
      })
      const booking = await bookingApi.getBooking(held.bookingId)

      setHoldResult(held)
      setActiveBooking(booking)
      setNotice(`Booking ${held.bookingCode} is held until ${formatDateTime(held.holdExpiresAt)}.`)
      await loadFlight(flightDetail.id)
      await loadBookings()
    }, 'hold')
  }

  async function createPayment() {
    await run(async () => {
      if (!auth) {
        throw new Error('Đăng nhập bằng Keycloak trước khi thanh toán.')
      }
      const booking = activeBooking
      const held = holdResult
      const bookingId = booking?.id ?? held?.bookingId
      const amount = booking?.totalAmount ?? held?.price

      if (!bookingId || !amount) {
        throw new Error('Hold a booking before creating payment.')
      }

      const paymentData = await paymentApi.createPayment({
        bookingId,
        amount,
        method: 'BANK_TRANSFER',
        idempotencyKey: `web-${bookingId}-${Date.now()}`,
      })
      const related = await paymentApi.getByBooking(bookingId)

      setPayment(paymentData)
      setBookingPayments(related)
      setNotice(`Payment ${paymentData.paymentCode} created.`)
    }, 'payment')
  }

  async function refreshPayment() {
    await run(async () => {
      if (!payment) {
        throw new Error('Create a payment first.')
      }

      const paymentData = await paymentApi.getPayment(payment.id)
      setPayment(paymentData)
      setNotice(`Payment status: ${paymentData.status}`)
    }, 'payment')
  }

  async function loadBookings() {
    await run(async () => {
      if (!auth) throw new Error('Đăng nhập bằng Keycloak để xem booking.')
      const data = await bookingApi.getMyBookings(bookingStatus)
      setBookings(pageItems(data))
    }, 'bookings')
  }

  async function cancelBooking(bookingId: number) {
    await run(async () => {
      if (!auth) throw new Error('Đăng nhập bằng Keycloak để hủy booking.')
      const booking = await bookingApi.cancelBooking(bookingId)
      setActiveBooking(booking)
      setNotice(`Booking ${booking.bookingCode} is ${booking.status}.`)
      await loadBookings()
    }, 'bookings')
  }

  async function loadNotifications() {
    await run(async () => {
      if (!auth) throw new Error('Đăng nhập bằng Keycloak để xem thông báo.')
      const data = await notificationApi.mine()
      setNotifications(pageItems(data))
    }, 'notifications')
  }

  async function createFlight(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    await run(async () => {
      const flight = await flightApi.createFlight(flightForm)
      setNotice(`Created flight ${flight.flightNo} with ${flight.totalSeats} seats.`)
      setSearch((current) => ({
        ...current,
        from: flight.fromAirport,
        to: flight.toAirport,
        date: flight.departureTime.slice(0, 10),
      }))
      setFlightForm((current) => ({
        ...current,
        flightNo: nextFlightNo(),
      }))
      await handleSearch()
    }, 'admin')
  }

  return (
    <main className="site-shell">
      <header className="site-header">
        <button type="button" className="brand-mark" onClick={() => setPage('user')}>
          <span>AS</span>
          <strong>AeroSky</strong>
        </button>

        <nav className="page-nav" aria-label="Trang chính">
          <button
            type="button"
            className={page === 'user' ? 'active' : ''}
            onClick={() => setPage('user')}
          >
            Người dùng
          </button>
          {auth?.roles.includes('ADMIN') && (
            <button
              type="button"
              className={page === 'admin' ? 'active' : ''}
              onClick={() => setPage('admin')}
            >
              Admin
            </button>
          )}
        </nav>

        <div className="header-actions">
          <button type="button" className="text-button" onClick={() => void refreshCatalog()}>
            Làm mới dữ liệu
          </button>
          <div className="user-chip">
            <span>{auth ? auth.fullName : 'Khách'}</span>
            <small>{auth ? auth.email : 'Đăng nhập bằng Keycloak để đặt vé'}</small>
          </div>
        </div>
      </header>

      {(error || notice) && (
        <div className={`message ${error ? 'error' : 'notice'}`}>
          <strong>{error ? 'Không thể xử lý' : 'Cập nhật'}</strong>
          <span>{error || notice}</span>
        </div>
      )}

      {page === 'user' && (
        <>
          <section className="booking-hero">
            <img src={heroImage} alt="" aria-hidden="true" />
            <div className="hero-overlay"></div>
            <div className="hero-content">
              <div className="hero-copy">
                <p className="eyebrow light">Chuyến bay nội địa và quốc tế</p>
                <h1>Đặt vé máy bay rõ ràng, nhanh và chắc chỗ.</h1>
                <p>
                  Tìm chuyến bay, chọn ghế, giữ chỗ và thanh toán QR trong cùng một hành trình.
                </p>
                <div className="hero-stats">
                  <div>
                    <strong>{airports.length || 4}</strong>
                    <span>Sân bay</span>
                  </div>
                  <div>
                    <strong>{airlines.length || 3}</strong>
                    <span>Hãng bay</span>
                  </div>
                  <div>
                    <strong>{routes.length || 6}</strong>
                    <span>Tuyến bay</span>
                  </div>
                </div>
              </div>

              <form className="booking-card" onSubmit={(event) => void handleSearch(event)}>
                <div className="booking-card-head">
                  <div>
                    <p className="eyebrow">Tìm vé</p>
                    <h2>Hành trình của bạn</h2>
                  </div>
                  <div className="trip-pills" aria-label="Loại hành trình">
                    <span className="active">Một chiều</span>
                    <span>Khứ hồi</span>
                  </div>
                </div>

                <div className="search-grid">
                  <label>
                    Điểm đi
                    <select
                      value={search.from}
                      onChange={(event) => setSearch({ ...search, from: event.target.value })}
                    >
                      {airports.map((airport) => (
                        <option key={airport.iataCode} value={airport.iataCode}>
                          {airport.iataCode} - {airport.city}
                        </option>
                      ))}
                      {airports.length === 0 && <option value="HAN">HAN - Hà Nội</option>}
                    </select>
                  </label>
                  <label>
                    Điểm đến
                    <select
                      value={search.to}
                      onChange={(event) => setSearch({ ...search, to: event.target.value })}
                    >
                      {airports.map((airport) => (
                        <option key={airport.iataCode} value={airport.iataCode}>
                          {airport.iataCode} - {airport.city}
                        </option>
                      ))}
                      {airports.length === 0 && <option value="SGN">SGN - TP.HCM</option>}
                    </select>
                  </label>
                  <label>
                    Ngày bay
                    <input
                      type="date"
                      value={search.date}
                      onChange={(event) => setSearch({ ...search, date: event.target.value })}
                    />
                  </label>
                  <label>
                    Hành khách
                    <input
                      min="1"
                      type="number"
                      value={search.passengers}
                      onChange={(event) =>
                        setSearch({ ...search, passengers: Number(event.target.value || 1) })
                      }
                    />
                  </label>
                  <label>
                    Hãng bay
                    <select
                      value={search.airline}
                      onChange={(event) => setSearch({ ...search, airline: event.target.value })}
                    >
                      <option value="">Tất cả</option>
                      {airlines.map((airline) => (
                        <option key={airline.code} value={airline.code}>
                          {airline.code} - {airline.name}
                        </option>
                      ))}
                    </select>
                  </label>
                  <label>
                    Trạng thái
                    <select
                      value={search.status}
                      onChange={(event) =>
                        setSearch({ ...search, status: event.target.value as FlightStatus | '' })
                      }
                    >
                      <option value="">Tất cả</option>
                      <option value="SCHEDULED">Đúng lịch</option>
                      <option value="DELAYED">Trễ chuyến</option>
                      <option value="CANCELLED">Đã hủy</option>
                    </select>
                  </label>
                </div>

                <div className="booking-card-actions">
                  <button type="submit" className="primary-button large" disabled={busy === 'search'}>
                    {busy === 'search' ? 'Đang tìm...' : 'Tìm chuyến bay'}
                  </button>
                  <span>Ngày đang chọn: {search.date}</span>
                </div>
              </form>
            </div>
          </section>

          <section className="content-shell">
            <section className="account-panel">
              <div>
                <p className="eyebrow">Tài khoản</p>
                <h3>{auth ? `Xin chào, ${auth.fullName}` : 'Đăng nhập bằng Keycloak'}</h3>
              </div>

              {!auth && (
                <div className="auth-form">
                  <p>Mật khẩu và phiên đăng nhập được quản lý tập trung bởi Keycloak.</p>
                  <button type="button" className="primary-button" onClick={() => void keycloakLogin()}>
                    Đăng nhập với Keycloak
                  </button>
                  <button type="button" className="ghost-button" onClick={() => void keycloakRegister()}>
                    Tạo tài khoản Keycloak
                  </button>
                </div>
              )}

              <div className="identity-box">
                {auth ? (
                  <>
                    <strong>{auth.fullName}</strong>
                    <span>{auth.email}</span>
                    <div className="identity-meta">
                      {auth.roles.map((role) => <Badge key={role} value={role} />)}
                    </div>
                    <button type="button" className="ghost-button" onClick={() => void keycloakLogout()}>
                      Đăng xuất
                    </button>
                  </>
                ) : (
                  <>
                    <strong>Chưa đăng nhập</strong>
                    <span>Bạn vẫn có thể tìm chuyến bay; đăng nhập để đặt chỗ và thanh toán.</span>
                  </>
                )}
              </div>
            </section>

            <section className="booking-layout">
              <div className="main-column">
                <section className="section-head">
                  <div>
                    <p className="eyebrow">Kết quả tìm kiếm</p>
                    <h2>Chuyến bay phù hợp</h2>
                  </div>
                  <div className="sort-controls">
                    <select
                      value={search.sort}
                      onChange={(event) =>
                        setSearch({
                          ...search,
                          sort: event.target.value as FlightSearchParams['sort'],
                        })
                      }
                    >
                      <option value="departureTime">Giờ khởi hành</option>
                      <option value="arrivalTime">Giờ đến</option>
                      <option value="price">Giá vé</option>
                    </select>
                    <select
                      value={search.order}
                      onChange={(event) =>
                        setSearch({
                          ...search,
                          order: event.target.value as FlightSearchParams['order'],
                        })
                      }
                    >
                      <option value="asc">Tăng dần</option>
                      <option value="desc">Giảm dần</option>
                    </select>
                  </div>
                </section>

                <div className="flight-list">
                  {flights.map((flight) => (
                    <article className="flight-row" key={flight.id}>
                      <div className="flight-route">
                        <span>{flight.fromAirport}</span>
                        <strong>{flight.flightNo}</strong>
                        <span>{flight.toAirport}</span>
                      </div>
                      <div>
                        <strong>{flight.airlineName}</strong>
                        <p>
                          {formatDateTime(flight.departureTime)} - {formatDateTime(flight.arrivalTime)}
                        </p>
                      </div>
                      <div>
                        <strong>{formatMoney(flight.currentPrice)}</strong>
                        <p>
                          {flight.availableSeats} ghế còn ·{' '}
                          {durationLabel(flight.departureTime, flight.arrivalTime)}
                        </p>
                      </div>
                      <Badge value={flight.status} />
                      <button type="button" className="ghost-button" onClick={() => void loadFlight(flight.id)}>
                        Chọn
                      </button>
                    </article>
                  ))}
                  {flights.length === 0 && (
                    <div className="empty-state">
                      <strong>Chưa có chuyến bay nào.</strong>
                      <span>Chọn hành trình rồi bấm Tìm chuyến bay để xem giá và ghế.</span>
                    </div>
                  )}
                </div>
              </div>

              <aside className="booking-sidebar">
                <section className="panel">
                  <div className="panel-heading">
                    <div>
                      <p className="eyebrow">Chọn ghế</p>
                      <h3>{flightDetail ? flightDetail.flightNo : 'Chưa chọn chuyến'}</h3>
                    </div>
                    {flightDetail && <Badge value={flightDetail.status} />}
                  </div>

                  {flightDetail ? (
                    <>
                      <div className="stats-grid">
                        <div>
                          <span>Còn trống</span>
                          <strong>{availableSeats || flightDetail.availableSeats}</strong>
                        </div>
                        <div>
                          <span>Đang giữ</span>
                          <strong>{flightDetail.heldSeats}</strong>
                        </div>
                        <div>
                          <span>Đã đặt</span>
                          <strong>{flightDetail.bookedSeats}</strong>
                        </div>
                      </div>

                      {seatRows.length > 0 ? (
                        <div className="seat-map" aria-label="Sơ đồ ghế">
                          {seatRows.map((row) => (
                            <div className="seat-row" key={row.row}>
                              <span>{row.row}</span>
                              {row.seats.map((seat) => (
                                <button
                                  type="button"
                                  className={`seat ${seat.seatClass.toLowerCase()} ${seat.status.toLowerCase()} ${
                                    selectedSeat === seat.seatNo ? 'selected' : ''
                                  }`}
                                  key={seat.seatNo}
                                  disabled={seat.status !== 'AVAILABLE'}
                                  onClick={() => {
                                    setSelectedSeat(seat.seatNo)
                                    setManualSeat(seat.seatNo)
                                  }}
                                  title={`${seat.seatNo} ${seat.seatClass} ${seat.status}`}
                                >
                                  {seat.seatNo}
                                </button>
                              ))}
                            </div>
                          ))}
                        </div>
                      ) : (
                        <div className="empty-state compact">
                          <strong>Chuyến này chưa có sơ đồ ghế.</strong>
                          <span>Nhập ghế thủ công để giữ chỗ.</span>
                        </div>
                      )}
                    </>
                  ) : (
                    <div className="empty-state compact">
                      <strong>Chọn một chuyến bay trước.</strong>
                      <span>Sơ đồ ghế và giá cuối sẽ hiển thị tại đây.</span>
                    </div>
                  )}
                </section>

                <section className="panel">
                  <div className="panel-heading">
                    <div>
                      <p className="eyebrow">Thông tin khách</p>
                      <h3>Giữ chỗ</h3>
                    </div>
                    {holdResult && <Badge value="HELD" />}
                  </div>

                  <div className="stack-form">
                    <label>
                      Ghế
                      <input
                        value={selectedSeat || manualSeat}
                        onChange={(event) => {
                          setSelectedSeat('')
                          setManualSeat(event.target.value.toUpperCase())
                        }}
                        placeholder="1A"
                      />
                    </label>
                    <label>
                      Họ tên hành khách
                      <input
                        value={passengerName}
                        onChange={(event) => setPassengerName(event.target.value)}
                      />
                    </label>
                    <label>
                      Hộ chiếu/CCCD
                      <input
                        value={passengerPassport}
                        onChange={(event) => setPassengerPassport(event.target.value)}
                      />
                    </label>
                    <button type="button" className="primary-button" onClick={() => void holdSeat()}>
                      {busy === 'hold' ? 'Đang giữ chỗ' : 'Giữ chỗ'}
                    </button>
                  </div>

                  {holdResult && (
                    <div className="result-box">
                      <div>
                        <span>Mã đặt chỗ</span>
                        <strong>{holdResult.bookingCode}</strong>
                      </div>
                      <div>
                        <span>Giá vé</span>
                        <strong>{formatMoney(holdResult.price)}</strong>
                      </div>
                    </div>
                  )}
                </section>

                <section className="panel">
                  <div className="panel-heading">
                    <div>
                      <p className="eyebrow">Thanh toán</p>
                      <h3>QR chuyển khoản</h3>
                    </div>
                    {payment && <Badge value={payment.status} />}
                  </div>

                  <div className="button-row">
                    <button type="button" className="primary-button" onClick={() => void createPayment()}>
                      {busy === 'payment' ? 'Đang tạo' : 'Tạo QR'}
                    </button>
                    <button type="button" className="ghost-button" onClick={() => void refreshPayment()}>
                      Cập nhật
                    </button>
                  </div>

                  {payment ? (
                    <div className="payment-box">
                      {payment.qrUrl ? (
                        <img src={payment.qrUrl} alt={`Payment QR ${payment.paymentCode}`} />
                      ) : (
                        <div className="qr-placeholder">QR</div>
                      )}
                      <div>
                        <span>{payment.paymentCode}</span>
                        <strong>{formatMoney(payment.amount)}</strong>
                        <p>Nội dung: {payment.transferCode ?? 'N/A'}</p>
                        <p>Hạn: {formatDateTime(payment.expiresAt)}</p>
                      </div>
                    </div>
                  ) : (
                    <div className="empty-state compact">
                      <strong>Chưa có thanh toán.</strong>
                      <span>Giữ chỗ trước khi tạo mã QR.</span>
                    </div>
                  )}

                  {bookingPayments.length > 0 && (
                    <div className="mini-list payment-history">
                      {bookingPayments.map((item) => (
                        <div key={item.id}>
                          <span>{item.paymentCode}</span>
                          <Badge value={item.status} />
                        </div>
                      ))}
                    </div>
                  )}
                </section>
              </aside>
            </section>

            <section className="history-grid">
              <div className="panel">
                <div className="panel-heading">
                  <div>
                    <p className="eyebrow">Chuyến đi của tôi</p>
                    <h3>Lịch sử đặt chỗ</h3>
                  </div>
                  <div className="inline-controls">
                    <select
                      value={bookingStatus}
                      onChange={(event) => setBookingStatus(event.target.value as BookingStatus | '')}
                    >
                      <option value="">Tất cả</option>
                      <option value="HELD">Đang giữ</option>
                      <option value="CONFIRMED">Đã xác nhận</option>
                      <option value="CANCELLED">Đã hủy</option>
                      <option value="EXPIRED">Hết hạn</option>
                    </select>
                    <button type="button" className="ghost-button" onClick={() => void loadBookings()}>
                      Tải lịch sử
                    </button>
                  </div>
                </div>

                <div className="booking-list">
                  {bookings.map((booking) => (
                    <article className="booking-row" key={booking.id}>
                      <div>
                        <strong>{booking.bookingCode}</strong>
                        <p>
                          Chuyến {booking.flightId} · {booking.items.map((item) => item.seatNo).join(', ')}
                        </p>
                      </div>
                      <div>
                        <strong>{formatMoney(booking.totalAmount)}</strong>
                        <p>{formatDateTime(booking.createdAt)}</p>
                      </div>
                      <Badge value={booking.status} />
                      {booking.status === 'HELD' && (
                        <button
                          type="button"
                          className="danger-button"
                          onClick={() => void cancelBooking(booking.id)}
                        >
                          Hủy
                        </button>
                      )}
                    </article>
                  ))}
                  {bookings.length === 0 && (
                    <div className="empty-state">
                      <strong>Chưa có booking nào được tải.</strong>
                      <span>Bấm Tải lịch sử hoặc giữ chỗ một chuyến bay.</span>
                    </div>
                  )}
                </div>
              </div>

              <div className="panel">
                <div className="panel-heading">
                  <div>
                    <p className="eyebrow">Thông báo</p>
                    <h3>Trạng thái gửi</h3>
                  </div>
                  <button type="button" className="ghost-button" onClick={() => void loadNotifications()}>
                    Tải
                  </button>
                </div>

                <div className="mini-list">
                  {notifications.map((item) => (
                    <div key={item.id}>
                      <span>
                        {item.templateCode} · {item.channel}
                      </span>
                      <Badge value={item.status} />
                    </div>
                  ))}
                  {notifications.length === 0 && (
                    <div className="empty-state compact">
                      <strong>Chưa có thông báo.</strong>
                      <span>Thông báo sẽ xuất hiện khi booking đổi trạng thái.</span>
                    </div>
                  )}
                </div>
              </div>
            </section>
          </section>
          </>
        )}

        {page === 'admin' && (
          <section className="admin-page">
            <div className="admin-hero">
              <div>
                <p className="eyebrow">AeroSky Admin</p>
                <h1>Quản trị lịch bay và tồn ghế</h1>
                <p>Khởi tạo chuyến bay mới, theo dõi catalog sân bay, hãng bay và tuyến bay.</p>
              </div>
              <button type="button" className="primary-button" onClick={() => void refreshCatalog()}>
                Làm mới catalog
              </button>
            </div>

            <div className="admin-stats">
              <div>
                <span>Sân bay</span>
                <strong>{airports.length}</strong>
              </div>
              <div>
                <span>Hãng bay</span>
                <strong>{airlines.length}</strong>
              </div>
              <div>
                <span>Tuyến bay</span>
                <strong>{routes.length}</strong>
              </div>
            </div>

            <section className="panel">
              <div className="panel-heading">
                <div>
                  <p className="eyebrow">Tạo chuyến bay</p>
                  <h3>Thông tin khai thác</h3>
                </div>
              </div>

              <form className="admin-grid" onSubmit={(event) => void createFlight(event)}>
                <label>
                  Số hiệu
                  <input
                    value={flightForm.flightNo}
                    onChange={(event) => setFlightForm({ ...flightForm, flightNo: event.target.value })}
                  />
                </label>
                <label>
                  Hãng bay
                  <select
                    value={flightForm.airlineCode}
                    onChange={(event) =>
                      setFlightForm({ ...flightForm, airlineCode: event.target.value })
                    }
                  >
                    {airlines.map((airline) => (
                      <option key={airline.code} value={airline.code}>
                        {airline.code} - {airline.name}
                      </option>
                    ))}
                    {airlines.length === 0 && <option value="VN">VN</option>}
                  </select>
                </label>
                <label>
                  Từ
                  <select
                    value={flightForm.fromAirportCode}
                    onChange={(event) =>
                      setFlightForm({ ...flightForm, fromAirportCode: event.target.value })
                    }
                  >
                    {airports.map((airport) => (
                      <option key={airport.iataCode} value={airport.iataCode}>
                        {airport.iataCode}
                      </option>
                    ))}
                    {airports.length === 0 && <option value="HAN">HAN</option>}
                  </select>
                </label>
                <label>
                  Đến
                  <select
                    value={flightForm.toAirportCode}
                    onChange={(event) =>
                      setFlightForm({ ...flightForm, toAirportCode: event.target.value })
                    }
                  >
                    {airports.map((airport) => (
                      <option key={airport.iataCode} value={airport.iataCode}>
                        {airport.iataCode}
                      </option>
                    ))}
                    {airports.length === 0 && <option value="SGN">SGN</option>}
                  </select>
                </label>
                <label>
                  Giờ đi
                  <input
                    type="datetime-local"
                    value={flightForm.departureTime}
                    onChange={(event) =>
                      setFlightForm({ ...flightForm, departureTime: event.target.value })
                    }
                  />
                </label>
                <label>
                  Giờ đến
                  <input
                    type="datetime-local"
                    value={flightForm.arrivalTime}
                    onChange={(event) =>
                      setFlightForm({ ...flightForm, arrivalTime: event.target.value })
                    }
                  />
                </label>
                <label>
                  Số ghế
                  <input
                    min="6"
                    step="6"
                    type="number"
                    value={flightForm.totalSeats}
                    onChange={(event) =>
                      setFlightForm({ ...flightForm, totalSeats: Number(event.target.value || 60) })
                    }
                  />
                </label>
                <label>
                  Giá cơ bản
                  <input
                    min="1000"
                    type="number"
                    value={flightForm.basePrice}
                    onChange={(event) =>
                      setFlightForm({ ...flightForm, basePrice: Number(event.target.value || 0) })
                    }
                  />
                </label>
                <label>
                  Tàu bay
                  <input
                    value={flightForm.aircraftType}
                    onChange={(event) =>
                      setFlightForm({ ...flightForm, aircraftType: event.target.value })
                    }
                  />
                </label>
                <button type="submit" className="primary-button">
                  {busy === 'admin' ? 'Đang tạo' : 'Tạo chuyến bay'}
                </button>
              </form>
            </section>
          </section>
        )}
    </main>
  )
}

export default App
