import React, { useState } from 'react'
import type { BookingDetail } from '../features/bookings/types'

interface MyBookingsETicketProps {
  bookings: BookingDetail[]
  onCancelBooking: (id: number) => void
  onRefreshBookings: () => void
  formatMoney: (val: number | string | null | undefined) => string
  formatDateTime: (val: string | null | undefined) => string
}

export const MyBookingsETicket: React.FC<MyBookingsETicketProps> = ({
  bookings,
  onCancelBooking,
  onRefreshBookings,
  formatMoney,
  formatDateTime,
}) => {
  const [filterStatus, setFilterStatus] = useState<string>('ALL')
  const [selectedETicket, setSelectedETicket] = useState<BookingDetail | null>(null)

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
                <button
                  onClick={() => setSelectedETicket(b)}
                  className="px-4 py-2.5 bg-slate-900 hover:bg-sky-600 text-white rounded-xl font-bold text-xs flex items-center gap-1.5 shadow-sm transition-colors"
                >
                  <span className="material-symbols-outlined text-base">qr_code</span>
                  Vé điện tử
                </button>

                {b.status !== 'CANCELLED' && b.status !== 'EXPIRED' && (
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
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-sm animate-fade-in">
          <div className="bg-white rounded-3xl max-w-lg w-full overflow-hidden shadow-2xl border border-slate-200 relative">
            {/* Modal Header */}
            <div className="bg-slate-900 text-white p-6 relative">
              <button
                onClick={() => setSelectedETicket(null)}
                className="absolute top-4 right-4 text-slate-400 hover:text-white p-1 rounded-full"
              >
                <span className="material-symbols-outlined text-2xl">close</span>
              </button>

              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-xl bg-sky-500 text-white flex items-center justify-center font-bold">
                  <span className="material-symbols-outlined">flight</span>
                </div>
                <div>
                  <h3 className="text-lg font-black tracking-tight">SkySwift Digital Boarding Pass</h3>
                  <span className="text-xs text-sky-400 font-bold">Vé điện tử E-Ticket</span>
                </div>
              </div>
            </div>

            {/* Ticket Content */}
            <div className="p-6 space-y-6">
              {/* Status & Code */}
              <div className="flex justify-between items-center bg-slate-50 p-4 rounded-2xl border border-slate-200">
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
              </div>

              {/* Passenger & Flight Details */}
              <div className="grid grid-cols-2 gap-4 text-xs font-semibold text-slate-700">
                <div>
                  <span className="text-slate-400 block text-[10px] uppercase font-bold">Hành khách</span>
                  <strong className="text-slate-900 text-sm">
                    {selectedETicket.items?.[0]?.passengerName || 'N/A'}
                  </strong>
                </div>

                <div>
                  <span className="text-slate-400 block text-[10px] uppercase font-bold">Số ghế</span>
                  <strong className="text-sky-600 text-sm">
                    {selectedETicket.items?.[0]?.seatNo || 'N/A'}
                  </strong>
                </div>

                <div>
                  <span className="text-slate-400 block text-[10px] uppercase font-bold">Flight ID</span>
                  <strong className="text-slate-800">{selectedETicket.flightId}</strong>
                </div>

                <div>
                  <span className="text-slate-400 block text-[10px] uppercase font-bold">Giá thanh toán</span>
                  <strong className="text-orange-600">{formatMoney(selectedETicket.totalAmount)}</strong>
                </div>
              </div>

              {/* Barcode Graphic */}
              <div className="pt-4 border-t border-dashed border-slate-300 text-center space-y-2">
                <div className="barcode-visual rounded-md shadow-inner" />
                <span className="text-[10px] font-mono text-slate-400 uppercase tracking-widest block">
                  *{selectedETicket.bookingCode}*
                </span>
              </div>
            </div>

            {/* Modal Footer */}
            <div className="bg-slate-50 px-6 py-4 border-t border-slate-200 flex justify-end">
              <button
                onClick={() => setSelectedETicket(null)}
                className="px-6 py-2 bg-slate-900 hover:bg-slate-800 text-white rounded-xl font-bold text-xs"
              >
                Đóng
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
