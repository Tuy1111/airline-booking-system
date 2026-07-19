import { apiRequest } from '../../shared/api/http'
import { serviceBases } from '../../shared/config/services'
import type {
  Airline,
  AirlineCreateRequest,
  Airport,
  AirportCreateRequest,
  FlightCreateRequest,
  FlightDetail,
  FlightSearchParams,
  FlightStatusUpdateRequest,
  FlightSummary,
  RouteCreateRequest,
  RouteInfo,
  SeatInfo,
  SeatMapItem,
} from './types'

const baseUrl = serviceBases.flight
const root = '/api/v1'

export const flightApi = {
  searchFlights(params: FlightSearchParams) {
    return apiRequest<FlightSummary[]>(baseUrl, `${root}/flights`, {
      query: params,
    })
  },
  upcoming(hours = 24) {
    return apiRequest<FlightSummary[]>(baseUrl, `${root}/flights/upcoming`, {
      query: { hours },
    })
  },
  getFlight(id: number) {
    return apiRequest<FlightDetail>(baseUrl, `${root}/flights/${id}`)
  },
  getSeatMap(flightId: number) {
    return apiRequest<SeatMapItem[]>(baseUrl, `${root}/flights/${flightId}/seats`)
  },
  checkSeat(flightId: number, seatNo: string) {
    return apiRequest<SeatInfo>(
      baseUrl,
      `${root}/flights/${flightId}/seats/${encodeURIComponent(seatNo)}`,
    )
  },
  holdSeat(flightId: number, seatNo: string) {
    return apiRequest<SeatInfo>(
      baseUrl,
      `${root}/flights/${flightId}/seats/${encodeURIComponent(seatNo)}/hold`,
      { method: 'PUT' },
    )
  },
  bookSeat(flightId: number, seatNo: string) {
    return apiRequest<SeatInfo>(
      baseUrl,
      `${root}/flights/${flightId}/seats/${encodeURIComponent(seatNo)}/book`,
      { method: 'PUT' },
    )
  },
  releaseSeat(flightId: number, seatNo: string) {
    return apiRequest<SeatInfo>(
      baseUrl,
      `${root}/flights/${flightId}/seats/${encodeURIComponent(seatNo)}/release`,
      { method: 'PUT' },
    )
  },
  updateStatus(id: number, request: FlightStatusUpdateRequest) {
    return apiRequest<FlightDetail>(baseUrl, `${root}/flights/${id}/status`, {
      method: 'PUT',
      body: request,
    })
  },
  createFlight(request: FlightCreateRequest) {
    return apiRequest<FlightDetail>(baseUrl, `${root}/flights`, {
      method: 'POST',
      body: request,
    })
  },
  updateFlight(id: number, request: Partial<FlightCreateRequest>) {
    return apiRequest<FlightDetail>(baseUrl, `${root}/flights/${id}`, {
      method: 'PUT',
      body: request,
    })
  },
  deleteFlight(id: number) {
    return apiRequest<void>(baseUrl, `${root}/flights/${id}`, {
      method: 'DELETE',
    })
  },
  getAirports() {
    return apiRequest<Airport[]>(baseUrl, `${root}/airports`)
  },
  createAirport(request: AirportCreateRequest) {
    return apiRequest<Airport>(baseUrl, `${root}/airports`, {
      method: 'POST',
      body: request,
    })
  },
  getAirlines() {
    return apiRequest<Airline[]>(baseUrl, `${root}/airlines`)
  },
  createAirline(request: AirlineCreateRequest) {
    return apiRequest<Airline>(baseUrl, `${root}/airlines`, {
      method: 'POST',
      body: request,
    })
  },
  getRoutes() {
    return apiRequest<RouteInfo[]>(baseUrl, `${root}/routes`)
  },
  createRoute(request: RouteCreateRequest) {
    return apiRequest<RouteInfo>(baseUrl, `${root}/routes`, {
      method: 'POST',
      body: request,
    })
  },
}
