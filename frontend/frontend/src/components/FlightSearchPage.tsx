import React, { useState } from 'react'
import type { Airline, Airport, FlightSearchParams, FlightSummary } from '../features/flights/types'
import { SearchWidget } from './SearchWidget'

interface FlightSearchPageProps {
  search: FlightSearchParams
  setSearch: React.Dispatch<React.SetStateAction<FlightSearchParams>>
  airports: Airport[]
  airlines: Airline[]
  flights: FlightSummary[]
  onSearch: (e?: React.FormEvent) => void
  onSelectFlight: (flight: FlightSummary) => void
  isLoading: boolean
  tripType: 'one-way' | 'round-trip'
  setTripType: (type: 'one-way' | 'round-trip') => void
  formatMoney: (val: number | string | null | undefined) => string
  formatDateTime: (val: string | null | undefined) => string
  durationLabel: (start: string, end: string) => string
}

export const FlightSearchPage: React.FC<FlightSearchPageProps> = ({
  search,
  setSearch,
  airports,
  airlines,
  flights,
  onSearch,
  onSelectFlight,
  isLoading,
  tripType,
  setTripType,
  formatMoney,
  durationLabel,
}) => {
  const [selectedAirlines, setSelectedAirlines] = useState<string[]>([])
  const [maxPriceFilter, setMaxPriceFilter] = useState<number>(5000000)
  const [sortBy, setSortBy] = useState<'price' | 'departureTime' | 'duration'>('departureTime')

  const toggleAirlineFilter = (code: string) => {
    setSelectedAirlines((prev) =>
      prev.includes(code) ? prev.filter((c) => c !== code) : [...prev, code],
    )
  }

  const handleClearFilters = () => {
    setSelectedAirlines([])
    setMaxPriceFilter(5000000)
    setSortBy('departureTime')
  }

  // Filter flights locally based on sidebar selections
  const filteredFlights = flights.filter((f) => {
    if (selectedAirlines.length > 0 && !selectedAirlines.includes(f.airlineCode)) {
      return false
    }
    if (f.currentPrice && f.currentPrice > maxPriceFilter) {
      return false
    }
    return true
  })

  // Sort flights
  const sortedFlights = [...filteredFlights].sort((a, b) => {
    if (sortBy === 'price') return (a.currentPrice || a.basePrice) - (b.currentPrice || b.basePrice)
    if (sortBy === 'departureTime')
      return new Date(a.departureTime).getTime() - new Date(b.departureTime).getTime()
    if (sortBy === 'duration') {
      const durA = new Date(a.arrivalTime).getTime() - new Date(a.departureTime).getTime()
      const durB = new Date(b.arrivalTime).getTime() - new Date(b.departureTime).getTime()
      return durA - durB
    }
    return 0
  })

  return (
    <div className="flight-search-page pt-24 pb-16 min-h-[100dvh] bg-slate-50">
      {/* Clean White Search Widget Box Card at Top of FlightSearchPage */}
      <div className="search-page-top max-w-7xl mx-auto px-4 md:px-8 mb-8">
        <SearchWidget
          search={search}
          setSearch={setSearch}
          airports={airports}
          airlines={airlines}
          onSearch={onSearch}
          isSearching={isLoading}
          tripType={tripType}
          setTripType={setTripType}
        />
      </div>

      {/* Main Content: 2 Columns matching Stitch screen 45d946a43afd4ba2a9c634da627b980b */}
      <main className="flight-results-layout max-w-7xl mx-auto px-4 md:px-8 flex gap-8">
        {/* Left Sidebar Filters */}
        <aside className="flight-filter w-72 flex-shrink-0 hidden lg:block space-y-6">
          <div className="flight-filter-panel bg-white rounded-3xl p-6 border border-slate-200 shadow-sm space-y-6 sticky top-24">
            <div className="flex justify-between items-center pb-3 border-b border-slate-100">
              <h2 className="text-base font-extrabold text-slate-900 flex items-center gap-1.5">
                <span className="material-symbols-outlined text-sky-600 text-lg">filter_alt</span>
                Bộ lọc tìm kiếm
              </h2>
              <button
                onClick={handleClearFilters}
                className="text-xs font-bold text-sky-600 hover:underline"
              >
                Xóa tất cả
              </button>
            </div>

            {/* Price Range */}
            <div className="space-y-2">
              <label className="block text-[11px] font-extrabold text-slate-400 uppercase tracking-wider">
                Khoảng giá tối đa
              </label>
              <input
                type="range"
                min={500000}
                max={5000000}
                step={100000}
                value={maxPriceFilter}
                onChange={(e) => setMaxPriceFilter(Number(e.target.value))}
                className="w-full accent-sky-600 cursor-pointer"
              />
              <div className="flex justify-between text-xs font-bold text-slate-700">
                <span>500.000đ</span>
                <span className="text-sky-600">{formatMoney(maxPriceFilter)}</span>
              </div>
            </div>

            {/* Airline Selector */}
            <div className="space-y-3 pt-3 border-t border-slate-100">
              <label className="block text-[11px] font-extrabold text-slate-400 uppercase tracking-wider">
                Hãng hàng không
              </label>
              <div className="space-y-2">
                {airlines.map((al) => (
                  <label
                    key={al.code}
                    className="flex items-center gap-3 cursor-pointer group hover:text-sky-600 text-xs font-bold text-slate-700"
                  >
                    <input
                      type="checkbox"
                      checked={selectedAirlines.includes(al.code)}
                      onChange={() => toggleAirlineFilter(al.code)}
                      className="rounded border-slate-300 text-sky-600 focus:ring-sky-500 w-4 h-4 cursor-pointer"
                    />
                    <span>
                      {al.name} ({al.code})
                    </span>
                  </label>
                ))}
              </div>
            </div>

            {/* Stops */}
            <div className="space-y-3 pt-3 border-t border-slate-100">
              <label className="block text-[11px] font-extrabold text-slate-400 uppercase tracking-wider">
                Số điểm dừng
              </label>
              <div className="space-y-2 text-xs font-bold text-slate-700">
                <label className="flex items-center gap-3 cursor-pointer">
                  <input
                    type="checkbox"
                    defaultChecked
                    className="rounded border-slate-300 text-sky-600 focus:ring-sky-500 w-4 h-4"
                  />
                  <span>Bay thẳng (Direct)</span>
                </label>
                <label className="flex items-center gap-3 cursor-pointer text-slate-400">
                  <input
                    type="checkbox"
                    disabled
                    className="rounded border-slate-300 text-slate-300 w-4 h-4"
                  />
                  <span>1 điểm dừng</span>
                </label>
              </div>
            </div>
          </div>
        </aside>

        {/* Right Main Flight Results Column */}
        <section className="flight-results flex-1 space-y-6">
          {/* Top Sort Controls Bar */}
          <div className="result-toolbar bg-white rounded-2xl p-4 border border-slate-200 shadow-sm flex flex-wrap items-center justify-between gap-4">
            <span className="text-xs font-bold text-slate-500">
              Tìm thấy <strong className="text-slate-900 text-sm">{sortedFlights.length}</strong> chuyến
              bay phù hợp từ <strong className="text-slate-900">{search.from}</strong> sang <strong className="text-slate-900">{search.to}</strong>
            </span>

            <div className="flex items-center gap-2">
              <span className="text-xs font-bold text-slate-400 uppercase">Sắp xếp:</span>
              <div className="flex items-center gap-1 bg-slate-100 p-1 rounded-xl">
                <button
                  onClick={() => setSortBy('departureTime')}
                  className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all ${
                    sortBy === 'departureTime'
                      ? 'bg-slate-900 text-white shadow-sm'
                      : 'text-slate-600 hover:text-slate-900'
                  }`}
                >
                  Giờ khởi hành
                </button>
                <button
                  onClick={() => setSortBy('price')}
                  className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all ${
                    sortBy === 'price'
                      ? 'bg-slate-900 text-white shadow-sm'
                      : 'text-slate-600 hover:text-slate-900'
                  }`}
                >
                  Giá rẻ nhất
                </button>
                <button
                  onClick={() => setSortBy('duration')}
                  className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all ${
                    sortBy === 'duration'
                      ? 'bg-slate-900 text-white shadow-sm'
                      : 'text-slate-600 hover:text-slate-900'
                  }`}
                >
                  Thời gian bay
                </button>
              </div>
            </div>
          </div>

          {/* Loading State */}
          {isLoading && (
            <div className="result-state loading-state bg-white rounded-3xl p-12 text-center border border-slate-200 shadow-sm">
              <div className="inline-block p-4 bg-sky-50 text-sky-600 rounded-full animate-bounce mb-3">
                <span className="material-symbols-outlined text-3xl">flight</span>
              </div>
              <p className="text-slate-700 font-bold">Đang tìm kiếm chuyến bay phù hợp nhất...</p>
            </div>
          )}

          {/* Empty State */}
          {!isLoading && sortedFlights.length === 0 && (
            <div className="result-state empty-state bg-white rounded-3xl p-12 text-center border border-slate-200 shadow-sm space-y-4">
              <div className="w-16 h-16 bg-slate-100 text-slate-400 rounded-full flex items-center justify-center mx-auto">
                <span className="material-symbols-outlined text-3xl">search_off</span>
              </div>
              <h3 className="text-lg font-bold text-slate-800">Không tìm thấy chuyến bay nào</h3>
              <p className="text-slate-500 text-xs max-w-md mx-auto">
                Không có chuyến bay nào phù hợp với bộ lọc hiện tại. Vui lòng thử thay đổi khoảng giá hoặc
                xóa bộ lọc hãng bay.
              </p>
              <button
                onClick={handleClearFilters}
                className="px-5 py-2.5 bg-slate-900 text-white rounded-xl text-xs font-bold shadow-md"
              >
                Xóa tất cả bộ lọc
              </button>
            </div>
          )}

          {/* Flight Cards List */}
          {!isLoading &&
            sortedFlights.map((flight) => (
              <div
                key={flight.id}
                className="flight-result-card bg-white rounded-3xl p-6 border border-slate-200/80 shadow-sm hover:shadow-xl hover:border-sky-400 transition-all duration-300 grid grid-cols-1 md:grid-cols-12 gap-6 items-center"
              >
                {/* Airline Info */}
                <div className="md:col-span-3 flex items-center gap-3">
                  <div className="w-12 h-12 rounded-2xl bg-slate-900 text-white font-extrabold flex items-center justify-center text-sm shadow-md">
                    {flight.airlineCode}
                  </div>
                  <div>
                    <div className="flex items-center gap-2">
                      <span className="font-black text-slate-900 text-sm">
                        {flight.airlineName}
                      </span>
                      <span className="text-[10px] font-extrabold text-sky-700 bg-sky-100 px-2 py-0.5 rounded-md">
                        {flight.flightNo}
                      </span>
                    </div>
                    <p className="text-xs text-slate-400 font-semibold mt-0.5">
                      {flight.aircraftType || 'Airbus A321'}
                    </p>
                  </div>
                </div>

                {/* Departure ➔ Duration ➔ Arrival */}
                <div className="md:col-span-6 flex items-center justify-between px-2">
                  <div className="text-left">
                    <div className="text-2xl font-black text-slate-900">
                      {new Date(flight.departureTime).toLocaleTimeString('vi-VN', {
                        hour: '2-digit',
                        minute: '2-digit',
                      })}
                    </div>
                    <div className="text-xs font-extrabold text-slate-800">{flight.fromAirport}</div>
                    <div className="text-[11px] text-slate-400">{flight.fromCity}</div>
                  </div>

                  <div className="flex-1 px-4 flex flex-col items-center">
                    <span className="text-[10px] font-bold text-slate-400 mb-1">
                      {durationLabel(flight.departureTime, flight.arrivalTime)}
                    </span>
                    <div className="w-full flex items-center gap-1">
                      <div className="h-[2px] flex-1 bg-slate-200" />
                      <span className="material-symbols-outlined text-sky-600 text-sm transform rotate-90">
                        flight
                      </span>
                      <div className="h-[2px] flex-1 bg-slate-200" />
                    </div>
                    <span className="text-[10px] font-bold text-emerald-600 mt-1">Bay thẳng</span>
                  </div>

                  <div className="text-right">
                    <div className="text-2xl font-black text-slate-900">
                      {new Date(flight.arrivalTime).toLocaleTimeString('vi-VN', {
                        hour: '2-digit',
                        minute: '2-digit',
                      })}
                    </div>
                    <div className="text-xs font-extrabold text-slate-800">{flight.toAirport}</div>
                    <div className="text-[11px] text-slate-400">{flight.toCity}</div>
                  </div>
                </div>

                {/* Price & Action Button */}
                <div className="md:col-span-3 flex flex-col items-end justify-center space-y-2 border-t md:border-t-0 md:border-l border-slate-100 pt-4 md:pt-0 md:pl-4">
                  <div className="text-right">
                    <span className="text-[10px] text-slate-400 font-bold uppercase block">Giá từ</span>
                    <span className="text-xl font-black text-orange-600">
                      {formatMoney(flight.currentPrice || flight.basePrice)}
                    </span>
                  </div>

                  <button
                    onClick={() => onSelectFlight(flight)}
                    className="w-full py-2.5 bg-sky-600 hover:bg-sky-700 text-white rounded-xl font-bold text-xs shadow-md shadow-sky-600/20 transition-all active:scale-95"
                  >
                    Chọn chuyến bay
                  </button>

                  <span className="text-[10px] text-slate-400 font-semibold">
                    Còn {flight.availableSeats} ghế trống
                  </span>
                </div>
              </div>
            ))}
        </section>
      </main>
    </div>
  )
}
