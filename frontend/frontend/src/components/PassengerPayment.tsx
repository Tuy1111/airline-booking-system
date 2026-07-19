import React from 'react'
import type { HoldSeatResponse } from '../features/bookings/types'
import type { FlightDetail } from '../features/flights/types'
import type { PaymentResponse } from '../features/payments/types'
import type { AuthenticatedUser } from '../shared/auth/keycloak'

interface PassengerPaymentProps {
  flight: FlightDetail
  selectedSeat: string
  passengerName: string
  setPassengerName: (name: string) => void
  passengerPassport: string
  setPassengerPassport: (passport: string) => void
  onHoldSeat: (e: React.FormEvent) => void
  isHolding: boolean
  holdResult: HoldSeatResponse | null
  payment: PaymentResponse | null
  onCreatePayment: () => void
  onCheckPaymentStatus: () => void
  isCreatingPayment: boolean
  onViewETicket: () => void
  formatMoney: (val: number | string | null | undefined) => string
  user: AuthenticatedUser | null
  onLogin: () => void
}

export const PassengerPayment: React.FC<PassengerPaymentProps> = ({
  flight,
  selectedSeat,
  passengerName,
  setPassengerName,
  passengerPassport,
  setPassengerPassport,
  onHoldSeat,
  isHolding,
  holdResult,
  payment,
  onCreatePayment,
  onCheckPaymentStatus,
  isCreatingPayment,
  onViewETicket,
  formatMoney,
  user,
  onLogin,
}) => {
  return (
    <div className="max-w-4xl mx-auto px-4 py-8 animate-fade-in space-y-8">
      {/* Flight & Seat Summary Header */}
      <div className="bg-white rounded-3xl p-6 border border-slate-200 shadow-sm flex flex-wrap justify-between items-center gap-4">
        <div>
          <span className="text-xs font-bold text-sky-600 uppercase tracking-wider block mb-1">
            Chuyến bay đã chọn
          </span>
          <h2 className="text-xl font-black text-slate-900">
            {flight.flightNo} • {flight.fromCity} ({flight.fromAirport}) ➔ {flight.toCity} ({flight.toAirport})
          </h2>
          <p className="text-xs text-slate-500 mt-1">
            Khởi hành: {new Date(flight.departureTime).toLocaleString('vi-VN')}
          </p>
        </div>

        <div className="flex items-center gap-4">
          <div className="text-right">
            <span className="text-xs font-bold text-slate-400 block">Số ghế</span>
            <span className="text-2xl font-black text-sky-600">{selectedSeat}</span>
          </div>
          <div className="text-right pl-4 border-l border-slate-200">
            <span className="text-xs font-bold text-slate-400 block">Tổng tiền</span>
            <span className="text-xl font-black text-orange-600">
              {formatMoney(holdResult?.price || flight.currentPrice || flight.basePrice)}
            </span>
          </div>
        </div>
      </div>

      {/* Auth Requirement Warning if unauthenticated */}
      {!user && (
        <div className="bg-amber-50 border border-amber-200 rounded-3xl p-6 flex flex-wrap items-center justify-between gap-4 text-amber-900">
          <div className="flex items-center gap-3">
            <span className="material-symbols-outlined text-amber-600 text-3xl">lock</span>
            <div>
              <h4 className="font-extrabold text-sm">Yêu cầu đăng nhập tài khoản</h4>
              <p className="text-xs text-amber-800">
                Để thực hiện giữ ghế và thanh toán, bạn cần đăng nhập tài khoản Keycloak.
              </p>
            </div>
          </div>
          <button
            onClick={onLogin}
            className="px-6 py-2.5 bg-slate-900 hover:bg-sky-600 text-white font-bold rounded-xl text-xs shadow-md transition-colors"
          >
            Đăng nhập ngay
          </button>
        </div>
      )}

      {/* Step 1: Passenger Form */}
      {!holdResult && (
        <div className="bg-white rounded-3xl p-8 border border-slate-200 shadow-sm space-y-6">
          <div className="flex items-center gap-3 border-b border-slate-100 pb-4">
            <div className="w-8 h-8 rounded-full bg-slate-900 text-white font-bold flex items-center justify-center text-sm">
              1
            </div>
            <h3 className="text-lg font-bold text-slate-900">Thông tin hành khách</h3>
          </div>

          <form
            onSubmit={(e) => {
              if (!user) {
                e.preventDefault()
                onLogin()
                return
              }
              onHoldSeat(e)
            }}
            className="space-y-4"
          >
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-bold text-slate-700 uppercase mb-1">
                  Họ và tên hành khách
                </label>
                <input
                  type="text"
                  required
                  value={passengerName}
                  onChange={(e) => setPassengerName(e.target.value)}
                  placeholder="NGUYEN VAN A"
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-3 text-sm font-bold focus:outline-none focus:border-sky-500"
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-700 uppercase mb-1">
                  Số hộ chiếu / CCCD
                </label>
                <input
                  type="text"
                  required
                  value={passengerPassport}
                  onChange={(e) => setPassengerPassport(e.target.value)}
                  placeholder="P1234567"
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-3 text-sm font-bold focus:outline-none focus:border-sky-500"
                />
              </div>
            </div>

            <div className="pt-4 flex justify-end">
              <button
                type="submit"
                disabled={isHolding}
                className="px-8 py-3.5 bg-orange-500 hover:bg-orange-600 disabled:opacity-50 text-white font-bold rounded-xl text-sm shadow-lg shadow-orange-500/20 transition-all flex items-center gap-2"
              >
                <span className="material-symbols-outlined text-lg">lock</span>
                {isHolding ? 'Đang giữ ghế...' : 'Giữ ghế & Tiếp tục thanh toán'}
              </button>
            </div>
          </form>
        </div>
      )}

      {/* Step 2: Seat Hold Success Banner & SePay Payment Generator */}
      {holdResult && (
        <div className="space-y-6">
          {/* Seat Hold Alert Box */}
          <div className="bg-emerald-50 border border-emerald-200 rounded-3xl p-6 text-emerald-900 space-y-2">
            <div className="flex items-center gap-2 text-emerald-700 font-extrabold text-base">
              <span className="material-symbols-outlined text-2xl">check_circle</span>
              <span>{holdResult.message || 'Giữ ghế thành công!'}</span>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-xs font-semibold pt-2 border-t border-emerald-200/60">
              <div>
                <span className="text-emerald-600 block">Mã đặt chỗ (Booking Code):</span>
                <strong className="text-sm text-slate-900">{holdResult.bookingCode}</strong>
              </div>
              <div>
                <span className="text-emerald-600 block">Ghế giữ:</span>
                <strong className="text-sm text-slate-900">{holdResult.seatNo}</strong>
              </div>
              <div>
                <span className="text-emerald-600 block">Hạn giữ ghế:</span>
                <strong className="text-sm text-amber-700">
                  {new Date(holdResult.holdExpiresAt).toLocaleTimeString('vi-VN')}
                </strong>
              </div>
            </div>
          </div>

          {/* Payment Section */}
          <div className="bg-white rounded-3xl p-8 border border-slate-200 shadow-sm space-y-6">
            <div className="flex items-center gap-3 border-b border-slate-100 pb-4">
              <div className="w-8 h-8 rounded-full bg-slate-900 text-white font-bold flex items-center justify-center text-sm">
                2
              </div>
              <h3 className="text-lg font-bold text-slate-900">Thanh toán chuyển khoản SePay VietQR</h3>
            </div>

            {!payment ? (
              <div className="text-center py-6 space-y-4">
                <p className="text-slate-600 text-sm">
                  Vui lòng tạo mã QR thanh toán để thực hiện chuyển khoản qua cổng SePay MBBank.
                </p>
                <button
                  onClick={onCreatePayment}
                  disabled={isCreatingPayment}
                  className="px-8 py-3.5 bg-sky-600 hover:bg-sky-700 disabled:opacity-50 text-white rounded-xl font-bold text-sm shadow-md transition-all inline-flex items-center gap-2"
                >
                  <span className="material-symbols-outlined text-lg">qr_code_2</span>
                  {isCreatingPayment ? 'Đang tạo mã QR...' : 'Tạo mã QR thanh toán VietQR'}
                </button>
              </div>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-12 gap-8 items-center">
                {/* VietQR Image */}
                <div className="md:col-span-5 text-center bg-slate-50 p-6 rounded-2xl border border-slate-200">
                  <span className="text-xs font-bold text-slate-500 block mb-2">
                    Quét mã VietQR bằng ứng dụng Ngân hàng
                  </span>
                  {payment.qrUrl ? (
                    <img
                      src={payment.qrUrl}
                      alt="SePay VietQR Code"
                      className="w-56 h-56 mx-auto object-contain bg-white p-2 rounded-xl border border-slate-200 shadow-sm"
                    />
                  ) : (
                    <div className="w-56 h-56 mx-auto bg-slate-200 rounded-xl flex items-center justify-center text-slate-400 text-xs font-bold">
                      [Mã QR VietQR]
                    </div>
                  )}
                  <span className="text-[11px] font-semibold text-emerald-600 mt-2 block">
                    Tự động xác nhận sau khi chuyển khoản
                  </span>
                </div>

                {/* Transfer Details */}
                <div className="md:col-span-7 space-y-4 text-sm">
                  <div className="bg-slate-50 p-4 rounded-xl space-y-2 border border-slate-200">
                    <div className="flex justify-between">
                      <span className="text-slate-500">Mã giao dịch (Transfer Code):</span>
                      <strong className="text-slate-900 font-extrabold">{payment.transferCode}</strong>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-slate-500">Số tiền:</span>
                      <strong className="text-orange-600 font-black text-lg">
                        {formatMoney(payment.amount)}
                      </strong>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-slate-500">Trạng thái:</span>
                      <span
                        className={`badge ${
                          payment.status === 'SUCCESS'
                            ? 'badge-success'
                            : payment.status === 'FAILED'
                            ? 'badge-danger'
                            : 'badge-warning'
                        }`}
                      >
                        {payment.status}
                      </span>
                    </div>
                  </div>

                  <div className="flex flex-wrap gap-3 pt-2">
                    <button
                      onClick={onCheckPaymentStatus}
                      className="px-5 py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-800 rounded-xl font-bold text-xs flex items-center gap-1.5"
                    >
                      <span className="material-symbols-outlined text-base">sync</span>
                      Kiểm tra trạng thái thanh toán
                    </button>

                    <button
                      onClick={onViewETicket}
                      className="px-5 py-2.5 bg-slate-900 hover:bg-sky-600 text-white rounded-xl font-bold text-xs flex items-center gap-1.5 shadow-md ml-auto"
                    >
                      <span className="material-symbols-outlined text-base">confirmation_number</span>
                      Xem vé điện tử (E-Ticket)
                    </button>
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  )
}
