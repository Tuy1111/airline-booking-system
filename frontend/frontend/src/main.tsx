import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { initializeKeycloak } from './shared/auth/keycloak.ts'

async function bootstrap() {
  try {
    await initializeKeycloak()
    createRoot(document.getElementById('root')!).render(
      <StrictMode>
        <App />
      </StrictMode>,
    )
  } catch (error) {
    const message = error instanceof Error ? error.message : String(error)
    createRoot(document.getElementById('root')!).render(
      <main className="content-shell">
        <h1>Không thể kết nối Keycloak</h1>
        <p>{message}</p>
      </main>,
    )
  }
}

void bootstrap()
