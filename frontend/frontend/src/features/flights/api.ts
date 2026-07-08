import { apiRequest } from '../../shared/api/http'
import { serviceBases } from '../../shared/config/services'
import type {
  Airline,
  Airport,
  FlightCreateRequest,
  FlightDetail,
  FlightSearchParams,
  FlightStatusUpdateRequest,
  FlightSummary,
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
  getAirports() {
    return apiRequest<Airport[]>(baseUrl, `${root}/airports`)
  },
  getAirlines() {
    return apiRequest<Airline[]>(baseUrl, `${root}/airlines`)
  },
  getRoutes() {
    return apiRequest<RouteInfo[]>(baseUrl, `${root}/routes`)
  },
}
