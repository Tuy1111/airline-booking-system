import Keycloak, { type KeycloakTokenParsed } from 'keycloak-js'

interface AirlineToken extends KeycloakTokenParsed {
  email?: string
  name?: string
  preferred_username?: string
}

export interface AuthenticatedUser {
  email: string
  fullName: string
  roles: string[]
}

export function isAdminUser(user: AuthenticatedUser | null) {
  return Boolean(user?.roles.some((role) => ['ADMIN', 'admin', 'ROLE_ADMIN'].includes(role)))
}

const TOKEN_KEY = 'kc_token'
const REFRESH_TOKEN_KEY = 'kc_refreshToken'

export const keycloak = new Keycloak({
  url: import.meta.env.VITE_KEYCLOAK_URL ?? 'http://localhost:8180',
  realm: import.meta.env.VITE_KEYCLOAK_REALM ?? 'airline-booking',
  clientId: import.meta.env.VITE_KEYCLOAK_CLIENT_ID ?? 'airline-frontend',
})

export async function initializeKeycloak(): Promise<boolean> {
  try {
    const savedToken = localStorage.getItem(TOKEN_KEY) || undefined
    const savedRefreshToken = localStorage.getItem(REFRESH_TOKEN_KEY) || undefined

    const authenticated = await keycloak.init({
      token: savedToken,
      refreshToken: savedRefreshToken,
      pkceMethod: 'S256',
      checkLoginIframe: false,
    })

    if (authenticated && keycloak.token && keycloak.refreshToken) {
      localStorage.setItem(TOKEN_KEY, keycloak.token)
      localStorage.setItem(REFRESH_TOKEN_KEY, keycloak.refreshToken)
    } else if (!authenticated) {
      localStorage.removeItem(TOKEN_KEY)
      localStorage.removeItem(REFRESH_TOKEN_KEY)
    }

    keycloak.onAuthSuccess = () => {
      if (keycloak.token) localStorage.setItem(TOKEN_KEY, keycloak.token)
      if (keycloak.refreshToken) localStorage.setItem(REFRESH_TOKEN_KEY, keycloak.refreshToken)
    }

    keycloak.onAuthRefreshSuccess = () => {
      if (keycloak.token) localStorage.setItem(TOKEN_KEY, keycloak.token)
      if (keycloak.refreshToken) localStorage.setItem(REFRESH_TOKEN_KEY, keycloak.refreshToken)
    }

    keycloak.onAuthLogout = () => {
      localStorage.removeItem(TOKEN_KEY)
      localStorage.removeItem(REFRESH_TOKEN_KEY)
    }

    return authenticated
  } catch (error) {
    console.warn('Keycloak authentication server is currently unreachable or offline:', error)
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(REFRESH_TOKEN_KEY)
    return false
  }
}

export function currentUser(): AuthenticatedUser | null {
  if (!keycloak.authenticated || !keycloak.tokenParsed) return null

  const token = keycloak.tokenParsed as AirlineToken
  return {
    email: token.email ?? '',
    fullName: token.name ?? token.preferred_username ?? token.email ?? 'Keycloak user',
    roles: token.realm_access?.roles ?? [],
  }
}

export function login() {
  return keycloak.login({ redirectUri: window.location.href })
}

export function register() {
  return keycloak.register({ redirectUri: window.location.href })
}

export function logout() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(REFRESH_TOKEN_KEY)
  return keycloak.logout({ redirectUri: window.location.origin })
}

export async function accessToken(): Promise<string | null> {
  try {
    if (!keycloak.authenticated) return null
    await keycloak.updateToken(30)
    if (keycloak.token) localStorage.setItem(TOKEN_KEY, keycloak.token)
    if (keycloak.refreshToken) localStorage.setItem(REFRESH_TOKEN_KEY, keycloak.refreshToken)
    return keycloak.token ?? null
  } catch {
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(REFRESH_TOKEN_KEY)
    return null
  }
}
