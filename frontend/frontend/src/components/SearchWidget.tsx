import React, { useState } from 'react'
import type { Airline, Airport, FlightSearchParams, FlightStatus } from '../features/flights/types'

interface SearchWidgetProps {
  search: FlightSearchParams
  setSearch: React.Dispatch<React.SetStateAction<FlightSearchParams>>
  airports: Airport[]
  airlines: Airline[]
  onSearch: (e?: React.FormEvent) => void
  onReset: () => void
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
  onReset,
  isSearching,
  tripType,
  setTripType,
}) => {
  const [validationError, setValidationError] = useState('')

  const update = <K extends keyof FlightSearchParams>(key: K, value: FlightSearchParams[K]) => {
    setValidationError('')
    setSearch((current) => ({ ...current, [key]: value }))
  }

  const swapAirports = () => {
    setValidationError('')
    setSearch((current) => ({ ...current, from: current.to, to: current.from }))
  }

  const chooseTripType = (value: 'one-way' | 'round-trip') => {
    setTripType(value)
    if (value === 'one-way') update('dateTo', '')
  }

  const submit = (event: React.FormEvent) => {
    event.preventDefault()
    if (search.from && search.from === search.to) {
      setValidationError('Điểm đi và điểm đến phải khác nhau.')
      return
    }
    if (search.dateTo && !search.date) {
      setValidationError('Hãy chọn ngày đi trước ngày về.')
      return
    }
    if (search.date && search.dateTo && search.dateTo < search.date) {
      setValidationError('Ngày về phải từ ngày đi trở đi.')
      return
    }
    if (search.minPrice && search.maxPrice && Number(search.minPrice) > Number(search.maxPrice)) {
      setValidationError('Giá tối thiểu không thể lớn hơn giá tối đa.')
      return
    }
    setValidationError('')
    onSearch(event)
  }

  return (
    <form className="flight-filter-form" onSubmit={submit}>
      <div className="filter-heading">
        <div>
          <span className="filter-kicker">Bộ lọc hành trình</span>
          <h2>Chọn chuyến phù hợp</h2>
        </div>
        <button type="button" className="filter-reset" onClick={onReset} disabled={isSearching}>
          Đặt lại
        </button>
      </div>

      {validationError && (
        <p className="filter-error" role="alert">
          <span className="material-symbols-outlined" aria-hidden="true">error</span>
          {validationError}
        </p>
      )}

      <fieldset className="trip-type-fieldset">
        <legend>Loại hành trình</legend>
        <div className="trip-type-control">
          <button
            type="button"
            className={tripType === 'one-way' ? 'active' : ''}
            aria-pressed={tripType === 'one-way'}
            onClick={() => chooseTripType('one-way')}
          >
            Một chiều
          </button>
          <button
            type="button"
            className={tripType === 'round-trip' ? 'active' : ''}
            aria-pressed={tripType === 'round-trip'}
            onClick={() => chooseTripType('round-trip')}
          >
            Khứ hồi
          </button>
        </div>
      </fieldset>

      <div className="route-filter-group">
        <div className="filter-field">
          <label htmlFor="filter-from">Điểm đi</label>
          <div className="filter-control with-icon">
            <span className="material-symbols-outlined" aria-hidden="true">flight_takeoff</span>
            <select id="filter-from" value={search.from} onChange={(e) => update('from', e.target.value)}>
              <option value="">Tất cả điểm đi</option>
              {airports.map((airport) => (
                <option key={airport.iataCode} value={airport.iataCode} disabled={airport.iataCode === search.to}>
                  {airport.city} ({airport.iataCode})
                </option>
              ))}
            </select>
          </div>
        </div>

        <button type="button" className="route-swap" onClick={swapAirports} aria-label="Đổi điểm đi và điểm đến">
          <span className="material-symbols-outlined" aria-hidden="true">swap_vert</span>
        </button>

        <div className="filter-field">
          <label htmlFor="filter-to">Điểm đến</label>
          <div className="filter-control with-icon">
            <span className="material-symbols-outlined" aria-hidden="true">flight_land</span>
            <select id="filter-to" value={search.to} onChange={(e) => update('to', e.target.value)}>
              <option value="">Tất cả điểm đến</option>
              {airports.map((airport) => (
                <option key={airport.iataCode} value={airport.iataCode} disabled={airport.iataCode === search.from}>
                  {airport.city} ({airport.iataCode})
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      <div className="filter-pair">
        <div className="filter-field">
          <label htmlFor="filter-date">Ngày đi</label>
          <input id="filter-date" type="date" value={search.date} onChange={(e) => update('date', e.target.value)} />
        </div>
        {tripType === 'round-trip' && (
          <div className="filter-field">
            <label htmlFor="filter-date-to">Ngày về</label>
            <input
              id="filter-date-to"
              type="date"
              min={search.date || undefined}
              value={search.dateTo || ''}
              onChange={(e) => update('dateTo', e.target.value)}
            />
          </div>
        )}
      </div>

      <div className="filter-pair">
        <div className="filter-field">
          <label htmlFor="filter-passengers">Hành khách</label>
          <input
            id="filter-passengers"
            type="number"
            min={1}
            max={9}
            value={search.passengers}
            onChange={(e) => update('passengers', Number(e.target.value) || 1)}
          />
        </div>
        <div className="filter-field">
          <label htmlFor="filter-status">Trạng thái</label>
          <select id="filter-status" value={search.status || ''} onChange={(e) => update('status', e.target.value as FlightStatus | '')}>
            <option value="">Tất cả</option>
            <option value="SCHEDULED">Đúng lịch</option>
            <option value="DELAYED">Bị hoãn</option>
            <option value="DEPARTED">Đã khởi hành</option>
            <option value="CANCELLED">Đã hủy</option>
          </select>
        </div>
      </div>

      <div className="filter-field">
        <label htmlFor="filter-airline">Hãng bay</label>
        <select id="filter-airline" value={search.airline || ''} onChange={(e) => update('airline', e.target.value)}>
          <option value="">Tất cả hãng bay</option>
          {airlines.map((airline) => (
            <option key={airline.code} value={airline.code}>{airline.name} ({airline.code})</option>
          ))}
        </select>
      </div>

      <fieldset className="price-fieldset">
        <legend>Khoảng giá (VNĐ)</legend>
        <div className="filter-pair">
          <input
            type="number"
            min={0}
            step={100000}
            aria-label="Giá tối thiểu"
            placeholder="Từ 500.000"
            value={search.minPrice || ''}
            onChange={(e) => update('minPrice', e.target.value)}
          />
          <input
            type="number"
            min={0}
            step={100000}
            aria-label="Giá tối đa"
            placeholder="Đến 3.000.000"
            value={search.maxPrice || ''}
            onChange={(e) => update('maxPrice', e.target.value)}
          />
        </div>
      </fieldset>

      <div className="filter-pair">
        <div className="filter-field">
          <label htmlFor="filter-sort">Sắp xếp theo</label>
          <select id="filter-sort" value={search.sort || 'departureTime'} onChange={(e) => update('sort', e.target.value as FlightSearchParams['sort'])}>
            <option value="departureTime">Giờ khởi hành</option>
            <option value="arrivalTime">Giờ đến</option>
            <option value="price">Giá vé</option>
          </select>
        </div>
        <div className="filter-field">
          <label htmlFor="filter-order">Thứ tự</label>
          <select id="filter-order" value={search.order || 'asc'} onChange={(e) => update('order', e.target.value as 'asc' | 'desc')}>
            <option value="asc">Tăng dần</option>
            <option value="desc">Giảm dần</option>
          </select>
        </div>
      </div>

      <button type="submit" className="filter-submit" disabled={isSearching}>
        <span className="material-symbols-outlined" aria-hidden="true">search</span>
        {isSearching ? 'Đang lọc chuyến bay' : 'Áp dụng bộ lọc'}
      </button>
    </form>
  )
}
