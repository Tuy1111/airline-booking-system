import React, { useCallback, useEffect, useState } from 'react'
import type { BookingDetail } from '../features/bookings/types'
import { flightApi } from '../features/flights/api'
import type { FlightDetail } from '../features/flights/types'
import type { UserView } from '../features/users/types'

interface MyBookingsETicketProps {
  bookings: BookingDetail[]
  profile: UserView | null
  onCancelBooking: (id: number) => void
  onPayBooking: (booking: BookingDetail) => void | Promise<void>
  onRefreshBookings: () => void
  payingBookingId: number | null
  openBookingId: number | null
  onTicketOpened: () => void
  formatMoney: (val: number | string | null | undefined) => string
  formatDateTime: (val: string | null | undefined) => string
}

export const MyBookingsETicket: React.FC<MyBookingsETicketProps> = ({
  bookings,
  profile,
  onCancelBooking,
  onPayBooking,
  onRefreshBookings,
  payingBookingId,
  openBookingId,
  onTicketOpened,
  formatMoney,
  formatDateTime,
}) => {
  const [filterStatus, setFilterStatus] = useState<string>('ALL')
  const [selectedETicket, setSelectedETicket] = useState<BookingDetail | null>(null)
  const [selectedFlight, setSelectedFlight] = useState<FlightDetail | null>(null)
  const [isTicketLoading, setIsTicketLoading] = useState(false)
  const [ticketError, setTicketError] = useState('')

  const openTicket = useCallback(async (booking: BookingDetail) => {
    setSelectedETicket(booking)
    setSelectedFlight(null)
    setTicketError('')
    setIsTicketLoading(true)
    try {
      setSelectedFlight(await flightApi.getFlight(booking.flightId))
    } catch {
      setTicketError('Không tải được thông tin chuyến bay. Vui lòng thử lại.')
    } finally {
      setIsTicketLoading(false)
    }
  }, [])

  useEffect(() => {
    if (!openBookingId) return
    const booking = bookings.find((item) => item.id === openBookingId)
    if (!booking) return
    void openTicket(booking)
    onTicketOpened()
  }, [bookings, onTicketOpened, openBookingId, openTicket])

  const filteredBookings = bookings.filter((b) => {
    if (filterStatus === 'ALL') return true
    return b.status === filterStatus
  })

  return (
    <div className="max-w-5xl mx-auto px-4 py-8 animate-fade-in space-y-6">
      {/* Header Bar */}
      <div className="flex flex-wrap items-center justify-between gap-4">
        <div>
          <h2 className="text-2xl font-black text-slate-900 flex items-center gap-2">
            <span className="material-symbols-outlined text-sky-600">confirmation_number</span>
            Danh sách chuyến bay & Vé của tôi
          </h2>
          <p className="text-slate-500 text-sm">
            Quản lý các mã đặt chỗ và xem vé điện tử E-Ticket.
          </p>
        </div>

        <button
          onClick={onRefreshBookings}
          className="px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold text-xs rounded-xl flex items-center gap-1.5 transition-colors"
        >
          <span className="material-symbols-outlined text-base">refresh</span>
          Làm mới
        </button>
      </div>

      {/* Filter Tabs */}
      <div className="flex items-center gap-2 bg-slate-100 p-1.5 rounded-2xl w-fit border border-slate-200 text-xs font-bold">
        {['ALL', 'CONFIRMED', 'HELD', 'CANCELLED', 'EXPIRED'].map((status) => (
          <button
            key={status}
            onClick={() => setFilterStatus(status)}
            className={`px-4 py-2 rounded-xl transition-all ${
              filterStatus === status
                ? 'bg-slate-900 text-white shadow-sm'
                : 'text-slate-600 hover:text-slate-900'
            }`}
          >
            {status === 'ALL'
              ? 'Tất cả'
              : status === 'CONFIRMED'
              ? 'Đã xác nhận'
              : status === 'HELD'
              ? 'Đang giữ'
              : status === 'CANCELLED'
              ? 'Đã hủy'
              : 'Đã hết hạn'}
          </button>
        ))}
      </div>

      {/* Bookings List */}
      {filteredBookings.length === 0 ? (
        <div className="bg-white rounded-3xl p-12 text-center border border-slate-200 shadow-sm">
          <div className="w-16 h-16 bg-slate-100 text-slate-400 rounded-full flex items-center justify-center mx-auto mb-3">
            <span className="material-symbols-outlined text-3xl">inbox</span>
          </div>
          <p className="text-slate-600 font-bold text-base">Không có đơn đặt chỗ nào</p>
          <p className="text-slate-400 text-xs mt-1">
            Các chuyến bay bạn đã đặt hoặc giữ chỗ sẽ hiển thị tại đây.
          </p>
        </div>
      ) : (
        <div className="space-y-4">
          {filteredBookings.map((b) => (
            <div
              key={b.id}
              className="bg-white rounded-2xl p-6 border border-slate-200/80 shadow-sm hover:shadow-md transition-all grid grid-cols-1 md:grid-cols-12 gap-6 items-center"
            >
              {/* Left: Code & Status */}
              <div className="md:col-span-3">
                <span className="text-[11px] font-bold text-slate-400 block uppercase">
                  Mã đặt chỗ
                </span>
                <strong className="text-lg font-black text-slate-900">{b.bookingCode}</strong>
                <div className="mt-2">
                  <span
                    className={`badge ${
                      b.status === 'CONFIRMED'
                        ? 'badge-success'
                        : b.status === 'HELD'
                        ? 'badge-warning'
                        : 'badge-danger'
                    }`}
                  >
                    {b.status}
                  </span>
                </div>
              </div>

              {/* Middle: Items detail & Total Amount */}
              <div className="md:col-span-5 text-xs text-slate-600 space-y-1">
                <div>
                  <span className="text-slate-400">Flight ID:</span>{' '}
                  <strong className="text-slate-800">{b.flightId}</strong>
                </div>
                {b.items?.[0] && (
                  <div>
                    <span className="text-slate-400">Hành khách:</span>{' '}
                    <strong className="text-slate-900">{b.items[0].passengerName}</strong> (Ghế:{' '}
                    <span className="text-sky-600 font-bold">{b.items[0].seatNo}</span>)
                  </div>
                )}
                {b.baggageWeightKg !== undefined && b.baggageWeightKg > 0 && (
                  <div>
                    <span className="text-slate-400">Hành lý ký gửi:</span>{' '}
                    <strong className="text-slate-800">{b.baggageWeightKg} kg</strong> (+{formatMoney(b.baggageFee ?? 0)})
                  </div>
                )}
                <div>
                  <span className="text-slate-400">Ngày đặt:</span> {formatDateTime(b.createdAt)}
                </div>
                <div>
                  <span className="text-slate-400">Tổng tiền:</span>{' '}
                  <strong className="text-orange-600 text-sm font-black">
                    {formatMoney(b.totalAmount)}
                  </strong>
                </div>
              </div>

              {/* Right: Actions */}
              <div className="md:col-span-4 flex items-center justify-end gap-3">
                {b.status === 'CONFIRMED' && (
                  <button
                    onClick={() => void openTicket(b)}
                    className="px-4 py-2.5 bg-slate-900 hover:bg-sky-600 active:scale-[0.98] text-white rounded-xl font-bold text-xs flex items-center gap-1.5 shadow-sm transition-all"
                  >
                    <span className="material-symbols-outlined text-base">confirmation_number</span>
                    Xem vé điện tử
                  </button>
                )}

                {b.status === 'HELD' && (
                  <button
                    onClick={() => void onPayBooking(b)}
                    disabled={payingBookingId === b.id}
                    className="px-4 py-2.5 bg-orange-500 hover:bg-orange-600 active:scale-[0.98] disabled:opacity-60 text-white rounded-xl font-bold text-xs flex items-center gap-1.5 shadow-sm transition-all"
                  >
                    <span className="material-symbols-outlined text-base">payments</span>
                    {payingBookingId === b.id ? 'Đang mở...' : 'Thanh toán'}
                  </button>
                )}

                {b.status === 'HELD' && (
                  <button
                    onClick={() => onCancelBooking(b.id)}
                    className="px-3.5 py-2.5 bg-red-50 hover:bg-red-100 text-red-600 font-bold text-xs rounded-xl border border-red-200 transition-colors"
                  >
                    Hủy vé
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}

      {/* E-Ticket Boarding Pass Modal */}
      {selectedETicket && (
        <div
          className="fixed inset-0 z-50 flex items-center justify-center overflow-y-auto p-4 bg-slate-950/70 backdrop-blur-sm animate-fade-in"
          role="dialog"
          aria-modal="true"
          aria-label={`Vé điện tử ${selectedETicket.bookingCode}`}
        >
          <article className="my-auto w-full max-w-4xl overflow-hidden rounded-3xl bg-white shadow-2xl relative">
            <header className="bg-slate-900 text-white px-6 py-5 md:px-8 relative">
              <button
                onClick={() => setSelectedETicket(null)}
                aria-label="Đóng vé điện tử"
                className="absolute top-4 right-4 text-slate-400 hover:text-white focus-visible:text-white p-1 rounded-lg transition-colors"
              >
                <span className="material-symbols-outlined text-2xl">close</span>
              </button>

              <div className="flex items-center gap-4 pr-10">
                <div className="w-11 h-11 rounded-xl bg-sky-500 text-white flex items-center justify-center">
                  <span className="material-symbols-outlined">flight</span>
                </div>
                <div>
                  <p className="text-xs font-semibold tracking-[0.2em] text-sky-300 uppercase">SkySwift Airlines</p>
                  <h3 className="text-xl font-black tracking-tight">Vé điện tử / E-ticket</h3>
                </div>
              </div>
            </header>

            <div className="p-6 md:p-8 space-y-6">
              <section className="flex flex-wrap justify-between items-center gap-4 bg-slate-50 p-4 rounded-2xl border border-slate-200">
                <div>
                  <span className="text-[10px] font-bold text-slate-400 uppercase block">
                    Mã đặt chỗ (PNR)
                  </span>
                  <strong className="text-xl font-black text-slate-900">
                    {selectedETicket.bookingCode}
                  </strong>
                </div>
                <span
                  className={`badge ${
                    selectedETicket.status === 'CONFIRMED'
                      ? 'badge-success'
                      : selectedETicket.status === 'HELD'
                      ? 'badge-warning'
                      : 'badge-danger'
                  }`}
                >
                  {selectedETicket.status}
                </span>
              </section>

              {isTicketLoading ? (
                <section className="grid grid-cols-3 gap-4" aria-live="polite">
                  <div className="col-span-3 h-24 animate-pulse rounded-2xl bg-slate-100" />
                  <div className="h-20 animate-pulse rounded-xl bg-slate-100" />
                  <div className="h-20 animate-pulse rounded-xl bg-slate-100" />
                  <div className="h-20 animate-pulse rounded-xl bg-slate-100" />
                </section>
              ) : ticketError ? (
                <p className="rounded-xl bg-red-50 px-4 py-3 text-sm font-semibold text-red-700" role="alert">
                  {ticketError}
                </p>
              ) : selectedFlight && (
                <section className="rounded-2xl bg-sky-50/70 p-5 md:p-6">
                  <div className="flex flex-wrap items-end justify-between gap-4">
                    <div>
                      <p className="text-xs font-semibold text-slate-500">{selectedFlight.airlineName}</p>
                      <strong className="text-lg font-black text-slate-900">{selectedFlight.flightNo}</strong>
                    </div>
                    <div className="flex flex-1 items-center justify-end gap-3 text-right md:gap-6">
                      <div>
                        <strong className="block text-3xl font-black tracking-tight text-slate-900">{selectedFlight.fromAirport}</strong>
                        <span className="text-xs font-medium text-slate-500">{selectedFlight.fromCity}</span>
                      </div>
                      <span className="material-symbols-outlined text-sky-600">flight_takeoff</span>
                      <div>
                        <strong className="block text-3xl font-black tracking-tight text-slate-900">{selectedFlight.toAirport}</strong>
                        <span className="text-xs font-medium text-slate-500">{selectedFlight.toCity}</span>
                      </div>
                    </div>
                  </div>
                  <div className="mt-5 grid grid-cols-1 gap-3 border-t border-sky-100 pt-4 text-sm sm:grid-cols-3">
                    <div><span className="block text-xs text-slate-500">Khởi hành</span><strong>{formatDateTime(selectedFlight.departureTime)}</strong></div>
                    <div><span className="block text-xs text-slate-500">Đến nơi</span><strong>{formatDateTime(selectedFlight.arrivalTime)}</strong></div>
                    <div><span className="block text-xs text-slate-500">Máy bay</span><strong>{selectedFlight.aircraftType || 'Đang cập nhật'}</strong></div>
                  </div>
                </section>
              )}

              <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
                <section>
                  <h4 className="mb-3 text-sm font-black text-slate-900">Thông tin hành khách</h4>
                  <div className="space-y-3 rounded-2xl bg-slate-50 p-4 text-sm">
                    {selectedETicket.items.map((item) => (
                      <div key={item.id} className="grid grid-cols-[1fr_auto] gap-4 border-b border-slate-200 pb-3 last:border-0 last:pb-0">
                        <div>
                          <span className="block text-xs text-slate-500">Họ và tên</span>
                          <strong className="text-slate-900">{item.passengerName}</strong>
                          <span className="mt-1 block text-xs text-slate-500">Hộ chiếu / CCCD: {item.passengerPassport || 'Chưa cung cấp'}</span>
                        </div>
                        <div className="text-right">
                          <span className="block text-xs text-slate-500">Ghế</span>
                          <strong className="text-xl font-black text-sky-700">{item.seatNo}</strong>
                        </div>
                      </div>
                    ))}
                    <div className="flex justify-between gap-4 pt-1">
                      <span className="text-slate-500">Hành lý</span>
                      <strong>{selectedETicket.baggageWeightKg ? `${selectedETicket.baggageWeightKg} kg ký gửi` : '7 kg xách tay'}</strong>
                    </div>
                  </div>
                </section>

                <section>
                  <h4 className="mb-3 text-sm font-black text-slate-900">Người đặt vé</h4>
                  <dl className="space-y-3 rounded-2xl bg-slate-50 p-4 text-sm">
                    <div><dt className="text-xs text-slate-500">Họ và tên</dt><dd className="font-bold text-slate-900">{profile?.fullName || selectedETicket.items[0]?.passengerName || 'Chưa cập nhật'}</dd></div>
                    <div><dt className="text-xs text-slate-500">Email</dt><dd className="font-semibold text-slate-800">{profile?.email || 'Chưa cập nhật'}</dd></div>
                    <div><dt className="text-xs text-slate-500">Số điện thoại</dt><dd className="font-semibold text-slate-800">{profile?.phone || 'Chưa cập nhật'}</dd></div>
                  </dl>
                </section>
              </div>

              <section className="grid grid-cols-2 gap-x-6 gap-y-4 border-y border-dashed border-slate-300 py-5 text-sm md:grid-cols-4">
                <div><span className="block text-xs text-slate-500">Ngày đặt</span><strong>{formatDateTime(selectedETicket.createdAt)}</strong></div>
                <div><span className="block text-xs text-slate-500">Xác nhận lúc</span><strong>{formatDateTime(selectedETicket.confirmedAt)}</strong></div>
                <div><span className="block text-xs text-slate-500">Mã thanh toán</span><strong className="break-all">{selectedETicket.paymentId || 'N/A'}</strong></div>
                <div><span className="block text-xs text-slate-500">Tổng thanh toán</span><strong className="text-base font-black text-orange-600">{formatMoney(selectedETicket.totalAmount)}</strong></div>
              </section>

              <section className="text-center space-y-2">
                <div className="barcode-visual rounded-md shadow-inner" />
                <span className="text-[10px] font-mono text-slate-400 uppercase tracking-widest block">
                  *{selectedETicket.bookingCode}*
                </span>
                <p className="text-xs text-slate-500">Vui lòng xuất trình mã đặt chỗ và giấy tờ tùy thân khi làm thủ tục.</p>
              </section>
            </div>

            <footer className="bg-slate-50 px-6 py-4 border-t border-slate-200 flex justify-end">
              <button
                onClick={() => setSelectedETicket(null)}
                className="px-6 py-2.5 bg-slate-900 hover:bg-slate-800 active:scale-[0.98] text-white rounded-xl font-bold text-xs transition-all"
              >
                Đóng
              </button>
            </footer>
          </article>
        </div>
      )}
    </div>
  )
}
