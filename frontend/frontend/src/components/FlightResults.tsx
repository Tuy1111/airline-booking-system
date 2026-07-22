import React from 'react'
import type { FlightSummary } from '../features/flights/types'

interface FlightResultsProps {
  flights: FlightSummary[]
  upcomingFlights: FlightSummary[]
  onSelectFlight: (flight: FlightSummary) => void
  isLoading: boolean
  formatMoney: (val: number | string | null | undefined) => string
  formatDateTime: (val: string | null | undefined) => string
  durationLabel: (start: string, end: string) => string
}

export const FlightResults: React.FC<FlightResultsProps> = ({
  flights,
  upcomingFlights,
  onSelectFlight,
  isLoading,
  formatMoney,
  formatDateTime,
  durationLabel,
}) => {
  if (isLoading) {
    return (
      <div className="max-w-6xl mx-auto px-4 py-12 text-center">
        <div className="inline-block p-4 bg-sky-50 text-sky-600 rounded-full animate-bounce mb-3">
          <span className="material-symbols-outlined text-3xl">flight</span>
        </div>
        <p className="text-slate-600 font-semibold">Đang tìm kiếm chuyến bay phù hợp nhất...</p>
      </div>
    )
  }

  return (
    <div className="max-w-6xl mx-auto px-4 py-8 space-y-8 animate-fade-in">
      {/* Flight Search Results Section */}
      <div>
        <div className="flex items-center justify-between mb-6">
          <div>
            <h2 className="text-2xl font-black text-slate-900 flex items-center gap-2">
              <span className="material-symbols-outlined text-sky-600">connecting_airports</span>
              Kết quả tìm kiếm chuyến bay
            </h2>
            <p className="text-slate-500 text-sm">
              Tìm thấy <strong className="text-slate-900">{flights.length}</strong> chuyến bay phù
              hợp với yêu cầu của bạn.
            </p>
          </div>
        </div>

        {flights.length === 0 ? (
          <div className="bg-white rounded-3xl p-12 text-center border border-slate-200 shadow-sm">
            <div className="w-16 h-16 bg-slate-100 text-slate-400 rounded-full flex items-center justify-center mx-auto mb-4">
              <span className="material-symbols-outlined text-3xl">search_off</span>
            </div>
            <h3 className="text-lg font-bold text-slate-800 mb-1">Không tìm thấy chuyến bay nào</h3>
            <p className="text-slate-500 text-sm max-w-md mx-auto">
              Rất tiếc, không có chuyến bay nào phù hợp với điều kiện tìm kiếm. Vui lòng thay đổi
              ngày khởi hành hoặc địa điểm đi/đến.
            </p>
          </div>
        ) : (
          <div className="space-y-4">
            {flights.map((flight) => (
              <div
                key={flight.id}
                className="bg-white rounded-2xl p-6 border border-slate-200/80 shadow-sm hover:shadow-md hover:border-sky-300 transition-all grid grid-cols-1 md:grid-cols-12 gap-6 items-center"
              >
                {/* Airline & Flight Details */}
                <div className="md:col-span-3 flex items-center gap-4">
                  <div className="w-12 h-12 rounded-xl bg-slate-900 text-white font-bold flex items-center justify-center text-sm shadow-md">
                    {flight.airlineCode}
                  </div>
                  <div>
                    <div className="flex items-center gap-2">
                      <span className="font-extrabold text-slate-900">{flight.airlineName}</span>
                      <span className="text-xs font-bold text-sky-700 bg-sky-100 px-2 py-0.5 rounded-md">
                        {flight.flightNo}
                      </span>
                    </div>
                    <p className="text-xs text-slate-500 font-medium mt-0.5">
                      {flight.aircraftType || 'Airbus A321'}
                    </p>
                  </div>
                </div>

                {/* Schedule & Duration Timeline */}
                <div className="md:col-span-5 flex items-center justify-between px-2">
                  {/* Departure */}
                  <div className="text-left">
                    <div className="text-xl font-black text-slate-900">
                      {new Date(flight.departureTime).toLocaleTimeString('vi-VN', {
                        hour: '2-digit',
                        minute: '2-digit',
                      })}
                    </div>
                    <div className="text-xs font-bold text-slate-700">{flight.fromAirport}</div>
                    <div className="text-[11px] text-slate-400">{flight.fromCity}</div>
                  </div>

                  {/* Flight Duration Visual */}
                  <div className="flex-1 px-4 text-center">
                    <span className="text-[11px] font-bold text-slate-500 bg-slate-100 px-2 py-0.5 rounded-full">
                      {durationLabel(flight.departureTime, flight.arrivalTime)}
                    </span>
                    <div className="relative my-2 flex items-center justify-center">
                      <div className="w-full h-0.5 bg-slate-200" />
                      <span className="material-symbols-outlined text-sky-600 absolute bg-white px-1 text-lg">
                        flight_takeoff
                      </span>
                    </div>
                    <span className="text-[10px] font-bold text-emerald-600 uppercase tracking-wider">
                      Bay thẳng
                    </span>
                  </div>

                  {/* Arrival */}
                  <div className="text-right">
                    <div className="text-xl font-black text-slate-900">
                      {new Date(flight.arrivalTime).toLocaleTimeString('vi-VN', {
                        hour: '2-digit',
                        minute: '2-digit',
                      })}
                    </div>
                    <div className="text-xs font-bold text-slate-700">{flight.toAirport}</div>
                    <div className="text-[11px] text-slate-400">{flight.toCity}</div>
                  </div>
                </div>

                {/* Price & Booking Button */}
                <div className="md:col-span-4 flex items-center justify-between md:justify-end gap-6 pt-4 md:pt-0 border-t md:border-t-0 border-slate-100">
                  <div className="text-left md:text-right">
                    <div className="text-[11px] text-slate-400 font-semibold uppercase">
                      Giá từ / hành khách
                    </div>
                    <div className="text-xl font-black text-orange-600">
                      {formatMoney(flight.currentPrice || flight.basePrice)}
                    </div>
                    <div className="text-[11px] text-emerald-600 font-semibold">
                      Còn {flight.availableSeats} chỗ trống
                    </div>
                  </div>

                  <button
                    onClick={() => onSelectFlight(flight)}
                    className="px-5 py-2.5 bg-slate-900 hover:bg-sky-600 text-white rounded-xl font-bold text-sm shadow-md transition-all active:scale-95 flex items-center gap-1.5"
                  >
                    Chọn ghế
                    <span className="material-symbols-outlined text-base">arrow_forward</span>
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Upcoming Departures Shelf */}
      {upcomingFlights.length > 0 && (
        <div className="pt-8 border-t border-slate-200">
          <h3 className="text-lg font-bold text-slate-800 mb-4 flex items-center gap-2">
            <span className="material-symbols-outlined text-amber-500">schedule</span>
            Chuyến bay sắp khởi hành trong 24h
          </h3>
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
            {upcomingFlights.slice(0, 3).map((flight) => (
              <div
                key={flight.id}
                onClick={() => onSelectFlight(flight)}
                className="bg-slate-50 border border-slate-200 rounded-xl p-4 hover:bg-white hover:border-sky-400 hover:shadow-md transition-all cursor-pointer"
              >
                <div className="flex justify-between items-center mb-2">
                  <span className="text-xs font-bold text-slate-900">{flight.flightNo}</span>
                  <span className="text-xs font-black text-orange-600">
                    {formatMoney(flight.currentPrice)}
                  </span>
                </div>
                <div className="flex items-center justify-between text-sm font-bold text-slate-800">
                  <span>{flight.fromAirport}</span>
                  <span className="material-symbols-outlined text-xs text-slate-400">
                    east
                  </span>
                  <span>{flight.toAirport}</span>
                </div>
                <div className="text-[11px] text-slate-500 mt-1">
                  Khởi hành: {formatDateTime(flight.departureTime)}
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}
