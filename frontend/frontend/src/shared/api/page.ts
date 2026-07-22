export interface PageResponse<T> {
  content: T[]
  number: number
  size: number
  totalElements: number
  totalPages: number
}

export function pageItems<T>(value: PageResponse<T> | T[] | undefined) {
  if (!value) {
    return []
  }

  return Array.isArray(value) ? value : value.content
}
