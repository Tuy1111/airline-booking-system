import React from 'react'
import type { FlightDetail, SeatMapItem } from '../features/flights/types'

interface SeatPickerProps {
  flight: FlightDetail
  seats: SeatMapItem[]
  selectedSeat: string
  setSelectedSeat: (seatNo: string) => void
  manualSeat: string
  setManualSeat: (seatNo: string) => void
  onProceedToPassenger: () => void
  onBackToResults: () => void
  formatMoney: (val: number | string | null | undefined) => string
}

export const SeatPicker: React.FC<SeatPickerProps> = ({
  flight,
  seats,
  selectedSeat,
  setSelectedSeat,
  manualSeat,
  setManualSeat,
  onProceedToPassenger,
  onBackToResults,
  formatMoney,
}) => {
  // Sort and group seats by row
  const sortedSeats = [...seats].sort((a, b) => {
    const rA = parseInt(a.seatNo, 10) || 0
    const rB = parseInt(b.seatNo, 10) || 0
    if (rA !== rB) return rA - rB
    return a.seatNo.localeCompare(b.seatNo)
  })

  const rowsMap: { [row: string]: SeatMapItem[] } = {}
  sortedSeats.forEach((seat) => {
    const rowNum = seat.seatNo.replace(/[A-Z]/g, '') || '1'
    if (!rowsMap[rowNum]) rowsMap[rowNum] = []
    rowsMap[rowNum].push(seat)
  })

  const activeSelectedSeatInfo = seats.find((s) => s.seatNo === selectedSeat)

  return (
    <div className="max-w-5xl mx-auto px-4 py-8 animate-fade-in space-y-6">
      {/* Top Navigation & Flight Summary Card */}
      <div className="flex items-center justify-between">
        <button
          onClick={onBackToResults}
          className="px-4 py-2 text-slate-600 hover:text-slate-900 bg-white border border-slate-200 rounded-xl font-bold text-xs flex items-center gap-1.5 shadow-sm transition-colors"
        >
          <span className="material-symbols-outlined text-base">arrow_back</span>
          Quay lại danh sách chuyến bay
        </button>
      </div>

      <div className="bg-slate-900 text-white rounded-3xl p-6 md:p-8 shadow-xl relative overflow-hidden">
        <div className="relative z-10 flex flex-wrap justify-between items-center gap-6">
          <div>
            <div className="flex items-center gap-3 mb-2">
              <span className="px-3 py-1 bg-sky-500/20 text-sky-300 rounded-full text-xs font-bold uppercase tracking-wider">
                {flight.airlineName} ({flight.airlineCode})
              </span>
              <span className="text-xs font-bold text-slate-300">
                Tàu bay: {flight.aircraftType || 'Airbus A321'}
              </span>
            </div>
            <h2 className="text-2xl md:text-3xl font-black flex items-center gap-3">
              <span>{flight.fromCity} ({flight.fromAirport})</span>
              <span className="material-symbols-outlined text-sky-400">flight_takeoff</span>
              <span>{flight.toCity} ({flight.toAirport})</span>
            </h2>
            <p className="text-xs text-slate-400 mt-1">
              Chuyến bay <strong>{flight.flightNo}</strong> • Khởi hành:{' '}
              {new Date(flight.departureTime).toLocaleString('vi-VN')}
            </p>
          </div>

          <div className="bg-white/10 backdrop-blur-md rounded-2xl p-4 text-right border border-white/10">
            <span className="text-xs text-slate-300 block font-medium">Giá cơ bản</span>
            <span className="text-2xl font-black text-amber-400">
              {formatMoney(flight.currentPrice || flight.basePrice)}
            </span>
          </div>
        </div>
      </div>

      {/* Seat Picker Layout Container */}
      <div className="grid grid-cols-1 md:grid-cols-12 gap-8 items-start">
        {/* Left/Main: Cabin Map Graphic */}
        <div className="md:col-span-8 bg-white rounded-3xl p-6 border border-slate-200 shadow-sm">
          <h3 className="text-lg font-bold text-slate-900 mb-4 flex items-center justify-between">
            <span>Sơ đồ chỗ ngồi</span>
            <span className="text-xs text-slate-500 font-normal">
              Chọn ghế bằng cách click trực tiếp trên sơ đồ
            </span>
          </h3>

          {/* Seat Status Legend */}
          <div className="flex flex-wrap items-center justify-center gap-4 py-3 bg-slate-50 rounded-2xl mb-6 text-xs font-bold text-slate-700">
            <div className="flex items-center gap-2">
              <span className="w-5 h-5 rounded-md bg-white border border-slate-300 inline-block" />
              <span>Trống</span>
            </div>
            <div className="flex items-center gap-2">
              <span className="w-5 h-5 rounded-md bg-sky-500 text-white inline-block border border-sky-600" />
              <span>Đang chọn</span>
            </div>
            <div className="flex items-center gap-2">
              <span className="w-5 h-5 rounded-md bg-amber-100 border border-amber-400 inline-block" />
              <span>Đã giữ</span>
            </div>
            <div className="flex items-center gap-2">
              <span className="w-5 h-5 rounded-md bg-slate-200 border border-slate-300 inline-block" />
              <span>Đã đặt</span>
            </div>
            <div className="flex items-center gap-2">
              <span className="w-5 h-5 rounded-md bg-white border-2 border-amber-500 inline-block" />
              <span>Thương gia</span>
            </div>
          </div>

          {/* Airplane Nose Cone Visual */}
          <div className="max-w-md mx-auto bg-slate-100 rounded-t-full pt-8 pb-4 text-center border-t-2 border-slate-300 mb-6">
            <span className="material-symbols-outlined text-slate-400 text-3xl">flight</span>
            <span className="block text-[10px] font-bold text-slate-400 uppercase tracking-widest mt-1">
              Đầu máy bay / Cockpit
            </span>
          </div>

          {/* Rows Grid */}
          <div className="max-w-md mx-auto space-y-3 pb-8 overflow-x-auto">
            {Object.entries(rowsMap).map(([rowNum, rowSeats]) => {
              return (
                <div key={rowNum} className="flex items-center justify-center gap-2">
                  <span className="w-6 text-center text-xs font-bold text-slate-400">
                    {rowNum}
                  </span>

                  {/* Left seats */}
                  <div className="flex gap-2">
                    {rowSeats
                      .filter((s) => s.seatNo.endsWith('A') || s.seatNo.endsWith('B') || s.seatNo.endsWith('C'))
                      .map((seat) => {
                        const isSelected = selectedSeat === seat.seatNo
                        const isHeld = seat.status === 'HELD'
                        const isBooked = seat.status === 'BOOKED'

                        let btnClass = 'seat-button seat-available'
                        if (isBooked) btnClass = 'seat-button seat-booked'
                        else if (isHeld) btnClass = 'seat-button seat-held'
                        else if (isSelected) btnClass = 'seat-button seat-selected'
                        else if (seat.seatClass === 'BUSINESS' || seat.seatClass === 'FIRST')
                          btnClass = 'seat-button seat-available seat-business'

                        return (
                          <button
                            key={seat.seatNo}
                            disabled={isBooked || isHeld}
                            onClick={() => {
                              setSelectedSeat(seat.seatNo)
                              setManualSeat(seat.seatNo)
                            }}
                            className={btnClass}
                            title={`${seat.seatNo} - Class: ${seat.seatClass} - Status: ${seat.status}`}
                          >
                            <span>{seat.seatNo}</span>
                          </button>
                        );
                      })}
                  </div>

                  {/* Aisle Spacer */}
                  <div className="w-8 text-center text-[10px] font-bold text-slate-300 uppercase tracking-widest">
                    Lối đi
                  </div>

                  {/* Right seats */}
                  <div className="flex gap-2">
                    {rowSeats
                      .filter((s) => s.seatNo.endsWith('D') || s.seatNo.endsWith('E') || s.seatNo.endsWith('F'))
                      .map((seat) => {
                        const isSelected = selectedSeat === seat.seatNo
                        const isHeld = seat.status === 'HELD'
                        const isBooked = seat.status === 'BOOKED'

                        let btnClass = 'seat-button seat-available'
                        if (isBooked) btnClass = 'seat-button seat-booked'
                        else if (isHeld) btnClass = 'seat-button seat-held'
                        else if (isSelected) btnClass = 'seat-button seat-selected'
                        else if (seat.seatClass === 'BUSINESS' || seat.seatClass === 'FIRST')
                          btnClass = 'seat-button seat-available seat-business'

                        return (
                          <button
                            key={seat.seatNo}
                            disabled={isBooked || isHeld}
                            onClick={() => {
                              setSelectedSeat(seat.seatNo)
                              setManualSeat(seat.seatNo)
                            }}
                            className={btnClass}
                            title={`${seat.seatNo} - Class: ${seat.seatClass} - Status: ${seat.status}`}
                          >
                            <span>{seat.seatNo}</span>
                          </button>
                        );
                      })}
                  </div>
                </div>
              );
            })}

            {Object.keys(rowsMap).length === 0 && (
              <div className="text-center py-8 text-slate-400 text-sm">
                Đang tải sơ đồ ghế...
              </div>
            )}
          </div>
        </div>

        {/* Right Sidebar: Selected Seat & Action Box */}
        <div className="md:col-span-4 bg-white rounded-3xl p-6 border border-slate-200 shadow-sm space-y-6">
          <h3 className="text-base font-bold text-slate-900 border-b border-slate-100 pb-3">
            Thông tin ghế được chọn
          </h3>

          <div className="bg-sky-50 rounded-2xl p-5 border border-sky-100 text-center">
            {selectedSeat ? (
              <div>
                <span className="text-xs font-bold text-sky-600 uppercase tracking-wider">
                  Số ghế đã chọn
                </span>
                <div className="text-4xl font-black text-sky-900 my-1">{selectedSeat}</div>
                <div className="text-xs font-bold text-slate-600">
                  Hạng ghế:{' '}
                  <span className="text-sky-700 font-extrabold">
                    {activeSelectedSeatInfo?.seatClass || 'ECONOMY'}
                  </span>
                </div>
              </div>
            ) : (
              <div className="py-4">
                <span className="material-symbols-outlined text-slate-400 text-3xl mb-1">
                  event_seat
                </span>
                <p className="text-xs text-slate-500 font-medium">
                  Vui lòng chọn 1 vị trí ghế trên sơ đồ
                </p>
              </div>
            )}
          </div>

          {/* Manual Input Fallback */}
          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">
              Nhập mã ghế thủ công (Ví dụ: 12A)
            </label>
            <div className="flex gap-2">
              <input
                type="text"
                value={manualSeat}
                onChange={(e) => setManualSeat(e.target.value.toUpperCase())}
                placeholder="12A"
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-sm font-bold uppercase focus:outline-none focus:border-sky-500"
              />
              <button
                type="button"
                onClick={() => setSelectedSeat(manualSeat)}
                className="px-4 py-2.5 bg-slate-100 hover:bg-slate-200 font-bold text-xs rounded-xl text-slate-800"
              >
                Áp dụng
              </button>
            </div>
          </div>

          <div className="pt-4 border-t border-slate-100">
            <button
              onClick={onProceedToPassenger}
              disabled={!selectedSeat}
              className="w-full py-3.5 bg-sky-600 hover:bg-sky-700 disabled:opacity-50 text-white rounded-xl font-bold text-sm shadow-md transition-all flex items-center justify-center gap-2"
            >
              Tiếp tục điền thông tin hành khách
              <span className="material-symbols-outlined text-base">arrow_forward</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}
