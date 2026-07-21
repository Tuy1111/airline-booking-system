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
  onReset: () => void
  onSelectFlight: (flight: FlightSummary) => void
  isLoading: boolean
  error: string
  tripType: 'one-way' | 'round-trip'
  setTripType: (type: 'one-way' | 'round-trip') => void
  formatMoney: (val: number | string | null | undefined) => string
  formatDateTime: (val: string | null | undefined) => string
  durationLabel: (start: string, end: string) => string
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
}) => {
  const [filtersOpen, setFiltersOpen] = useState(false)
  const hasFilters = Boolean(
    search.from || search.to || search.date || search.dateTo || search.status || search.airline || search.minPrice || search.maxPrice,
  )

  return (
    <div className="flight-search-page search-redesign">
      <header className="search-route-header">
        <div>
          <p className="eyebrow">Tìm vé máy bay</p>
          <h1>{hasFilters ? 'Chuyến bay theo hành trình của bạn.' : 'Tất cả chuyến bay hiện có.'}</h1>
        </div>
        <p>
          So sánh lịch bay, thời lượng, chỗ còn lại và giá vé trong cùng một màn hình.
          Bộ lọc chỉ thu hẹp danh sách khi bạn cần.
        </p>
      </header>

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
            airports={airports}
            airlines={airlines}
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
              <span className="result-count">{isLoading ? '—' : flights.length}</span>
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

          {!isLoading && !error && flights.length === 0 && (
            <div className="result-state result-empty">
              <span className="material-symbols-outlined" aria-hidden="true">travel_explore</span>
              <div>
                <h3>Không có chuyến bay phù hợp</h3>
                <p>Hãy nới khoảng ngày, giá vé hoặc đặt lại bộ lọc để xem toàn bộ chuyến bay.</p>
              </div>
              <button type="button" onClick={onReset}>Xem tất cả chuyến bay</button>
            </div>
          )}

          {!isLoading && !error && flights.length > 0 && (
            <div className="flight-card-list">
              {flights.map((flight, index) => (
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
                    <button type="button" onClick={() => onSelectFlight(flight)} disabled={flight.status === 'CANCELLED'}>
                      {flight.status === 'CANCELLED' ? 'Không khả dụng' : 'Chọn chuyến'}
                      {flight.status !== 'CANCELLED' && <span className="material-symbols-outlined" aria-hidden="true">arrow_forward</span>}
                    </button>
                  </div>
                </article>
              ))}
            </div>
          )}
        </section>
      </div>
    </div>
  )
}
