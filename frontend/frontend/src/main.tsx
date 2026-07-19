import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { initializeKeycloak } from './shared/auth/keycloak.ts'

async function bootstrap() {
  // Attempt to initialize Keycloak (check-sso), but render App regardless
  try {
    await initializeKeycloak()
  } catch (error) {
    console.warn('Keycloak initialization skipped:', error)
  }

  createRoot(document.getElementById('root')!).render(
    <StrictMode>
      <App />
    </StrictMode>,
  )
}

void bootstrap()
