import React, { useState } from 'react'
import type { BookingDetail } from '../features/bookings/types'
import type {
  Airline,
  Airport,
  FlightCreateRequest,
  FlightStatus,
  FlightStatusUpdateRequest,
  FlightSummary,
  RouteInfo,
} from '../features/flights/types'

interface AdminDashboardProps {
  flights: FlightSummary[]
  airports: Airport[]
  airlines: Airline[]
  routes: RouteInfo[]
  bookings: BookingDetail[]
  onCreateFlight: (req: FlightCreateRequest) => void
  onDeleteFlight: (id: number) => void
  onUpdateStatus: (id: number, req: FlightStatusUpdateRequest) => void
  onCreateAirport: (data: { iataCode: string; name: string; city: string; country: string }) => void
  onCreateAirline: (data: { code: string; name: string }) => void
  onCreateRoute: (data: { fromAirport: string; toAirport: string; distanceKm: number }) => void
  onSeatAction: (flightId: number, seatNo: string, action: 'hold' | 'book' | 'release') => void
  formatMoney: (val: number | string | null | undefined) => string
  formatDateTime: (val: string | null | undefined) => string
}

export const AdminDashboard: React.FC<AdminDashboardProps> = ({
  flights,
  airports,
  routes,
  bookings,
  onCreateFlight,
  onDeleteFlight,
  onUpdateStatus,
  onCreateAirport,
  onCreateAirline,
  onCreateRoute,
  onSeatAction,
  formatMoney,
  formatDateTime,
}) => {
  const [subTab, setSubTab] = useState<'overview' | 'flights' | 'bookings' | 'resources'>('overview')

  // Create Flight Form Modal State
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false)
  const [flightNo, setFlightNo] = useState(`VN${Math.floor(300 + Math.random() * 600)}`)
  const [fromAirportCode, setFromAirportCode] = useState('HAN')
  const [toAirportCode, setToAirportCode] = useState('SGN')
  const [airlineCode, setAirlineCode] = useState('VN')
  const [departureTime, setDepartureTime] = useState('')
  const [arrivalTime, setArrivalTime] = useState('')
  const [totalSeats, setTotalSeats] = useState(180)
  const [basePrice, setBasePrice] = useState(1500000)
  const [aircraftType] = useState('Airbus A321')
  const [flightError, setFlightError] = useState('')

  // Update Status Modal State
  const [statusFlight, setStatusFlight] = useState<FlightSummary | null>(null)
  const [newStatus, setNewStatus] = useState<FlightStatus>('SCHEDULED')
  const [newDeparture, setNewDeparture] = useState('')
  const [newArrival, setNewArrival] = useState('')

  // Seat Action Modal State
  const [seatFlightId, setSeatFlightId] = useState<number>(flights[0]?.id || 1)
  const [seatNo, setSeatNo] = useState('12A')

  // Resource Form States
  const [newIataCode, setNewIataCode] = useState('')
  const [newAirportName, setNewAirportName] = useState('')
  const [newCity, setNewCity] = useState('')
  const [newCountry] = useState('Vietnam')

  const [newAirlineCode, setNewAirlineCode] = useState('')
  const [newAirlineName, setNewAirlineName] = useState('')

  const [routeFrom, setRouteFrom] = useState('HAN')
  const [routeTo, setRouteTo] = useState('SGN')
  const [routeDistance, setRouteDistance] = useState(1160)
  const [routeError, setRouteError] = useState('')

  const totalRevenue = bookings.reduce((sum, b) => sum + (b.totalAmount || 0), 0)

  const handleCreateFlightSubmit = () => {
    if (fromAirportCode === toAirportCode) {
      setFlightError('Sân bay đi và sân bay đến không được trùng nhau!')
      return
    }
    setFlightError('')
    onCreateFlight({
      flightNo,
      fromAirportCode,
      toAirportCode,
      airlineCode,
      departureTime,
      arrivalTime,
      totalSeats,
      basePrice,
      aircraftType,
    })
    setIsCreateModalOpen(false)
  }

  const handleCreateRouteSubmit = () => {
    if (routeFrom === routeTo) {
      setRouteError('Sân bay đi và sân bay đến không được trùng nhau!')
      return
    }
    setRouteError('')
    onCreateRoute({
      fromAirport: routeFrom,
      toAirport: routeTo,
      distanceKm: routeDistance,
    })
  }

  return (
    <div className="max-w-7xl mx-auto px-4 py-8 animate-fade-in space-y-8">
      {/* Admin Header */}
      <div className="flex flex-wrap items-center justify-between gap-4 bg-slate-900 text-white p-6 rounded-3xl shadow-xl">
        <div className="flex items-center gap-3">
          <div className="w-12 h-12 rounded-2xl bg-sky-500 text-white flex items-center justify-center font-bold text-xl shadow-lg">
            <span className="material-symbols-outlined text-2xl">admin_panel_settings</span>
          </div>
          <div>
            <h2 className="text-2xl font-black tracking-tight">Horizon Air Admin Workspace</h2>
            <p className="text-xs text-sky-300 font-bold">
              Bảng điều khiển & Quản trị hệ thống chuyến bay
            </p>
          </div>
        </div>

        {/* Sub-Tabs Selector */}
        <div className="flex items-center gap-1 bg-white/10 p-1.5 rounded-2xl backdrop-blur-md border border-white/10 text-xs font-bold">
          <button
            onClick={() => setSubTab('overview')}
            className={`px-4 py-2 rounded-xl transition-all ${
              subTab === 'overview' ? 'bg-white text-slate-900 shadow-sm' : 'text-slate-300 hover:text-white'
            }`}
          >
            Tổng quan
          </button>
          <button
            onClick={() => setSubTab('flights')}
            className={`px-4 py-2 rounded-xl transition-all ${
              subTab === 'flights' ? 'bg-white text-slate-900 shadow-sm' : 'text-slate-300 hover:text-white'
            }`}
          >
            Quản lý chuyến bay
          </button>
          <button
            onClick={() => setSubTab('bookings')}
            className={`px-4 py-2 rounded-xl transition-all ${
              subTab === 'bookings' ? 'bg-white text-slate-900 shadow-sm' : 'text-slate-300 hover:text-white'
            }`}
          >
            Quản lý đặt chỗ
          </button>
          <button
            onClick={() => setSubTab('resources')}
            className={`px-4 py-2 rounded-xl transition-all ${
              subTab === 'resources' ? 'bg-white text-slate-900 shadow-sm' : 'text-slate-300 hover:text-white'
            }`}
          >
            Sân bay / Hãng bay / Đường bay
          </button>
        </div>
      </div>

      {/* Sub-Tab 1: OVERVIEW DASHBOARD */}
      {subTab === 'overview' && (
        <div className="space-y-6">
          {/* Key Metrics Cards */}
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4">
            <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-sm space-y-1">
              <span className="text-xs font-bold text-slate-400 uppercase">Tổng số chuyến bay</span>
              <div className="text-3xl font-black text-slate-900">{flights.length}</div>
              <span className="text-[11px] text-emerald-600 font-semibold">Đang vận hành</span>
            </div>

            <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-sm space-y-1">
              <span className="text-xs font-bold text-slate-400 uppercase">Đơn đặt chỗ (Bookings)</span>
              <div className="text-3xl font-black text-sky-600">{bookings.length}</div>
              <span className="text-[11px] text-sky-600 font-semibold">Tổng giao dịch</span>
            </div>

            <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-sm space-y-1">
              <span className="text-xs font-bold text-slate-400 uppercase">Doanh thu ước tính</span>
              <div className="text-2xl font-black text-orange-600">{formatMoney(totalRevenue)}</div>
              <span className="text-[11px] text-orange-600 font-semibold">Cập nhật thời gian thực</span>
            </div>

            <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-sm space-y-1">
              <span className="text-xs font-bold text-slate-400 uppercase">Đường bay (Routes)</span>
              <div className="text-3xl font-black text-slate-900">{routes.length}</div>
              <span className="text-[11px] text-slate-500 font-semibold">Sân bay: {airports.length}</span>
            </div>
          </div>

          {/* Quick Actions & Recent Departures */}
          <div className="bg-white rounded-3xl p-6 border border-slate-200 shadow-sm space-y-4">
            <h3 className="text-lg font-bold text-slate-900">Danh sách chuyến bay gần đây</h3>
            <div className="overflow-x-auto">
              <table className="w-full text-left text-xs border-collapse">
                <thead>
                  <tr className="border-b border-slate-200 text-slate-400 uppercase font-bold">
                    <th className="py-3 px-2">Mã chuyến</th>
                    <th className="py-3 px-2">Hãng bay</th>
                    <th className="py-3 px-2">Hành trình</th>
                    <th className="py-3 px-2">Giờ đi</th>
                    <th className="py-3 px-2">Giá vé</th>
                    <th className="py-3 px-2">Trạng thái</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100 font-medium">
                  {flights.slice(0, 5).map((f) => (
                    <tr key={f.id} className="hover:bg-slate-50">
                      <td className="py-3 px-2 font-bold text-slate-900">{f.flightNo}</td>
                      <td className="py-3 px-2">{f.airlineName} ({f.airlineCode})</td>
                      <td className="py-3 px-2 font-bold">{f.fromAirport} ➔ {f.toAirport}</td>
                      <td className="py-3 px-2">{formatDateTime(f.departureTime)}</td>
                      <td className="py-3 px-2 text-orange-600 font-bold">{formatMoney(f.currentPrice)}</td>
                      <td className="py-3 px-2">
                        <span className="badge badge-info">{f.status}</span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}

      {/* Sub-Tab 2: FLIGHT MANAGEMENT */}
      {subTab === 'flights' && (
        <div className="space-y-6">
          <div className="flex justify-between items-center">
            <h3 className="text-xl font-extrabold text-slate-900">Quản lý danh sách chuyến bay</h3>
            <button
              onClick={() => setIsCreateModalOpen(true)}
              className="px-5 py-2.5 bg-sky-600 hover:bg-sky-700 text-white rounded-xl font-bold text-xs shadow-md transition-all flex items-center gap-1.5"
            >
              <span className="material-symbols-outlined text-base">add</span>
              Tạo chuyến bay mới
            </button>
          </div>

          {/* Flights Table */}
          <div className="bg-white rounded-3xl p-6 border border-slate-200 shadow-sm overflow-x-auto">
            <table className="w-full text-left text-xs border-collapse">
              <thead>
                <tr className="border-b border-slate-200 text-slate-400 uppercase font-bold">
                  <th className="py-3 px-3">ID</th>
                  <th className="py-3 px-3">Mã chuyến</th>
                  <th className="py-3 px-3">Hãng</th>
                  <th className="py-3 px-3">Tuyến bay</th>
                  <th className="py-3 px-3">Khởi hành</th>
                  <th className="py-3 px-3">Đến</th>
                  <th className="py-3 px-3">Giá vé</th>
                  <th className="py-3 px-3">Ghế trống</th>
                  <th className="py-3 px-3">Trạng thái</th>
                  <th className="py-3 px-3 text-right">Thao tác</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 font-medium">
                {flights.map((f) => (
                  <tr key={f.id} className="hover:bg-slate-50">
                    <td className="py-3 px-3 text-slate-400">{f.id}</td>
                    <td className="py-3 px-3 font-bold text-slate-900">{f.flightNo}</td>
                    <td className="py-3 px-3">{f.airlineCode}</td>
                    <td className="py-3 px-3 font-bold">{f.fromAirport} ➔ {f.toAirport}</td>
                    <td className="py-3 px-3">{formatDateTime(f.departureTime)}</td>
                    <td className="py-3 px-3">{formatDateTime(f.arrivalTime)}</td>
                    <td className="py-3 px-3 text-orange-600 font-bold">{formatMoney(f.currentPrice)}</td>
                    <td className="py-3 px-3 font-bold text-emerald-600">{f.availableSeats}</td>
                    <td className="py-3 px-3">
                      <span className="badge badge-info">{f.status}</span>
                    </td>
                    <td className="py-3 px-3 text-right space-x-2">
                      <button
                        onClick={() => {
                          setStatusFlight(f)
                          setNewStatus(f.status)
                          setNewDeparture(f.departureTime.slice(0, 16))
                          setNewArrival(f.arrivalTime.slice(0, 16))
                        }}
                        className="px-2.5 py-1 bg-slate-100 hover:bg-slate-200 text-slate-800 rounded-lg text-[11px] font-bold"
                      >
                        Đổi trạng thái
                      </button>
                      <button
                        onClick={() => onDeleteFlight(f.id)}
                        className="px-2.5 py-1 bg-red-50 hover:bg-red-100 text-red-600 rounded-lg text-[11px] font-bold"
                      >
                        Xóa
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {/* Seat Override Box */}
          <div className="bg-white rounded-3xl p-6 border border-slate-200 shadow-sm space-y-4">
            <h4 className="font-bold text-slate-900 text-sm">Quản trị trạng thái ghế nội bộ</h4>
            <div className="flex flex-wrap gap-3 items-center text-xs">
              <label className="font-bold text-slate-600">ID Chuyến bay:</label>
              <input
                type="number"
                value={seatFlightId}
                onChange={(e) => setSeatFlightId(Number(e.target.value))}
                className="w-24 bg-slate-50 border border-slate-200 rounded-xl p-2 font-bold"
              />

              <label className="font-bold text-slate-600 ml-2">Mã ghế:</label>
              <input
                type="text"
                value={seatNo}
                onChange={(e) => setSeatNo(e.target.value.toUpperCase())}
                className="w-24 bg-slate-50 border border-slate-200 rounded-xl p-2 font-bold uppercase"
              />

              <button
                onClick={() => onSeatAction(seatFlightId, seatNo, 'hold')}
                className="px-3 py-2 bg-amber-500 hover:bg-amber-600 text-white rounded-xl font-bold"
              >
                Giữ ghế (Hold)
              </button>
              <button
                onClick={() => onSeatAction(seatFlightId, seatNo, 'book')}
                className="px-3 py-2 bg-sky-600 hover:bg-sky-700 text-white rounded-xl font-bold"
              >
                Đặt ghế (Book)
              </button>
              <button
                onClick={() => onSeatAction(seatFlightId, seatNo, 'release')}
                className="px-3 py-2 bg-slate-200 hover:bg-slate-300 text-slate-800 rounded-xl font-bold"
              >
                Nhả ghế (Release)
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Sub-Tab 3: BOOKING MANAGEMENT */}
      {subTab === 'bookings' && (
        <div className="bg-white rounded-3xl p-6 border border-slate-200 shadow-sm space-y-4">
          <h3 className="text-xl font-extrabold text-slate-900">Quản lý tất cả đơn đặt chỗ</h3>
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs border-collapse">
              <thead>
                <tr className="border-b border-slate-200 text-slate-400 uppercase font-bold">
                  <th className="py-3 px-3">ID</th>
                  <th className="py-3 px-3">Mã đơn đặt (Booking Code)</th>
                  <th className="py-3 px-3">Flight ID</th>
                  <th className="py-3 px-3">Hành khách</th>
                  <th className="py-3 px-3">Ghế</th>
                  <th className="py-3 px-3">Tổng tiền</th>
                  <th className="py-3 px-3">Trạng thái</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 font-medium">
                {bookings.map((b) => (
                  <tr key={b.id} className="hover:bg-slate-50">
                    <td className="py-3 px-3 text-slate-400">{b.id}</td>
                    <td className="py-3 px-3 font-bold text-slate-900">{b.bookingCode}</td>
                    <td className="py-3 px-3 font-bold">{b.flightId}</td>
                    <td className="py-3 px-3">{b.items?.[0]?.passengerName || 'N/A'}</td>
                    <td className="py-3 px-3 font-bold text-sky-600">{b.items?.[0]?.seatNo || 'N/A'}</td>
                    <td className="py-3 px-3 text-orange-600 font-bold">{formatMoney(b.totalAmount)}</td>
                    <td className="py-3 px-3">
                      <span className="badge badge-info">{b.status}</span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Sub-Tab 4: AIRPORTS, AIRLINES & ROUTES */}
      {subTab === 'resources' && (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {/* Airports Form */}
          <div className="bg-white rounded-3xl p-6 border border-slate-200 shadow-sm space-y-4">
            <h4 className="font-bold text-slate-900 text-sm">Thêm sân bay mới</h4>
            <div className="space-y-2 text-xs">
              <input
                type="text"
                placeholder="Mã IATA (Ví dụ: HAN)"
                value={newIataCode}
                onChange={(e) => setNewIataCode(e.target.value.toUpperCase())}
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold uppercase"
              />
              <input
                type="text"
                placeholder="Tên sân bay"
                value={newAirportName}
                onChange={(e) => setNewAirportName(e.target.value)}
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold"
              />
              <input
                type="text"
                placeholder="Thành phố"
                value={newCity}
                onChange={(e) => setNewCity(e.target.value)}
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold"
              />
              <button
                onClick={() =>
                  onCreateAirport({
                    iataCode: newIataCode,
                    name: newAirportName,
                    city: newCity,
                    country: newCountry,
                  })
                }
                className="w-full py-2.5 bg-slate-900 text-white font-bold rounded-xl"
              >
                + Tạo sân bay
              </button>
            </div>
          </div>

          {/* Airlines Form */}
          <div className="bg-white rounded-3xl p-6 border border-slate-200 shadow-sm space-y-4">
            <h4 className="font-bold text-slate-900 text-sm">Thêm hãng bay mới</h4>
            <div className="space-y-2 text-xs">
              <input
                type="text"
                placeholder="Mã hãng (Ví dụ: VN)"
                value={newAirlineCode}
                onChange={(e) => setNewAirlineCode(e.target.value.toUpperCase())}
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold uppercase"
              />
              <input
                type="text"
                placeholder="Tên hãng bay"
                value={newAirlineName}
                onChange={(e) => setNewAirlineName(e.target.value)}
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold"
              />
              <button
                onClick={() =>
                  onCreateAirline({ code: newAirlineCode, name: newAirlineName })
                }
                className="w-full py-2.5 bg-slate-900 text-white font-bold rounded-xl"
              >
                + Tạo hãng bay
              </button>
            </div>
          </div>

          {/* Routes Form */}
          <div className="bg-white rounded-3xl p-6 border border-slate-200 shadow-sm space-y-4">
            <h4 className="font-bold text-slate-900 text-sm">Thêm đường bay mới</h4>
            {routeError && (
              <p className="text-red-600 text-[11px] font-bold">{routeError}</p>
            )}
            <div className="space-y-2 text-xs">
              <input
                type="text"
                placeholder="Sân bay đi (HAN)"
                value={routeFrom}
                onChange={(e) => {
                  setRouteError('')
                  setRouteFrom(e.target.value.toUpperCase())
                }}
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold uppercase"
              />
              <input
                type="text"
                placeholder="Sân bay đến (SGN)"
                value={routeTo}
                onChange={(e) => {
                  setRouteError('')
                  setRouteTo(e.target.value.toUpperCase())
                }}
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold uppercase"
              />
              <input
                type="number"
                placeholder="Khoảng cách (km)"
                value={routeDistance}
                onChange={(e) => setRouteDistance(Number(e.target.value))}
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold"
              />
              <button
                onClick={handleCreateRouteSubmit}
                className="w-full py-2.5 bg-sky-600 text-white font-bold rounded-xl"
              >
                + Tạo đường bay
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Modal: Create Flight */}
      {isCreateModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-sm animate-fade-in">
          <div className="bg-white rounded-3xl max-w-xl w-full p-6 space-y-4 shadow-2xl border border-slate-200">
            <div className="flex justify-between items-center border-b border-slate-100 pb-3">
              <h3 className="font-extrabold text-base text-slate-900">Tạo chuyến bay mới</h3>
              <button onClick={() => setIsCreateModalOpen(false)}>
                <span className="material-symbols-outlined text-slate-400">close</span>
              </button>
            </div>

            {flightError && (
              <div className="bg-red-50 border border-red-200 text-red-700 px-3 py-2 rounded-xl text-xs font-bold">
                {flightError}
              </div>
            )}

            <div className="grid grid-cols-2 gap-3 text-xs">
              <div>
                <label className="font-bold text-slate-600 block mb-1">Mã chuyến bay</label>
                <input
                  type="text"
                  value={flightNo}
                  onChange={(e) => setFlightNo(e.target.value)}
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold"
                />
              </div>

              <div>
                <label className="font-bold text-slate-600 block mb-1">Hãng bay</label>
                <input
                  type="text"
                  value={airlineCode}
                  onChange={(e) => setAirlineCode(e.target.value)}
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold"
                />
              </div>

              <div>
                <label className="font-bold text-slate-600 block mb-1">Sân bay đi</label>
                <input
                  type="text"
                  value={fromAirportCode}
                  onChange={(e) => {
                    setFlightError('')
                    setFromAirportCode(e.target.value.toUpperCase())
                  }}
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold uppercase"
                />
              </div>

              <div>
                <label className="font-bold text-slate-600 block mb-1">Sân bay đến</label>
                <input
                  type="text"
                  value={toAirportCode}
                  onChange={(e) => {
                    setFlightError('')
                    setToAirportCode(e.target.value.toUpperCase())
                  }}
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold uppercase"
                />
              </div>

              <div>
                <label className="font-bold text-slate-600 block mb-1">Giờ đi ISO</label>
                <input
                  type="datetime-local"
                  value={departureTime}
                  onChange={(e) => setDepartureTime(e.target.value)}
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold"
                />
              </div>

              <div>
                <label className="font-bold text-slate-600 block mb-1">Giờ đến ISO</label>
                <input
                  type="datetime-local"
                  value={arrivalTime}
                  onChange={(e) => setArrivalTime(e.target.value)}
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold"
                />
              </div>

              <div>
                <label className="font-bold text-slate-600 block mb-1">Tổng ghế</label>
                <input
                  type="number"
                  value={totalSeats}
                  onChange={(e) => setTotalSeats(Number(e.target.value))}
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold"
                />
              </div>

              <div>
                <label className="font-bold text-slate-600 block mb-1">Giá gốc (VNĐ)</label>
                <input
                  type="number"
                  value={basePrice}
                  onChange={(e) => setBasePrice(Number(e.target.value))}
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold"
                />
              </div>
            </div>

            <div className="pt-2 flex justify-end gap-2">
              <button
                onClick={() => setIsCreateModalOpen(false)}
                className="px-4 py-2 bg-slate-100 text-slate-700 font-bold rounded-xl text-xs"
              >
                Hủy
              </button>
              <button
                onClick={handleCreateFlightSubmit}
                className="px-5 py-2 bg-sky-600 text-white font-bold rounded-xl text-xs"
              >
                Xác nhận tạo
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Modal: Update Status */}
      {statusFlight && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-sm animate-fade-in">
          <div className="bg-white rounded-3xl max-w-md w-full p-6 space-y-4 shadow-2xl border border-slate-200 text-xs">
            <h3 className="font-extrabold text-base text-slate-900">
              Cập nhật trạng thái chuyến bay {statusFlight.flightNo}
            </h3>

            <div>
              <label className="font-bold text-slate-600 block mb-1">Trạng thái mới</label>
              <select
                value={newStatus}
                onChange={(e) => setNewStatus(e.target.value as FlightStatus)}
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold"
              >
                <option value="SCHEDULED">SCHEDULED</option>
                <option value="DELAYED">DELAYED</option>
                <option value="CANCELLED">CANCELLED</option>
                <option value="DEPARTED">DEPARTED</option>
              </select>
            </div>

            <div>
              <label className="font-bold text-slate-600 block mb-1">Giờ đi mới (Nếu hoãn)</label>
              <input
                type="datetime-local"
                value={newDeparture}
                onChange={(e) => setNewDeparture(e.target.value)}
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold"
              />
            </div>

            <div>
              <label className="font-bold text-slate-600 block mb-1">Giờ đến mới</label>
              <input
                type="datetime-local"
                value={newArrival}
                onChange={(e) => setNewArrival(e.target.value)}
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 font-bold"
              />
            </div>

            <div className="pt-2 flex justify-end gap-2">
              <button
                onClick={() => setStatusFlight(null)}
                className="px-4 py-2 bg-slate-100 text-slate-700 font-bold rounded-xl"
              >
                Hủy
              </button>
              <button
                onClick={() => {
                  onUpdateStatus(statusFlight.id, {
                    status: newStatus,
                    newDepartureTime: newDeparture || undefined,
                    newArrivalTime: newArrival || undefined,
                  })
                  setStatusFlight(null)
                }}
                className="px-5 py-2 bg-sky-600 text-white font-bold rounded-xl"
              >
                Lưu thay đổi
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
