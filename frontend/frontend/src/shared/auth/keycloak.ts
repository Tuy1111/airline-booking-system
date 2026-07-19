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

export const keycloak = new Keycloak({
  url: import.meta.env.VITE_KEYCLOAK_URL ?? 'http://localhost:8180',
  realm: import.meta.env.VITE_KEYCLOAK_REALM ?? 'airline-booking',
  clientId: import.meta.env.VITE_KEYCLOAK_CLIENT_ID ?? 'airline-frontend',
})

export async function initializeKeycloak() {
  return keycloak.init({ onLoad: 'check-sso', pkceMethod: 'S256', checkLoginIframe: false })
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
  return keycloak.logout({ redirectUri: window.location.origin })
}

export async function accessToken() {
  if (!keycloak.authenticated) return null
  await keycloak.updateToken(30)
  return keycloak.token ?? null
}
