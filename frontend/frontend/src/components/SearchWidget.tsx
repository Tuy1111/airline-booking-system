import React, { useState } from 'react'
import type { Airline, Airport, FlightSearchParams } from '../features/flights/types'

interface SearchWidgetProps {
  search: FlightSearchParams
  setSearch: React.Dispatch<React.SetStateAction<FlightSearchParams>>
  airports: Airport[]
  airlines: Airline[]
  onSearch: (e?: React.FormEvent) => void
  isSearching: boolean
  tripType: 'one-way' | 'round-trip'
  setTripType: (type: 'one-way' | 'round-trip') => void
}

export const SearchWidget: React.FC<SearchWidgetProps> = ({
  search,
  setSearch,
  airports,
  airlines,
  onSearch,
  isSearching,
  tripType,
  setTripType,
}) => {
  const [validationError, setValidationError] = useState('')

  const handleSwapAirports = () => {
    setValidationError('')
    setSearch((prev) => ({
      ...prev,
      from: prev.to,
      to: prev.from,
    }))
  }

  const handleFromChange = (newFrom: string) => {
    setValidationError('')
    setSearch((prev) => {
      let newTo = prev.to
      if (newFrom === prev.to) {
        const alternative =
          airports.find((ap) => ap.iataCode !== newFrom)?.iataCode ||
          (newFrom === 'HAN' ? 'SGN' : 'HAN')
        newTo = alternative
      }
      return { ...prev, from: newFrom, to: newTo }
    })
  }

  const handleToChange = (newTo: string) => {
    setValidationError('')
    setSearch((prev) => {
      let newFrom = prev.from
      if (newTo === prev.from) {
        const alternative =
          airports.find((ap) => ap.iataCode !== newTo)?.iataCode ||
          (newTo === 'SGN' ? 'HAN' : 'SGN')
        newFrom = alternative
      }
      return { ...prev, from: newFrom, to: newTo }
    })
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (search.from === search.to) {
      setValidationError('Điểm đi và điểm đến không được trùng nhau!')
      return
    }
    setValidationError('')
    onSearch(e)
  }

  return (
    <div className="search-widget bg-white p-6 md:p-8 rounded-3xl shadow-xl border border-slate-200/90">
      {/* Validation Error Alert */}
      {validationError && (
        <div className="mb-4 bg-red-50 border border-red-200 text-red-700 px-4 py-2.5 rounded-xl text-xs font-bold flex items-center gap-2">
          <span className="material-symbols-outlined text-base">error</span>
          <span>{validationError}</span>
        </div>
      )}

      {/* Trip Type Selector & Sort Bar */}
      <div className="flex flex-wrap items-center justify-between gap-4 mb-6 pb-4 border-b border-slate-100">
        <div className="flex items-center gap-2 bg-slate-100 p-1 rounded-full border border-slate-200">
          <button
            type="button"
            onClick={() => setTripType('round-trip')}
            className={`px-4 py-1.5 rounded-full text-xs font-bold transition-all ${
              tripType === 'round-trip'
                ? 'bg-slate-900 text-white shadow-sm'
                : 'text-slate-600 hover:text-slate-900'
            }`}
          >
            Khứ hồi
          </button>
          <button
            type="button"
            onClick={() => setTripType('one-way')}
            className={`px-4 py-1.5 rounded-full text-xs font-bold transition-all ${
              tripType === 'one-way'
                ? 'bg-slate-900 text-white shadow-sm'
                : 'text-slate-600 hover:text-slate-900'
            }`}
          >
            Một chiều
          </button>
        </div>

        <div className="flex items-center gap-4 text-xs font-semibold text-slate-600">
          <label className="flex items-center gap-2 cursor-pointer">
            <span className="text-[11px] font-bold text-slate-500 uppercase tracking-wider">
              Sắp xếp:
            </span>
            <select
              value={search.sort || 'departureTime'}
              onChange={(e) =>
                setSearch((prev) => ({
                  ...prev,
                  sort: e.target.value as 'price' | 'departureTime' | 'arrivalTime',
                }))
              }
              className="bg-slate-50 border border-slate-200 rounded-lg px-2.5 py-1 text-slate-800 font-bold focus:outline-none focus:ring-2 focus:ring-sky-500"
            >
              <option value="departureTime">Giờ khởi hành</option>
              <option value="price">Giá vé</option>
              <option value="arrivalTime">Giờ hạ cánh</option>
            </select>
          </label>

          <label className="flex items-center gap-2 cursor-pointer">
            <span className="text-[11px] font-bold text-slate-500 uppercase tracking-wider">
              Thứ tự:
            </span>
            <select
              value={search.order || 'asc'}
              onChange={(e) =>
                setSearch((prev) => ({
                  ...prev,
                  order: e.target.value as 'asc' | 'desc',
                }))
              }
              className="bg-slate-50 border border-slate-200 rounded-lg px-2.5 py-1 text-slate-800 font-bold focus:outline-none focus:ring-2 focus:ring-sky-500"
            >
              <option value="asc">Tăng dần</option>
              <option value="desc">Giảm dần</option>
            </select>
          </label>
        </div>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Search Input Grid */}
        <div className="grid grid-cols-1 md:grid-cols-12 gap-3 items-center">
          {/* Departure Airport */}
          <div className="md:col-span-3 relative group">
            <label className="block text-[11px] font-bold text-slate-500 uppercase tracking-wider mb-1">
              Điểm đi
            </label>
            <div className="flex items-center bg-slate-50 border border-slate-200 rounded-xl p-3 focus-within:border-sky-500 focus-within:ring-2 focus-within:ring-sky-500/20 transition-all">
              <span className="material-symbols-outlined text-sky-600 mr-2.5">
                flight_takeoff
              </span>
              <select
                value={search.from}
                onChange={(e) => handleFromChange(e.target.value)}
                className="w-full bg-transparent border-none text-slate-900 font-bold text-sm focus:outline-none cursor-pointer"
                required
              >
                {airports.map((ap) => (
                  <option
                    key={ap.iataCode}
                    value={ap.iataCode}
                    disabled={ap.iataCode === search.to}
                  >
                    {ap.city} ({ap.iataCode}) - {ap.name}
                    {ap.iataCode === search.to ? ' (Đã chọn ở điểm đến)' : ''}
                  </option>
                ))}
                {airports.length === 0 && (
                  <>
                    <option value="HAN" disabled={search.to === 'HAN'}>
                      Hà Nội (HAN)
                    </option>
                    <option value="SGN" disabled={search.to === 'SGN'}>
                      TP. Hồ Chí Minh (SGN)
                    </option>
                    <option value="DAD" disabled={search.to === 'DAD'}>
                      Đà Nẵng (DAD)
                    </option>
                  </>
                )}
              </select>
            </div>
          </div>

          {/* Swap Button */}
          <div className="md:col-span-1 flex justify-center -my-2 md:my-0">
            <button
              type="button"
              onClick={handleSwapAirports}
              className="p-2.5 bg-slate-100 hover:bg-sky-100 text-slate-600 hover:text-sky-600 rounded-full border border-slate-200 transition-all shadow-sm active:scale-95"
              title="Đổi chiều bay"
            >
              <span className="material-symbols-outlined text-lg">swap_horiz</span>
            </button>
          </div>

          {/* Arrival Airport */}
          <div className="md:col-span-3 relative group">
            <label className="block text-[11px] font-bold text-slate-500 uppercase tracking-wider mb-1">
              Điểm đến
            </label>
            <div className="flex items-center bg-slate-50 border border-slate-200 rounded-xl p-3 focus-within:border-sky-500 focus-within:ring-2 focus-within:ring-sky-500/20 transition-all">
              <span className="material-symbols-outlined text-sky-600 mr-2.5">
                flight_land
              </span>
              <select
                value={search.to}
                onChange={(e) => handleToChange(e.target.value)}
                className="w-full bg-transparent border-none text-slate-900 font-bold text-sm focus:outline-none cursor-pointer"
                required
              >
                {airports.map((ap) => (
                  <option
                    key={ap.iataCode}
                    value={ap.iataCode}
                    disabled={ap.iataCode === search.from}
                  >
                    {ap.city} ({ap.iataCode}) - {ap.name}
                    {ap.iataCode === search.from ? ' (Đã chọn ở điểm đi)' : ''}
                  </option>
                ))}
                {airports.length === 0 && (
                  <>
                    <option value="SGN" disabled={search.from === 'SGN'}>
                      TP. Hồ Chí Minh (SGN)
                    </option>
                    <option value="HAN" disabled={search.from === 'HAN'}>
                      Hà Nội (HAN)
                    </option>
                    <option value="PQC" disabled={search.from === 'PQC'}>
                      Phú Quốc (PQC)
                    </option>
                  </>
                )}
              </select>
            </div>
          </div>

          {/* Date Departure & Return */}
          <div className="md:col-span-3 relative">
            <label className="block text-[11px] font-bold text-slate-500 uppercase tracking-wider mb-1">
              {tripType === 'round-trip' ? 'Ngày đi & Ngày về' : 'Ngày đi'}
            </label>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
              <div className="flex items-center bg-slate-50 border border-slate-200 rounded-xl p-2.5 focus-within:border-sky-500 focus-within:ring-2 focus-within:ring-sky-500/20">
                <input
                  type="date"
                  value={search.date}
                  onChange={(e) => setSearch((prev) => ({ ...prev, date: e.target.value }))}
                  className="w-full bg-transparent border-none text-slate-900 font-bold text-xs focus:outline-none"
                  required
                />
              </div>

              {tripType === 'round-trip' && (
                <div className="flex items-center bg-slate-50 border border-slate-200 rounded-xl p-2.5 focus-within:border-sky-500 focus-within:ring-2 focus-within:ring-sky-500/20">
                  <input
                    type="date"
                    value={search.dateTo || ''}
                    onChange={(e) =>
                      setSearch((prev) => ({ ...prev, dateTo: e.target.value }))
                    }
                    className="w-full bg-transparent border-none text-slate-900 font-bold text-xs focus:outline-none"
                  />
                </div>
              )}
            </div>
          </div>

          {/* Passenger Count */}
          <div className="md:col-span-2 relative">
            <label className="block text-[11px] font-bold text-slate-500 uppercase tracking-wider mb-1">
              Hành khách
            </label>
            <div className="flex items-center bg-slate-50 border border-slate-200 rounded-xl p-3 focus-within:border-sky-500">
              <span className="material-symbols-outlined text-slate-500 mr-2">person</span>
              <input
                type="number"
                min={1}
                max={9}
                value={search.passengers}
                onChange={(e) =>
                  setSearch((prev) => ({ ...prev, passengers: Number(e.target.value) || 1 }))
                }
                className="w-full bg-transparent border-none text-slate-900 font-bold text-sm focus:outline-none"
              />
            </div>
          </div>
        </div>

        {/* Filter Bar: Airline, Price range */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-3 pt-2">
          <div>
            <label className="block text-[11px] font-bold text-slate-500 uppercase tracking-wider mb-1">
              Hãng bay
            </label>
            <select
              value={search.airline || ''}
              onChange={(e) => setSearch((prev) => ({ ...prev, airline: e.target.value }))}
              className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs font-semibold text-slate-800 focus:outline-none focus:border-sky-500"
            >
              <option value="">Tất cả hãng bay</option>
              {airlines.map((al) => (
                <option key={al.code} value={al.code}>
                  {al.name} ({al.code})
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-[11px] font-bold text-slate-500 uppercase tracking-wider mb-1">
              Giá tối thiểu (VNĐ)
            </label>
            <input
              type="number"
              placeholder="Ví dụ: 500000"
              value={search.minPrice || ''}
              onChange={(e) => setSearch((prev) => ({ ...prev, minPrice: e.target.value }))}
              className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs font-semibold text-slate-800 focus:outline-none focus:border-sky-500"
            />
          </div>

          <div>
            <label className="block text-[11px] font-bold text-slate-500 uppercase tracking-wider mb-1">
              Giá tối đa (VNĐ)
            </label>
            <input
              type="number"
              placeholder="Ví dụ: 3000000"
              value={search.maxPrice || ''}
              onChange={(e) => setSearch((prev) => ({ ...prev, maxPrice: e.target.value }))}
              className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs font-semibold text-slate-800 focus:outline-none focus:border-sky-500"
            />
          </div>
        </div>

        {/* Search Submit Button */}
        <div className="flex justify-end pt-2">
          <button
            type="submit"
            disabled={isSearching}
            className="w-full md:w-auto px-8 py-3.5 bg-gradient-to-r from-orange-500 to-amber-500 hover:from-orange-600 hover:to-amber-600 text-white rounded-xl font-bold text-base shadow-lg shadow-orange-500/25 flex items-center justify-center gap-2 transition-all active:scale-95 disabled:opacity-50"
          >
            <span className="material-symbols-outlined">search</span>
            {isSearching ? 'Đang tìm kiếm...' : 'Tìm kiếm chuyến bay'}
          </button>
        </div>
      </form>
    </div>
  )
}
