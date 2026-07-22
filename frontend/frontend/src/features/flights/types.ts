export type FlightStatus = 'SCHEDULED' | 'DELAYED' | 'CANCELLED' | 'DEPARTED'
export type SeatStatus = 'AVAILABLE' | 'HELD' | 'BOOKED'
export type SeatClass = 'ECONOMY' | 'BUSINESS' | 'FIRST'

export interface FlightSearchParams
  extends Record<string, string | number | boolean | null | undefined> {
  from: string
  to: string
  date: string
  passengers: number
  status?: FlightStatus | ''
  airline?: string
  minPrice?: string
  maxPrice?: string
  dateTo?: string
  sort?: 'price' | 'departureTime' | 'arrivalTime'
  order?: 'asc' | 'desc'
}

export interface FlightSummary {
  id: number
  flightNo: string
  airlineCode: string
  airlineName: string
  fromAirport: string
  fromCity: string
  toAirport: string
  toCity: string
  departureTime: string
  arrivalTime: string
  basePrice: number
  currentPrice: number
  aircraftType: string | null
  status: FlightStatus
  availableSeats: number
}

export interface FlightDetail extends FlightSummary {
  totalSeats: number
  heldSeats: number
  bookedSeats: number
}

export interface SeatMapItem {
  seatNo: string
  seatClass: SeatClass
  status: SeatStatus
  priceFactor: number
}

export interface SeatInfo {
  flightId: number
  seatNo: string
  seatClass: SeatClass
  status: SeatStatus
  price: number
}

export interface Airport {
  iataCode: string
  name: string
  city: string
  country: string
}

export interface AirportCreateRequest {
  iataCode: string
  name: string
  city: string
  country: string
}

export interface Airline {
  code: string
  name: string
}

export interface AirlineCreateRequest {
  code: string
  name: string
}

export interface RouteInfo {
  id: number
  fromAirportCode: string
  fromAirportName: string
  fromCity: string
  toAirportCode: string
  toAirportName: string
  toCity: string
  distanceKm: number | null
}

export interface RouteCreateRequest {
  fromAirport: string
  toAirport: string
  distanceKm: number
}

export interface FlightCreateRequest {
  flightNo: string
  fromAirportCode: string
  toAirportCode: string
  airlineCode: string
  departureTime: string
  arrivalTime: string
  totalSeats: number
  basePrice: number
  aircraftType: string
}

export interface FlightStatusUpdateRequest {
  status: FlightStatus
  newDepartureTime?: string
  newArrivalTime?: string
}
