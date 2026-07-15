export type QueryValue = string | number | boolean | null | undefined
export type QueryParams = Record<string, QueryValue>

export interface ApiRequestOptions extends Omit<RequestInit, 'body'> {
  body?: unknown
  query?: QueryParams
}

export class ApiError extends Error {
  readonly status: number
  readonly detail: string
  readonly payload: unknown

  constructor(status: number, detail: string, payload: unknown) {
    super(detail)
    this.name = 'ApiError'
    this.status = status
    this.detail = detail
    this.payload = payload
  }
}

const trimSlash = (value: string) => value.replace(/\/+$/, '')

export function joinUrl(baseUrl: string, path: string, query?: QueryParams) {
  const normalizedBase = trimSlash(baseUrl)
  const normalizedPath = path.startsWith('/') ? path : `/${path}`
  const url = new URL(`${normalizedBase}${normalizedPath}`, window.location.origin)

  Object.entries(query ?? {}).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') {
      url.searchParams.set(key, String(value))
    }
  })

  return normalizedBase ? url.toString() : `${url.pathname}${url.search}`
}

async function parseResponse(response: Response) {
  const contentType = response.headers.get('content-type') ?? ''

  if (response.status === 204) {
    return undefined
  }

  if (contentType.includes('application/json')) {
    return response.json()
  }

  return response.text()
}

function getErrorMessage(payload: unknown, fallback: string) {
  if (payload && typeof payload === 'object') {
    const data = payload as Record<string, unknown>
    const detail = data.detail ?? data.message ?? data.error ?? data.title
    if (typeof detail === 'string' && detail.trim()) {
      return detail
    }
  }

  if (typeof payload === 'string' && payload.trim()) {
    return payload
  }

  return fallback
}

export async function apiRequest<T>(
  baseUrl: string,
  path: string,
  options: ApiRequestOptions = {},
): Promise<T> {
  const { body, query, headers, ...init } = options
  const response = await fetch(joinUrl(baseUrl, path, query), {
    ...init,
    headers: {
      Accept: 'application/json',
      ...(body === undefined ? {} : { 'Content-Type': 'application/json' }),
      ...headers,
    },
    body: body === undefined ? undefined : JSON.stringify(body),
  })
  const payload = await parseResponse(response)

  if (!response.ok) {
    const proxyStatuses = [502, 503, 504]
    throw new ApiError(
      response.status,
      proxyStatuses.includes(response.status)
        ? 'Hệ thống tạm thời chưa kết nối được. Vui lòng thử lại sau.'
        : getErrorMessage(payload, `Request failed with status ${response.status}`),
      payload,
    )
  }

  return payload as T
}

export async function tryFallback<T>(
  primary: () => Promise<T>,
  fallback: () => Promise<T>,
): Promise<T> {
  try {
    return await primary()
  } catch (error) {
    if (error instanceof ApiError && error.status === 404) {
      return fallback()
    }

    throw error
  }
}
