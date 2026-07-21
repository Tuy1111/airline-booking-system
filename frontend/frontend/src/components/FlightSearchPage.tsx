import React, { useEffect, useMemo, useState } from 'react'
import type { Airline, Airport, FlightSearchParams, FlightSummary } from '../features/flights/types'
import { SearchWidget } from './SearchWidget'

interface FlightSearchPageProps {
  search: FlightSearchParams
  setSearch: React.Dispatch<React.SetStateAction<FlightSearchParams>>
  airports: Airport[]
  airlines: Airline[]
  flights: FlightSummary[]
  onSearch: (e?: React.FormEvent) => void
  onReset: () => void
  onSelectFlight: (flight: FlightSummary) => void
  isLoading: boolean
  error: string
  tripType: 'one-way' | 'round-trip'
  setTripType: (type: 'one-way' | 'round-trip') => void
  formatMoney: (val: number | string | null | undefined) => string
  formatDateTime: (val: string | null | undefined) => string
  durationLabel: (start: string, end: string) => string
  canBook: boolean
}

const statusLabel: Record<FlightSummary['status'], string> = {
  SCHEDULED: 'Đúng lịch',
  DELAYED: 'Bị hoãn',
  CANCELLED: 'Đã hủy',
  DEPARTED: 'Đã khởi hành',
}

const clock = (value: string) => new Date(value).toLocaleTimeString('vi-VN', {
  hour: '2-digit',
  minute: '2-digit',
})

const day = (value: string) => new Date(value).toLocaleDateString('vi-VN', {
  weekday: 'short',
  day: '2-digit',
  month: '2-digit',
})

const PAGE_SIZE = 5

export const FlightSearchPage: React.FC<FlightSearchPageProps> = ({
  search,
  setSearch,
  airports,
  airlines,
  flights,
  onSearch,
  onReset,
  onSelectFlight,
  isLoading,
  error,
  tripType,
  setTripType,
  formatMoney,
  durationLabel,
  canBook,
}) => {
  const [filtersOpen, setFiltersOpen] = useState(false)
  const [page, setPage] = useState(1)
  const hasFilters = Boolean(
    search.from || search.to || search.date || search.dateTo || search.status || search.airline || search.minPrice || search.maxPrice,
  )
  const airportOptions = useMemo(() => {
    const options = new Map(airports.map((airport) => [airport.iataCode, airport]))
    flights.forEach((flight) => {
      if (!options.has(flight.fromAirport)) {
        options.set(flight.fromAirport, { iataCode: flight.fromAirport, city: flight.fromCity, name: flight.fromCity, country: 'VN' })
      }
      if (!options.has(flight.toAirport)) {
        options.set(flight.toAirport, { iataCode: flight.toAirport, city: flight.toCity, name: flight.toCity, country: 'VN' })
      }
    })
    return [...options.values()].sort((a, b) => a.city.localeCompare(b.city, 'vi'))
  }, [airports, flights])
  const airlineOptions = useMemo(() => {
    const options = new Map(airlines.map((airline) => [airline.code, airline]))
    flights.forEach((flight) => options.set(flight.airlineCode, { code: flight.airlineCode, name: flight.airlineName }))
    return [...options.values()].sort((a, b) => a.name.localeCompare(b.name, 'vi'))
  }, [airlines, flights])
  const bookableFlights = useMemo(
    () => flights.filter((flight) => flight.status === 'SCHEDULED' && new Date(flight.departureTime).getTime() > Date.now()),
    [flights],
  )
  const totalPages = Math.max(1, Math.ceil(bookableFlights.length / PAGE_SIZE))
  const currentPage = Math.min(page, totalPages)
  const visibleFlights = bookableFlights.slice((currentPage - 1) * PAGE_SIZE, currentPage * PAGE_SIZE)

  useEffect(() => setPage(1), [flights])

  return (
    <div className="flight-search-page search-redesign">
      <div className="flight-results-layout">
        <aside className={`flight-search-sidebar ${filtersOpen ? 'mobile-open' : ''}`} aria-label="Bộ lọc chuyến bay">
          <button
            type="button"
            className="mobile-filter-toggle"
            aria-expanded={filtersOpen}
            onClick={() => setFiltersOpen((open) => !open)}
          >
            <span><span className="material-symbols-outlined" aria-hidden="true">tune</span> Bộ lọc chuyến bay</span>
            <span>{hasFilters ? 'Đang áp dụng' : 'Tùy chọn'} <span className="material-symbols-outlined" aria-hidden="true">expand_more</span></span>
          </button>
          <SearchWidget
            search={search}
            setSearch={setSearch}
            airports={airportOptions}
            airlines={airlineOptions}
            onSearch={(event) => {
              onSearch(event)
              setFiltersOpen(false)
            }}
            onReset={onReset}
            isSearching={isLoading}
            tripType={tripType}
            setTripType={setTripType}
          />
        </aside>

        <section className="flight-list-region" aria-labelledby="flight-results-heading" aria-live="polite">
          <div className="result-toolbar">
            <div>
              <span className="result-count">{isLoading ? '...' : bookableFlights.length}</span>
              <div>
                <h2 id="flight-results-heading">{hasFilters ? 'Kết quả phù hợp' : 'Danh sách chuyến bay'}</h2>
                <p>{hasFilters ? 'Theo bộ lọc đang áp dụng' : 'Chưa áp dụng bộ lọc'}</p>
              </div>
            </div>
            <span className="result-live-note"><i aria-hidden="true" /> Giá được cập nhật theo số ghế còn lại</span>
          </div>

          {isLoading && (
            <div className="flight-skeleton-list" aria-label="Đang tải chuyến bay">
              {[0, 1, 2].map((item) => <div className="flight-skeleton" key={item} />)}
            </div>
          )}

          {!isLoading && error && (
            <div className="result-state result-error" role="alert">
              <span className="material-symbols-outlined" aria-hidden="true">cloud_off</span>
              <div>
                <h3>Chưa thể tải danh sách chuyến bay</h3>
                <p>{error}</p>
              </div>
              <button type="button" onClick={() => onSearch()}>Thử lại</button>
            </div>
          )}

          {!isLoading && !error && bookableFlights.length === 0 && (
            <div className="result-state result-empty">
              <span className="material-symbols-outlined" aria-hidden="true">travel_explore</span>
              <div>
                <h3>Không có chuyến bay phù hợp</h3>
                <p>Hãy nới khoảng ngày, giá vé hoặc đặt lại bộ lọc để xem toàn bộ chuyến bay.</p>
              </div>
              <button type="button" onClick={onReset}>Xem tất cả chuyến bay</button>
            </div>
          )}

          {!isLoading && !error && bookableFlights.length > 0 && (
            <div className="flight-card-list">
              {visibleFlights.map((flight, index) => (
                <article
                  className="flight-result-card"
                  key={flight.id}
                  style={{ animationDelay: `${Math.min(index, 8) * 55}ms` }}
                >
                  <div className="flight-card-meta">
                    <span className="airline-monogram" aria-hidden="true">{flight.airlineCode}</span>
                    <div>
                      <strong>{flight.airlineName}</strong>
                      <span>{flight.flightNo} · {flight.aircraftType || 'Máy bay tiêu chuẩn'}</span>
                    </div>
                    <span className={`flight-status status-${flight.status.toLowerCase()}`}>{statusLabel[flight.status]}</span>
                  </div>

                  <div className="flight-card-schedule">
                    <div className="schedule-point">
                      <time dateTime={flight.departureTime}>{clock(flight.departureTime)}</time>
                      <strong>{flight.fromAirport}</strong>
                      <span>{flight.fromCity} · {day(flight.departureTime)}</span>
                    </div>

                    <div className="flight-path" aria-label={`${durationLabel(flight.departureTime, flight.arrivalTime)}, bay thẳng`}>
                      <span>{durationLabel(flight.departureTime, flight.arrivalTime)}</span>
                      <div><i /><span className="material-symbols-outlined" aria-hidden="true">flight</span><i /></div>
                      <small>Bay thẳng</small>
                    </div>

                    <div className="schedule-point end">
                      <time dateTime={flight.arrivalTime}>{clock(flight.arrivalTime)}</time>
                      <strong>{flight.toAirport}</strong>
                      <span>{flight.toCity} · {day(flight.arrivalTime)}</span>
                    </div>
                  </div>

                  <div className="flight-card-booking">
                    <div>
                      <span>Giá mỗi hành khách</span>
                      <strong>{formatMoney(flight.currentPrice || flight.basePrice)}</strong>
                      <small>{flight.availableSeats} ghế còn lại</small>
                    </div>
                    <button type="button" onClick={() => onSelectFlight(flight)} disabled={!canBook}>
                      {canBook ? 'Chọn chuyến' : 'Tài khoản quản trị'}
                      {canBook && <span className="material-symbols-outlined" aria-hidden="true">arrow_forward</span>}
                    </button>
                  </div>
                </article>
              ))}
            </div>
          )}

          {!isLoading && !error && totalPages > 1 && (
            <nav className="flight-pagination" aria-label="Phân trang chuyến bay">
              <button type="button" onClick={() => setPage((current) => current - 1)} disabled={currentPage === 1}>
                <span className="material-symbols-outlined" aria-hidden="true">arrow_back</span>
                Trang trước
              </button>
              <span>Trang <strong>{currentPage}</strong> / {totalPages}</span>
              <button type="button" onClick={() => setPage((current) => current + 1)} disabled={currentPage === totalPages}>
                Trang sau
                <span className="material-symbols-outlined" aria-hidden="true">arrow_forward</span>
              </button>
            </nav>
          )}
        </section>
      </div>
    </div>
  )
}
